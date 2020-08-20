import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { type } from 'os';

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
  workId = this.route.snapshot.params.workId;
  workOrder;

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
    //localStorage["workOrders"] = JSON.stringify(this.tempJSON);
    if(this.workId == "new") { //new

    } else {
      const workOrders: any[] = JSON.parse(localStorage["workOrders"]);
      this.workOrder = workOrders.filter(order => order.id = this.workId)[0];
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

}
