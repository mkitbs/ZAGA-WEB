import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { WorkOrderTractorDriver } from 'src/app/models/WorkOrderTractorDriver';
import { WorkOrderWorkerService } from 'src/app/service/work-order-worker.service';

@Component({
  selector: 'app-work-order-tractor-driver',
  templateUrl: './work-order-tractor-driver.component.html',
  styleUrls: ['./work-order-tractor-driver.component.css']
})
export class WorkOrderTractorDriverComponent implements OnInit {

  constructor(
    private wowService: WorkOrderWorkerService,
    private router: Router,
    private spinner: NgxSpinnerService
  ) { }

  workOrders: WorkOrderTractorDriver[] = [];
  empty;
  loading;

  ngOnInit() {
    this.getWorkOrders();
  }

  getWorkOrders(){
    this.spinner.show();
    this.loading = true;
    this.wowService.getWorkOrderForTractorDriver().subscribe(data => {
      this.spinner.hide();
      this.loading = false;
      this.workOrders = data;
      if(this.workOrders.length == 0){
        this.empty = true;
      } else if(this.workOrders.length == 1){
        this.workOrders.forEach(wo => {
          this.router.navigateByUrl("/timeTracking/" + wo.wowId);
        })
      } else {
        this.empty = false;
        this.workOrders.forEach(wo => {
          if(wo.wowStatus == "NOT_STARTED"){
            wo.wowStatus = "Nije započet"
          } else if(wo.wowStatus == "STARTED"){
            wo.wowStatus = "Započet"
          } else if(wo.wowStatus == "PAUSED"){
            wo.wowStatus = "Pauziran"
          } else if(wo.wowStatus == "FINISHED"){
            wo.wowStatus = "Završen"
          }
        })
      }
      console.log(this.workOrders)
    }, error => {
      this.spinner.hide();
      this.loading = false;
    })
  }

  changeRoute(id){
    this.router.navigateByUrl("/timeTracking/" + id)
  }

}
