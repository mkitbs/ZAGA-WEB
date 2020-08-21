import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-closework-order',
    templateUrl: './closework-order.component.html',
    styleUrls: ['./closework-order.component.css']
  })

export class CloseWorkOrderComponent implements OnInit{
    
    constructor(private route: ActivatedRoute) { }

    workId = this.route.snapshot.params.workId;
    workOrder;
    workers = false;
    machines = false;
    materials = false;
    tempJSON = [
        {
           "id":1,
           "name":"Prskanje",
           "start":"13.08.2020.",
           "end":"15.08.2020.",
           "field":"T46",
           "category": "Odrzavanje",
           "culture":"Kukuruz",
           "responsible":"Milos Vrgovic",
           "status":"Novi",
           "machines": [
             {
               "id":1,
               "machine":"Traktor",
               "worker":"Pera Peric",
               "date":"13.08.2020.",
               "workPeriod":"8",
               "fuel":"10",
               "fuelType":"disel",
               "storage":"Magacin 1"
             }
           ],
           "workers": [
             {
               "id":1,
               "worker":"Pera Peric",
               "date":"13.08.2020.",
               "workPeriod":"6",
               "treatedArea":"20",
               "status": "Glavni radnik"
             },
             {
               "id":2,
               "worker":"Nikola Nikolic",
               "date":"13.08.2020.",
               "workPeriod":"2",
               "treatedArea":"10",
               "status": "Pomoćni radnik"
             }
           ],
           "materials": [
             {
               "id":1,
               "name":"MAP",
               "quantity":"1600",
               "unit":"KG"
             },
             {
                "id": 2,
                "name": "NPK 15:15:15",
                "quantity": "1050",
                "unit": "KG"  
             }
           ]
         },
         {
           "id": 2,
           "name": "Osnovno đubrenje",
           "start": "19.08.2020.",
           "end": "20.08.2020.",
           "field": "T4",
           "category": "Kategorija 1",
           "culture": "Kukuruz 2020",
           "responsible": "Nemanja Nemanjic",
           "status": "U radu",
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
               "treatedArea": "3",
               "status": "Glavni radnik"
           }],
           "materials": [{
               "id": 1,
               "name": "NPK 15:15:15",
               "quantity": "1050",
               "unit": "KG"      
           }]
          }
       ]

    
    ngOnInit(){
        localStorage["workOrders"] = JSON.stringify(this.tempJSON);
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