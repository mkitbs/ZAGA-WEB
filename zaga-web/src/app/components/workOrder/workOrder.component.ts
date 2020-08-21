import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-workOrder',
  templateUrl: './workOrder.component.html',
  styleUrls: ['./workOrder.component.css']
})
export class WorkOrderComponent implements OnInit {

  click = false;
  collapseBool = true;
  workOrders: any[];
  tempJSON = [
   {
      "id":1,
      "name":"Prskanje",
      "start":"13.08.2020.",
      "end":"13.08.2020.",
      "field":"T46",
      "fieldArea": "40",
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
          "workPeriod":"4",
          "fuel":"10",
          "fuelType":"disel",
          "storage":"Magacin 1"
        },
        {
          "id":2,
          "machine":"Bager",
          "worker":"Nikola Nikolic",
          "date":"13.08.2020.",
          "workPeriod":"2",
          "fuel":"20",
          "fuelType":"disel",
          "storage":"Magacin 1"
        }
      ],
      "workers": [
        {
          "id":1,
          "worker":"Pera Peric",
          "date":"13.08.2020.",
          "workPeriod":"4",
          "treatedArea":"20"
        },
        {
          "id":2,
          "worker":"Nikola Nikolic",
          "date":"13.08.2020.",
          "workPeriod":"2",
          "treatedArea":"10"
        }
      ],
      "materials": [
        {
          "id":1,
          "name":"XYZ",
          "quantity":"10",
          "unit":"l"
        }
      ]
    },
    {
      "id": 2,
      "name": "Osnovno đubrenje",
      "start": "19.08.2020.",
      "end": "20.08.2020.",
      "field": "T4",
      "fieldArea": "80",
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
          "treatedArea": "3"
      }],
      "materials": [{
          "id": 1,
          "name": "NPK 15:15:15",
          "quantity": "1050",
          "unit": "KG"      
      }]
     }
  ]



  constructor(private router: Router) { }

  ngOnInit() {
    localStorage["workOrders"] = JSON.stringify(this.tempJSON);
    this.workOrders = JSON.parse(localStorage["workOrders"]);
  }

  changeClick(){
    this.click = true;
  }

  editOrder(){
    alert("Izmeni nalog");
  }

  deleteOrder(){
    alert("Obriši nalog");
  }

  collapse() {
    this.collapseBool = !this.collapseBool;
  }

  changeRoute(id){
    this.router.navigateByUrl('/create/workOrder/'+id)
  }

}
