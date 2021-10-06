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

  page = 1;
  pageSize = 1;
 
  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.wowService.getDataForReport().subscribe(data => {
      console.log(data)
      this.spinner.hide();
      this.loading = false;
      this.workers = data;
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
        
          if(wo.dayWork == "Nije uneto"){
            wo.dayWork = 0.0;
          }
          dayPeriodSum += +wo.dayWork;
        
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
      
          if(wo.nightWork == "Nije uneto"){
            wo.nightWork = 0.0;
          }
          nightPeriodSum += +wo.nightWork;
       
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
       
          if(wo.nightWork == "Nije uneto"){
            wo.nightWork = 0.0;
          }
          if(wo.dayWork == "Nije uneto"){
            wo.dayWork = 0.0;
          }
          workPeriodSum += +wo.nightWork + +wo.dayWork;
        
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
