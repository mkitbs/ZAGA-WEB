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

  page = 1;
  pageSize = 3;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.spentMaterialService.getDataForReport().subscribe(data => {
      this.spinner.hide();
      this.loading = false;
      this.materials = data;
      console.log(this.materials)
      
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
       
          if(wo.quantity == 'Nije uneto'){
            wo.quantity = 0.0;
          }
          quantitySum += +wo.quantity;
        
      })
      return quantitySum.toFixed(2)
    } else {
      return null;
    }
   
  
  }

  getSpentSum(workOrders){
    let spentSum = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
        
          if(wo.spent == 'Nije uneto'){
            wo.spent = 0.0;
          }
          spentSum += +wo.spent;
       
      })
      return spentSum.toFixed(2)
    } else {
      return null;
    }
   
  }

  
  updateFilters(): void {
    this.filters = Object.assign({}, this.dates);
    console.log(this.filters)
  }

}
