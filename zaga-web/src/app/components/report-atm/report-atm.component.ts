import { Route } from '@angular/compiler/src/core';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ATMReportResponse } from 'src/app/models/ATMReportResponse';
import { WorkOrderService } from 'src/app/service/work-order.service';

@Component({
  selector: 'app-report-atm',
  templateUrl: './report-atm.component.html',
  styleUrls: ['./report-atm.component.css']
})
export class ReportAtmComponent implements OnInit {

  collapseBool = true;
  atmReport: ATMReportResponse[] = [];
  dates: any  = { dateFrom: "", dateTo: "" };
  filters: any = { namdateFrome: "", dateTo: "" };

  empty;
  loading;

  constructor(
    private workOrderService: WorkOrderService,
    private spinner: NgxSpinnerService,
    private router: Router
  ) { }

  ngOnInit() {
    this.spinner.show();
    this.workOrderService.getDataForATMReport().subscribe(data => {
      this.atmReport = data;
      console.log(this.atmReport)
      this.spinner.hide();
    }, error => {
      this.spinner.hide();
    })
  }

  collapse() {
    this.collapseBool = !this.collapseBool;
  }

  
  updateFilters(): void {
    this.filters = Object.assign({}, this.dates);
  }

  getAreaSum(workOrders){
    let area = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
          area += wo.area;
        })
      return area.toFixed(2) + " ha";
    } else {
      return null;
    }
  }

  getTreatedSum(workOrders){
    let treated = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
          treated += wo.treated;
        })
      return treated.toFixed(2) + " ha";
    } else {
      return null;
    }
  }

  goToWorkOrder(id) {
    this.router.navigateByUrl("/create/workOrder/" + id);
  }

}
