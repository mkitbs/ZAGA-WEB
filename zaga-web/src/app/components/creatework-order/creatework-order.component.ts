import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-creatework-order',
  templateUrl: './creatework-order.component.html',
  styleUrls: ['./creatework-order.component.css']
})
export class CreateworkOrderComponent implements OnInit {
  
  constructor() { }
  workers = false;
  machines = false;
  materials = false;

  tempJSON = [
   {
    "id": 1,
    "name": "",
    "start": "",
    "end": "",
    "field": "",
    "culture": "",
    "responsible": "",
    "status": "",
    "machines": [{
      "id": 1,
      "machine": "",
      "worker": "",
      "date": "",
      "workPeriod": "",
      "fuel": "",
      "fuelType": "",
      "storage": ""
    }],
    "workers": [{
        "id": 1,
        "worker": "",
        "date": "",
        "workPeriod": "",
        "treatedArea": ""
    }],
    "materials": [{
        "id": 1,
        "name": "",
        "quantity": "",
        "unit": ""      
    }]
   },
]
  
  ngOnInit() {
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

}
