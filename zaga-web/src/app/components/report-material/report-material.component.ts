import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MaterialReport } from 'src/app/models/MaterialReport';
import { WorkOrder } from 'src/app/models/WorkOrder';
import { SpentMaterialService } from 'src/app/service/spent-material.service';
import { WorkOrderService } from 'src/app/service/work-order.service';

@Component({
  selector: 'app-report-material',
  templateUrl: './report-material.component.html',
  styleUrls: ['./report-material.component.css']
})
export class ReportMaterialComponent implements OnInit {

  constructor(
    private spentMaterialService:SpentMaterialService,
    private router: Router
  ) { }

  materials: MaterialReport[] = [];
  
  sumsQuantity: any[] = [];
  sumsSpent: any[] = [];

  ngOnInit() {
    this.spentMaterialService.getDataForReport().subscribe(data => {
      this.materials = data;
      console.log(this.materials)
      this.materials.forEach(material => {
        var date = "";
        material.workOrders.forEach(workOrder => {
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
      })
    })
  })
  }

  changeRoute(id) {
    this.router.navigateByUrl("/create/workOrder/" + id);
  }

}
