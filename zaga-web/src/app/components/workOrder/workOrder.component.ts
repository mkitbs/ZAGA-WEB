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
      "table":"T46",
      "area": "40",
      "year": "2020",
      "culture":"Kukuruz",
      "responsible":"Milos Vrgovic",
      "status":"Novi",
      "treated": "",
      "machines": [
        {
          "id":1,
          "machine":"Traktor",
          "initialState": "10",
          "finalState": "10",
          "sumState": "10",
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
          "dayWorkPeriod": "10",
          "nightWorkPeriod": "0",
          "operation": "Operacija 1",
          "workPeriod":"4",
        },
        {
          "id":2,
          "worker":"Nikola Nikolic",
          "date":"13.08.2020.",
          "dayWorkPeriod": "10",
          "nightWorkPeriod": "0",
          "operation": "Operacija 1",
          "workPeriod":"4",
        }
      ],
      "materials": [
        {
          "id":1,
          "name":"XYZ",
          "quantity":"10",
          "spent": "10",
          "spentPerHectar": "10",
          "quantityPerHectar": "100",
          "unit":"l"
        }
      ]
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
