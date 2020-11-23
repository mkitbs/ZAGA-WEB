import { Component, OnInit } from '@angular/core';
import { WorkOrder } from 'src/app/models/WorkOrder';
import { WorkOrderService } from 'src/app/service/work-order.service';

@Component({
  selector: 'app-report-material',
  templateUrl: './report-material.component.html',
  styleUrls: ['./report-material.component.css']
})
export class ReportMaterialComponent implements OnInit {

  constructor(
    private workOrderService:WorkOrderService
  ) { }

  workOrders: WorkOrder[] = [];
  workOrdersWithMaterials: WorkOrder[] = [];

  empty;
  desc = false;

  ngOnInit() {
    this.workOrderService.getAll().subscribe((data) => {
      this.workOrders = data;
      console.log(this.workOrders);
      if (this.workOrders.length == 0) {
        this.empty = true;
      } else {
        this.empty = false;
        this.workOrders.forEach((workOrder) => {
          if(workOrder.materials.length != 0){
            var date = "";
            date =
              workOrder.date.day.split(" ")[0] +
              "." +
              workOrder.date.month +
              "." +
              workOrder.date.year +
              ".";
            workOrder.date = date;
            if (workOrder.status == "NEW") {
              workOrder.status = "Novi";
            } else if (workOrder.status == "IN_PROGRESS") {
              workOrder.status = "U radu";
            } else if (workOrder.status == "CLOSED") {
              workOrder.status = "Zatvoren";
            }
            if(workOrder.sapId == 0){
              workOrder.sapId = null;
            }
            this.workOrdersWithMaterials.push(workOrder);
            console.log(this.workOrdersWithMaterials)
          }
          
        });
      }
      this.workOrders.sort((w1, w2) => w2.sapId - w1.sapId);
    });
  }

  sortBySapId() {
    this.desc = !this.desc;
    if(this.desc) {
      this.workOrders.sort((w1, w2) => w1.sapId - w2.sapId);
    } else {
      this.workOrders.sort((w1, w2) => w2.sapId - w1.sapId);
    }

  }

}
