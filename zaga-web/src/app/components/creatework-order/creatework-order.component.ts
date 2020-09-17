import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Worker } from 'src/app/models/Worker';
import { Material } from 'src/app/models/Material';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

import { WorkOrderService } from 'src/app/service/work-order.service';
import { CropService } from 'src/app/service/crop.service';
import { Crop } from 'src/app/models/Crop';
import { WorkOrder } from 'src/app/models/WorkOrder';
import { UserService } from 'src/app/service/user.service';
import { Employee } from 'src/app/models/Employee';
import { Operation } from 'src/app/models/Operation';
import { OperationService } from 'src/app/service/operation.service';
import { CultureService } from 'src/app/service/culture.service';
import { Culture } from 'src/app/models/Culture';
import { Machine } from 'src/app/models/Machine';
import { MachineService } from 'src/app/service/machine.service';
import { MaterialService } from 'src/app/service/material.service';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-creatework-order',
  templateUrl: './creatework-order.component.html',
  styleUrls: ['./creatework-order.component.css']
})
export class CreateworkOrderComponent implements OnInit {

  constructor(private route: ActivatedRoute, 
    private toastr: ToastrService, 
    private router: Router,
    private userService: UserService,
    private operationService:OperationService,
    private cultureService:CultureService,
    private machineService:MachineService,
    private materialService:MaterialService,
    private workOrderService:WorkOrderService,
    private cropService:CropService) { 
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
  }

  tables = ["T-27", "T-28", "T-29", "T-30", "T-31", "T-37", "T-39", "T-40", "T-42", "T-44", "T-46", "T-47", "T-48", "T-52", "T-53", "T-54", 
  "T-55", "T-56", "T-57", "T-58", "T-59", "T-60", "T-61", "T-62", "T-63", "T-64", "T-65", "T-66", "T-67", "T-68"];


  modalHeader = "Dodavanje radnika";
  workers = false;
  machines = false;
  materials = false;
  editing = false;
  editingMachine = false;
  editingMaterial = false;
  workId = this.route.snapshot.params.workId;
  query = '';
  material : Material = new Material();
  idOfEditingMaterial:any = 0;
  idOfEditingWorker:any = 0;
  idOfEditingMachine:any = 0;
  new = false;
  inputFill = true;
  workerMob:Worker = new Worker();
  allEmployees : Employee[] = [];
  operations : Operation[] = [];
  cultures : Culture[] = [];
  devices : Machine[] = [];
  substances : Material[] =[];

  operation: Operation = new Operation();
  crop: Crop = new Crop();
  culture: Culture = new Culture();
  employee: Employee = new Employee();
  workOrder : WorkOrder = new WorkOrder();
  worker : Worker = new Worker();
  employees: Employee[] = [];
  machine: Machine = new Machine();
  woMachines: Machine[] = [];
  woMaterials: Material[] = [];
  cultureId;
  startDate:{}
  endDate:{}
  selectedWorker;
  selectedMachine;
  selectedMaterial;

  nameFC : FormControl = new FormControl("");
  
  filteredOptions: Observable<string[]>;

  ngOnInit() {
    this.query = this.nameFC.value;
    if(this.workId == "new") { //new
      this.new = true;
      this.workOrder = new WorkOrder();
      this.workOrder.machines = [];
      this.workOrder.workers = [];
      this.workOrder.materials = [];
      this.workOrder.status = "Novi"
      
    } else {
      this.new = false;

      this.workOrderService.getOne(this.workId).subscribe(data => {
        this.workOrder = data;
        
        if(this.workOrder.status == "NEW"){
          this.workOrder.status = "Novi";
        } else if(this.workOrder.status == "IN_PROGRESS"){
          this.workOrder.status = "U radu";
        } else if(this.workOrder.status == "CLOSED"){
          this.workOrder.status = "Zatvoren";
        }

        this.workOrder.start = { day: +this.workOrder.start.substring(8,10),
          month: +this.workOrder.start.substring(5,7), 
          year: +this.workOrder.start.substring(0,4)
        };
        this.workOrder.end = { day: +this.workOrder.end.substring(8,10), 
          month: +this.workOrder.end.substring(5,7), 
          year: +this.workOrder.end.substring(0,4)
       };


       this.getOperation(this.workOrder.operationId);
       this.getCrop(this.workOrder.cropId);
       this.getResponsibleEmployee(this.workOrder.responsibleId);

       this.workOrder.workers.forEach(data => {
        this.employee = data;
        this.getWorker(this.employee.userId);
       })
       console.log(this.workOrder)
      })
    }

    this.userService.getAll().subscribe(data=>{
      this.allEmployees = data.content;
    })

    this.operationService.getAll().subscribe(data=>{
      this.operations = data;
    })
    this.cultureService.getAll().subscribe(data=>{
      this.cultures = data;
    })
    this.machineService.getAll().subscribe(data=>{
      this.devices = data;
    })

    this.materialService.getAll().subscribe(data=>{
      this.substances = data;
    })
  }

  getOperation(id){
    this.operationService.getOne(id).subscribe(data => {
      this.operation = data;
    })
  }

  getCrop(id){
    this.cropService.getOne(id).subscribe(data => {
      this.crop = data;
      this.cultureId = this.crop.cultureId;
      this.getCulture(this.cultureId);
    })
  }

  getCulture(id){
    this.cultureService.getOne(id).subscribe(data => {
      this.culture = data;
    })
  }

  getResponsibleEmployee(id){
    this.userService.getOne(id).subscribe(data => {
      this.employee = data;
    })
  }

  getWorker(id){
    this.userService.getOne(id).subscribe(data => {
      this.employee = data;
      this.employees.push(this.employee);
    })
  }

  expandWorkers() {
    this.workers = !this.workers;
    let el = document.getElementById("rad");
    setTimeout(()=>{el.scrollIntoView({behavior:"smooth"})}, 500);
    
  }
  expandMachines() {
    this.machines = !this.machines;
    let el = document.getElementById("mas");
    setTimeout(()=>{el.scrollIntoView({behavior:"smooth"})}, 500);
    
  }
  expandMaterials() {
    this.materials = !this.materials;
    let el = document.getElementById("mat");
    setTimeout(()=>{el.scrollIntoView({behavior:"smooth"})}, 500);
    
  }

  addWorker(){
    this.userService.getOne(this.selectedWorker).subscribe(data => {
      this.employee = data;
      this.employee.name = data.Name;
      this.employees.push(this.employee);
    })
    this.employee = new Employee();
  }

  editWorker(worker){
    this.employee.id = worker.id;
    this.employee.name = worker.Name;
    this.editing = true;
    this.idOfEditingWorker = worker.id;
  }

  editExistingWorker(){
    this.employees.forEach(employee => {
      if(employee.id == this.idOfEditingWorker){
        this.userService.getOne(this.selectedWorker).subscribe(data => {
          employee.name = data.Name;
        })
      }
    })
    this.employee = new Employee();
    this.editing = false;
  }

  addMachine(){
    this.machineService.getOne(this.selectedMachine).subscribe(data => {
      this.machine = data;
      this.machine.name = data.Name;
      this.woMachines.push(this.machine);
    })
    this.machine = new Machine();
  }

  editMachine(machine){
    this.machine.id = machine.id
    this.machine.name = machine.Name;
    this.editingMachine = true;
    this.idOfEditingMachine = machine.id;
  }

  editExistingMachine(){
    this.woMachines.forEach(machine => {
      if(machine.id == this.idOfEditingMachine){
        this.machineService.getOne(this.selectedMachine).subscribe(data => {
          machine.name = data.Name;
        })
      }
    })
    this.machine = new Machine();
    this.editingMachine = false;
  }

  addMaterial(){
    this.materialService.getOne(this.selectedMaterial).subscribe(data => {
      this.material = data;
      this.material.name = data.Name;
      this.woMaterials.push(this.material);
    })
    this.material = new Material();
  }

  editMaterial(material){
    this.material.id = material.id;
    this.material.name = material.Name;
    this.editingMaterial = true;
    this.idOfEditingMaterial = material.id;
  }

  editExistingMaterial(){
    this.woMaterials.forEach(material => {
      if(material.id == this.idOfEditingMaterial){
        this.materialService.getOne(this.selectedMaterial).subscribe(data => {
          material.name = data.Name;
        })
      }
    })
    this.material = new Material();
    this.editingMaterial = false;
  }

  addWorkOrder(){

    this.workOrder.start = '2020-09-16';
    this.workOrder.end = "2020-09-17";
    this.workOrder.cropId = "268f6ee1-17fc-44ea-908c-02c218a9a031";
    this.workOrder.machines = this.woMachines;
    this.workOrder.workers = this.employees;
    this.workOrder.materials = this.woMaterials;
    this.workOrder.responsibleId = this.nameFC.value;
    
    this.workOrderService.addWorkOrder(this.workOrder).subscribe(data => {
      this.toastr.success("Uspešno kreiran radni nalog.");
    }, error => {
      this.toastr.error("Radni nalog nije kreiran.");
    })
    
    console.log(this.workOrder)
  }

  updateWorkOrder(){
    var dateStartToAdd = "";
    var dateEndToAdd = "";
    if(this.workOrder.start != undefined) {
        if(this.workOrder.start.month < 10) {
          this.workOrder.start.month = '0' + this.workOrder.start.month;
        }
        if(this.workOrder.start.day < 10){
          this.workOrder.start.day = '0' + this.workOrder.start.day;
        }
        dateStartToAdd= this.workOrder.start.year + '-' 
          + this.workOrder.start.month + '-' 
          + this.workOrder.start.day;
    }
    if(this.workOrder.end != undefined) {
      if(this.workOrder.end.month < 10) {
        this.workOrder.end.month = '0' + this.workOrder.end.month;
      }
      if(this.workOrder.end.day < 10){
        this.workOrder.end.day = '0' + this.workOrder.end.day;
      }
      dateEndToAdd= this.workOrder.end.year + '-' 
        + this.workOrder.end.month + '-' 
        + this.workOrder.end.day;
    }
   
    this.workOrder.machines = this.woMachines;
    this.workOrder.workers = this.employees;
    this.workOrder.materials = this.woMaterials;

    /*
    this.workOrderService.updateWorkOrder(this.wo).subscribe(data => {
      this.toastr.success("Uspešno sačuvan radni nalog.");
    }, error => {
      this.toastr.error("Radni nalog nije sačuvan.");
    })
    */
    console.log(this.workOrder)
  }

  setForEdit(wor){
    this.workerMob = wor;
    console.log(wor)
  }

  getEmpName(id) {
    return this.allEmployees.find(emp => emp.id === id).name;
  }

}
