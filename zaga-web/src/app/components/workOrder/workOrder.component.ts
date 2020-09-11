import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { WorkOrderService } from 'src/app/service/work-order.service';
import { WorkOrder } from 'src/app/models/WorkOrder';

@Component({
  selector: 'app-workOrder',
  templateUrl: './workOrder.component.html',
  styleUrls: ['./workOrder.component.css']
})
export class WorkOrderComponent implements OnInit {

  click = false;
  collapseBool = true;
  workOrders: WorkOrder[] = [];
  

  constructor(private router: Router, private workOrderService:WorkOrderService) { }

  ngOnInit() {
    //this.workOrders = JSON.parse(localStorage["workOrders"]);
    this.workOrderService.getAll().subscribe(data => {
      this.workOrders = data;
    })
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
