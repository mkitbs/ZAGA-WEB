import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
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
    private router: Router,
    private spinner: NgxSpinnerService
  ) { }

  materials: MaterialReport[] = [];
  collapseBool = true;

  dates: any  = { dateFrom: "", dateTo: "" };
  filters: any = { namdateFrome: "", dateTo: "" };

  loading;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.spentMaterialService.getDataForReport().subscribe(data => {
      this.spinner.hide();
      this.loading = false;
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
          } else if (workOrder.status == "CANCELLATION"){
            workOrder.status = "Storniran";
          }
        })
      })
    }, error => {
      this.spinner.hide();
      this.loading = false;
    })
  }

  changeRoute(id) {
    this.router.navigateByUrl("/create/workOrder/" + id);
  }

  collapse() {
    this.collapseBool = !this.collapseBool;
  }

  getQuantitySum(workOrders){
    let quantitySum = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
        wo.materials.forEach(m => {
          if(m.quantity == -1){
            m.quantity = 0.0;
          }
          quantitySum += m.quantity;
        })
      })
      return quantitySum
    } else {
      return null;
    }
   
  
  }

  getSpentSum(workOrders){
    let spentSum = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
        wo.materials.forEach(m => {
          if(m.spent == -1){
            m.spent = 0.0;
          }
          spentSum += m.spent;
        })
      })
      return spentSum
    } else {
      return null;
    }
   
  }

  
  updateFilters(): void {
    this.filters = Object.assign({}, this.dates);
    console.log(this.filters)
  }

}
