import { convertActionBinding } from '@angular/compiler/src/compiler_util/expression_converter';
import { ViewChild } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { WorkerReport } from 'src/app/models/WorkerReport';
import { WorkOrder } from 'src/app/models/WorkOrder';
import { WorkOrderWorkerService } from 'src/app/service/work-order-worker.service';

@Component({
  selector: 'app-report-employee',
  templateUrl: './report-employee.component.html',
  styleUrls: ['./report-employee.component.css']
})
export class ReportEmployeeComponent implements OnInit {

  constructor(
    private wowService: WorkOrderWorkerService,
    private router: Router,
    private spinner: NgxSpinnerService
  ) { }

  workers: WorkerReport[] = [];
  collapseBool = true;

  dates: any  = { dateFrom: "", dateTo: "" };
  filters: any = { namdateFrome: "", dateTo: "" };

  empty;
  loading;
 
  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.wowService.getDataForReport().subscribe(data => {
      console.log(data)
      this.spinner.hide();
      this.loading = false;
      this.workers = data;
      this.workers.forEach(worker => {
        var date = "";
        worker.workOrders.forEach(workOrder => {
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

  changeRoute(id){
    this.router.navigateByUrl("/create/workOrder/" + id)
  }

  collapse() {
    this.collapseBool = !this.collapseBool;
  }

  getDayPeriodSum(workOrders){
    let dayPeriodSum = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
        wo.workers.forEach(w => {
          if(w.dayPeriod == -1){
            w.dayPeriod = 0.0;
          }
          dayPeriodSum += w.dayPeriod;
        })
      })
      return dayPeriodSum + " h"
    } else {
      return null;
    }
    
  }

  getNightPeriodSum(workOrders){
    let nightPeriodSum = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
        wo.workers.forEach(w => {
          if(w.nightPeriod == -1){
            w.nightPeriod = 0.0;
          }
          nightPeriodSum += w.nightPeriod;
        })
      })
      return nightPeriodSum + " h";
    } else {
      return null;
    }
    
  }

  getWorkPeriodSum(workOrders){
    let workPeriodSum = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
        wo.workers.forEach(w => {
          if(w.nightPeriod == -1){
            w.nightPeriod = 0.0;
          }
          if(w.dayPeriod == -1){
            w.dayPeriod = 0.0;
          }
          workPeriodSum += w.nightPeriod + w.dayPeriod;
        })
      })
      return workPeriodSum + " h";
    } else {
      return null;
    }
    
  }

  updateFilters(): void {
    this.filters = Object.assign({}, this.dates);
  }

}
