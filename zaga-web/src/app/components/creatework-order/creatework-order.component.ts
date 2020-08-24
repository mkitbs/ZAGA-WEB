import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WorkOrdeMachine } from 'src/app/models/WorkOrderMachine';
import { Worker } from 'src/app/models/Worker';
import { Material } from 'src/app/models/Material';
import { WorkOrderInfo } from 'src/app/models/WorkOrderInfo';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-creatework-order',
  templateUrl: './creatework-order.component.html',
  styleUrls: ['./creatework-order.component.css']
})
export class CreateworkOrderComponent implements OnInit {
  
  constructor(private route: ActivatedRoute, private toastr: ToastrService) { }

  workers = false;
  machines = false;
  materials = false;
  editing = false;
  editingMachine = false;
  editingMaterial = false;
  workId = this.route.snapshot.params.workId;
  workOrder;
  machine : WorkOrdeMachine = new WorkOrdeMachine();
  workOrderMachine : WorkOrdeMachine = new WorkOrdeMachine();
  worker : Worker = new Worker();
  material : Material = new Material();
  idOfEditingMaterial:any = 0;
  idOfEditingWorker:any = 0;
  idOfEditingMachine:any = 0;
  new = false;
  workOrders: any[];
  
  ngOnInit() {
    if(this.workId == "new") { //new
      this.new = true;
      this.workOrder = new WorkOrderInfo();
      this.workOrder.machines = [];
      this.workOrder.workers = [];
      this.workOrder.materials = [];
    } else {
      this.new = false;
      this.workOrders = JSON.parse(localStorage["workOrders"]);
      this.workOrder = this.workOrders[this.workId-1];
      this.workOrder.start = { day: +this.workOrder.start.substring(0,2), 
                               month: +this.workOrder.start.substring(3,5), 
                               year: +this.workOrder.start.substring(6,10)
                             };
      this.workOrder.end = { day: +this.workOrder.end.substring(0,2), 
                             month: +this.workOrder.end.substring(3,5), 
                             year: +this.workOrder.end.substring(6,10)
                            };
    }
  }

  expandWorkers() {
    this.workers = !this.workers;
  }
  expandMachines() {
    this.machines = !this.machines;
  }
  expandMaterials() {
    this.materials = !this.materials;
  }

  addWorker() {
      if(this.worker.date != undefined) {
        if(this.worker.date.month < 10) {
          this.worker.date.month = '0' + this.worker.date.month;
        }
          this.worker.date = this.worker.date.day + '.' 
          + this.worker.date.month + '.' 
          + this.worker.date.year + '.';
      }  
      this.worker.id = this.workOrder.workers.length + 1;
      this.worker.workPeriod = this.worker.dayWorkPeriod + this.worker.nightWorkPeriod;
      this.workOrder.workers.push(this.worker);
      this.worker = new Worker();
      this.editing = false;
  } 
  
  editExistingWorker() {
    var dateToAdd = "";
    if(this.worker.date != undefined) {
        if(this.worker.date.month < 10) {
          this.worker.date.month = '0' + this.worker.date.month;
        }
        dateToAdd= this.worker.date.day + '.' 
          + this.worker.date.month + '.' 
          + this.worker.date.year + '.';
    }
    this.workOrder.workers.forEach(element => {
      if(element.id == this.idOfEditingWorker) {
        element.worker = this.worker.worker;
        element.dayWorkPeriod = this.worker.dayWorkPeriod;
        element.nightWorkPeriod = this.worker.nightWorkPeriod;
        element.date = dateToAdd;
        element.operation = this.worker.operation;

      }
    });
    this.worker = new Worker();
    this.editing = false;
  }

  editWorker(worker) {
    this.worker.worker = worker.worker;
    this.worker.workPeriod = worker.workPeriod;
    this.worker.operation = worker.operation;
    this.worker.nightWorkPeriod = worker.nightWorkPeriod;
    this.worker.dayWorkPeriod = worker.dayWorkPeriod;
    this.worker.workPeriod = worker.nightWorkPeriod + worker.dayWorkPeriod;
    if(worker.date != undefined) {
        this.worker.date = {day: +worker.date.substring(0,2), 
                            month: +worker.date.substring(3,5), 
                            year: +worker.date.substring(6,10)
                          };
    }
    this.editing = true;
    this.idOfEditingWorker = worker.id;
  }

  addMachine() {
    
    this.machine.id = this.workOrder.machines.length + 1;
    this.machine.sumState = this.machine.finalState - this.machine.initialState;
    this.workOrder.machines.push(this.machine);
    this.machine = new WorkOrdeMachine();
    this.editingMachine = false;
  }

  editMachine(machine) {
    this.machine.worker = machine.worker;
    this.machine.machine = machine.machine;
    this.machine.initialState = machine.initialState;
    this.machine.finalState = machine.finalState;
    this.machine.sumState = machine.finalState - machine.initialState;
    this.machine.fuel = machine.fuel;
    this.machine.workPeriod = machine.workPeriod;
    this.editingMachine = true;
    this.idOfEditingMachine = machine.id;
  }

  updateExistingMachine() {
 
    this.workOrder.machines.forEach(element => {
      if(element.id == this.idOfEditingMachine) {
        element.worker = this.machine.worker;
        element.workPeriod = this.machine.workPeriod;
        element.machine = this.machine.machine;
        element.fuel = this.machine.fuel;
        element.finalState = this.machine.finalState;
        element.initialState = this.machine.initialState;
        element.sumState = this.machine.sumState;
      }
    });
    this.machine = new WorkOrdeMachine();
    this.editingMachine= false;
  }

  addMaterial() {
    this.material.id = this.workOrder.materials.length + 1;
    this.workOrder.materials.push(this.material);
    this.editingMaterial = false;
    this.material = new Material();
  }

  editMaterial(material) {
    this.material.name = material.name;
    this.material.quantity = material.quantity;
    this.material.unit = material.unit;
    this.material.spent = material.spent;
    this.material.spentPerHectar = material.spentPerHectar;
    this.material.quantityPerHectar = material.quantityPerHectar;
    this.editingMaterial = true;
    this.idOfEditingMaterial = material.id;
  }

  editExistingMaterial() {

    this.workOrder.materials.forEach(element => {
      if(element.id == this.idOfEditingMaterial) {
        element.name = this.material.name;
        element.unit = this.material.unit;
        element.quantity = this.material.quantity;
        element.quantityPerHectar = this.material.quantityPerHectar;
        element.spent = this.material.spent;
        element.spentPerHectar = this.material.spentPerHectar;
      }
    });
    this.material = new Material();
    this.editingMaterial = false;
  }

  saveData() {
    localStorage.clear();
    var dateStartToAdd = "";
    var dateEndToAdd = "";
    if(this.workOrder.start != undefined) {
        if(this.workOrder.start.month < 10) {
          this.workOrder.start.month = '0' + this.workOrder.start.month;
        }
        dateStartToAdd= this.workOrder.start.day + '.' 
          + this.workOrder.start.month + '.' 
          + this.workOrder.start.year + '.';
    }
    if(this.workOrder.end != undefined) {
      if(this.workOrder.end.month < 10) {
        this.workOrder.end.month = '0' + this.workOrder.end.month;
      }
      dateEndToAdd= this.workOrder.end.day + '.' 
        + this.workOrder.end.month + '.' 
        + this.workOrder.end.year + '.';
    }
    this.workOrder.start = dateStartToAdd;
    this.workOrder.end = dateEndToAdd;
    localStorage["workOrders"] = JSON.stringify(this.workOrders);
    this.toastr.success("Uspešno sačuvan radni nalog.")
  }

  closeWorkOrder() {
    
    //logic for validation here
    if(this.workOrder.treated != ""){
      this.workOrder.status = "Zatvoren";
      localStorage.clear();
      localStorage["workOrders"] = JSON.stringify(this.workOrders);
      this.toastr.success("Uspešno zatvoren radni nalog.");
    } else{
      this.toastr.error("Neuspešno zatvoren radni nalog. Popunite sva polja.")
    }
  }



}
