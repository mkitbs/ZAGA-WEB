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
  wo : WorkOrder = new WorkOrder();
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

  ngOnInit() {
    if(this.workId == "new") { //new
      this.new = true;
      this.wo = new WorkOrder();
      this.wo.machines = [];
      this.wo.workers = [];
      this.wo.materials = [];
      this.wo.status = "Novi";
    } else {
      this.new = false;

      this.workOrderService.getOne(this.workId).subscribe(data => {
        this.wo = data;
        
        if(this.wo.status == "NEW"){
          this.wo.status = "Novi";
        } else if(this.wo.status == "IN_PROGRESS"){
          this.wo.status = "U radu";
        } else if(this.wo.status == "CLOSED"){
          this.wo.status = "Zatvoren";
        }

        this.wo.start = { day: +this.wo.start.substring(8,10),
          month: +this.wo.start.substring(5,7), 
          year: +this.wo.start.substring(0,4)
        };
        this.wo.end = { day: +this.wo.end.substring(8,10), 
          month: +this.wo.end.substring(5,7), 
          year: +this.wo.end.substring(0,4)
       };


       this.getOperation(this.wo.operationId);
       this.getCrop(this.wo.cropId);
       this.getResponsibleEmployee(this.wo.responsibleId);

       this.wo.workers.forEach(data => {
        this.employee = data;
        this.getWorker(this.employee.userId);
       })
       console.log(this.wo)
      })
    }

    this.userService.getAll().subscribe(data=>{
      this.allEmployees = data;
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
    this.employee.name = this.selectedWorker;
    this.employees.push(this.employee);
    this.employee = new Employee();
  }

  editWorker(worker){
    this.employee.name = worker.name;
    this.editing = true;
    this.idOfEditingWorker = worker.name;
  }

  editExistingWorker(){
    this.employees.forEach(employee => {
      if(employee.name == this.idOfEditingWorker){
        employee.name = this.selectedWorker;
      }
    })
    this.employee = new Employee();
    this.editing = false;
  }

  addMachine(){
    this.machine.name = this.selectedMachine;
    this.woMachines.push(this.machine);
    this.machine = new Machine();
  }

  editMachine(machine){
    this.machine.name = machine.name;
    this.editingMachine = true;
    this.idOfEditingMachine = machine.name;
  }

  editExistingMachine(){
    this.woMachines.forEach(machine => {
      if(machine.name == this.idOfEditingMachine){
        machine.name = this.selectedMachine;
      }
    })
    this.machine = new Machine();
    this.editingMachine = false;
  }

  addMaterial(){
    this.material.name = this.selectedMaterial;
    this.woMaterials.push(this.material);
    this.material = new Material();
  }

  editMaterial(material){
    this.material.name = material.name;
    this.editingMaterial = true;
    this.idOfEditingMaterial = material.name;
  }

  editExistingMaterial(){
    this.woMaterials.forEach(material => {
      if(material.name == this.idOfEditingMaterial){
        material.name = this.selectedMaterial;
      }
    })
    this.material = new Material();
    this.editingMaterial = false;
  }

  addWorkOrder(){
    
   
    this.wo.machines = this.woMachines;
    this.wo.workers = this.employees;
    this.wo.materials = this.woMaterials;

    /*
    this.workOrderService.addWorkOrder(this.wo).subscribe(data => {
      this.toastr.success("Uspešno kreiran radni nalog.");
    }, error => {
      this.toastr.error("Radni nalog nije kreiran.");
    })
    */
    console.log(this.wo)
  }

  updateWorkOrder(){
    var dateStartToAdd = "";
    var dateEndToAdd = "";
    if(this.wo.start != undefined) {
        if(this.wo.start.month < 10) {
          this.wo.start.month = '0' + this.wo.start.month;
        }
        if(this.wo.start.day < 10){
          this.wo.start.day = '0' + this.wo.start.day;
        }
        dateStartToAdd= this.wo.start.year + '-' 
          + this.wo.start.month + '-' 
          + this.wo.start.day;
    }
    if(this.wo.end != undefined) {
      if(this.wo.end.month < 10) {
        this.wo.end.month = '0' + this.wo.end.month;
      }
      if(this.wo.end.day < 10){
        this.wo.end.day = '0' + this.wo.end.day;
      }
      dateEndToAdd= this.wo.end.year + '-' 
        + this.wo.end.month + '-' 
        + this.wo.end.day;
    }
   
    this.wo.machines = this.woMachines;
    this.wo.workers = this.employees;
    this.wo.materials = this.woMaterials;

    /*
    this.workOrderService.updateWorkOrder(this.wo).subscribe(data => {
      this.toastr.success("Uspešno sačuvan radni nalog.");
    }, error => {
      this.toastr.error("Radni nalog nije sačuvan.");
    })
    */
    console.log(this.wo)
  }

  setForEdit(wor){
    this.workerMob = wor;
    console.log(wor)
  }

}
