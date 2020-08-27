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
  

  constructor(private router: Router) { }

  ngOnInit() {
    
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
