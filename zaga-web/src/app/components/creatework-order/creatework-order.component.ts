import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WorkOrdeMachine } from 'src/app/models/WorkOrderMachine';
import { Worker } from 'src/app/models/Worker';
import { Material } from 'src/app/models/Material';

@Component({
  selector: 'app-creatework-order',
  templateUrl: './creatework-order.component.html',
  styleUrls: ['./creatework-order.component.css']
})
export class CreateworkOrderComponent implements OnInit {
  
  constructor(private route: ActivatedRoute) { }

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

  tempJSON = [
   {
    "id": 1,
    "name": "Osnovno đubrenje",
    "start": "19.08.2020.",
    "end": "20.08.2020.",
    "field": "",
    "category": "Kategorija 1",
    "culture": "Kukuruz 2020",
    "responsible": "Nemanja Nemanjic",
    "status": "Status 1",
    "machines": [{
      "id": 1,
      "machine": "NH 456/1",
      "worker": "Miloš Milošević",
      "date": "20.08.2020.",
      "workPeriod": "6",
      "fuel": "10",
      "fuelType": "Gorivo 1",
      "storage": "Magacin 1"
    }],
    "workers": [{
        "id": 1,
        "worker": "Miloš Milošević",
        "date": "20.08.2020.",
        "workPeriod": "8",
        "treatedArea": "3"
    }],
    "materials": [{
        "id": 1,
        "name": "NPK 15:15:15",
        "quantity": "1050",
        "unit": "KG"      
    }]
   },
]
  
  ngOnInit() {
    if(this.workId == "new") { //new
      this.workOrder = JSON.parse(localStorage["workOrder"]);
      this.workOrder.workers = [];
      this.workOrder.machines = [];
      this.workOrder.materials = [];
    } else {
      const workOrders: any[] = JSON.parse(localStorage["workOrders"]);
      this.workOrder = workOrders.filter(order => order.id = this.workId)[0];
      console.log(this.workOrder)
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
    
    if(this.worker.date.month < 10) {
      this.worker.date.month = '0' + this.worker.date.month;
    }
      this.worker.date = this.worker.date.day + '.' 
      + this.worker.date.month + '.' 
      + this.worker.date.year + '.';
      
      this.worker.id = this.workOrder.workers.length + 1;
      this.workOrder.workers.push(this.worker);
      this.worker = new Worker();
      this.editing = false;
  } 
  
  editExistingWorker() {
    if(this.worker.date.month < 10) {
      this.worker.date.month = '0' + this.worker.date.month;
    }
    var dateToAdd = this.worker.date.day + '.' 
      + this.worker.date.month + '.' 
      + this.worker.date.year + '.';

    this.workOrder.workers.forEach(element => {
      if(element.id == this.idOfEditingWorker) {
        element.worker = this.worker.worker;
        element.workPeriod = this.worker.workPeriod;
        element.date = dateToAdd;
        element.treatedArea = this.worker.treatedArea;
      }
    });
    this.worker = new Worker();
    this.editing = false;
  }

  editWorker(worker) {
    this.worker.worker = worker.worker;
    this.worker.workPeriod = worker.workPeriod;
    this.worker.treatedArea = worker.treatedArea;
    console.log(worker.date);
    this.worker.date = {day: +worker.date.substring(0,2), 
                        month: +worker.date.substring(3,5), 
                        year: +worker.date.substring(6,10)
                      };
    this.editing = true;
    this.idOfEditingWorker = worker.id;
  }

  addMachine() {
    if(this.machine.date.month < 10) {
      this.machine.date.month = '0' + this.machine.date.month;
    }
    this.machine.date = this.machine.date.day + '.' 
    + this.machine.date.month + '.' 
    + this.machine.date.year + '.';
    this.machine.id = this.workOrder.machines.length + 1;
    this.workOrder.machines.push(this.machine);
    this.machine = new WorkOrdeMachine();
    this.editingMachine = false;
  }

  editMachine(machine) {
    this.machine.worker = machine.worker;
    this.machine.machine = machine.machine;
    this.machine.date = {day: +machine.date.substring(0,2), 
      month: +machine.date.substring(3,5), 
      year: +machine.date.substring(6,10)
    };
    this.machine.fuel = machine.fuel;
    this.machine.fuelType = machine.fuelType;
    this.machine.storage = machine.storage;
    this.machine.workPeriod = machine.workPeriod;
    this.editingMachine = true;
    this.idOfEditingMachine = machine.id;
  }

  updateExistingMachine() {
    if(this.machine.date.month < 10) {
      this.machine.date.month = '0' + this.machine.date.month;
    }
    var dateToAdd = this.machine.date.day + '.' 
      + this.machine.date.month + '.' 
      + this.machine.date.year + '.';

 
    this.workOrder.machines.forEach(element => {
      if(element.id == this.idOfEditingMachine) {
        element.worker = this.machine.worker;
        element.workPeriod = this.machine.workPeriod;
        element.date = dateToAdd;
        element.machine = this.machine.machine;
        element.fuel = this.machine.fuel;
        element.fuelType = this.machine.fuelType;
        element.storage = this.machine.storage;
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
    this.editingMaterial = true;
    this.idOfEditingMaterial = material.id;
  }

  editExistingMaterial() {

    this.workOrder.materials.forEach(element => {
      if(element.id == this.idOfEditingMaterial) {
        element.name = this.material.name;
        element.unit = this.material.unit;
        element.quantity = this.material.quantity;
      }
    });
    this.material = new Material();
    this.editingMaterial = false;
  }



}
