import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { WorkOrderService } from 'src/app/service/work-order.service';
import { WorkOrder } from 'src/app/models/WorkOrder';
import { Operation } from 'src/app/models/Operation';
import { Crop } from 'src/app/models/Crop';
import { CropService } from 'src/app/service/crop.service';
import { OperationService } from 'src/app/service/operation.service';
import { UserService } from 'src/app/service/user.service';
import { Employee } from 'src/app/models/Employee';

@Component({
  selector: 'app-workOrder',
  templateUrl: './workOrder.component.html',
  styleUrls: ['./workOrder.component.css']
})
export class WorkOrderComponent implements OnInit {

  click = false;
  collapseBool = true;
  workOrders: WorkOrder[] = [];
  operation: Operation;
  operations: Operation[] = [];
  crop: Crop;
  employee: Employee;
  operationId;
  cropId;

  constructor(private router: Router, private workOrderService:WorkOrderService) { }

  ngOnInit() {
    this.workOrderService.getAll().subscribe(data => {
      this.workOrders = data;
      this.workOrders.forEach(workOrder => {
        if(workOrder.status == "NEW"){
          workOrder.status = "Novi";
        } else if(workOrder.status == "IN_PROGRESS"){
          workOrder.status = "U radu";
        } else if(workOrder.status == "CLOSED"){
          workOrder.status = "Zatvoren";
        }
      })
    })
   
  }

  changeClick(){
    this.click = true;
  }

  collapse() {
    this.collapseBool = !this.collapseBool;
  }

  changeRoute(id){
    this.router.navigateByUrl('/create/workOrder/'+id)
  }

}
