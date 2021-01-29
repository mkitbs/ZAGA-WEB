import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/models/User';
import { WorkOrderTractorDriver } from 'src/app/models/WorkOrderTractorDriver';
import { AuthService } from 'src/app/service/auth/auth.service';
import { UserService } from 'src/app/service/user.service';
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
    private spinner: NgxSpinnerService,
    private toastr: ToastrService,
    private authService: AuthService,
    private userService: UserService
  ) { }

  workOrders: WorkOrderTractorDriver[] = [];
  empty;
  loading;
  inProgress;
  woInProgress: WorkOrderTractorDriver = new WorkOrderTractorDriver();
  logged;

  ngOnInit() {
    this.getWorkOrders();
    this.getTractorDriver();
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
          } else if (wo.wowStatus == "CANCELLATION"){
            wo.wowStatus = "Storniran"
          }
        })
      }
      if(!this.empty){
        let index = this.workOrders.findIndex(x => x.inProgress === true);
        if(index != -1){
          this.inProgress = true;
          this.woInProgress = this.workOrders[index];
          console.log(this.woInProgress)
        } else {
          this.inProgress = false;
        }
      }
      console.log(this.workOrders)
      console.log(this.inProgress)
    }, error => {
      this.spinner.hide();
      this.loading = false;
    })
  }

  changeRoute(wo){
    if((this.inProgress && wo.inProgress) || !this.inProgress){
      this.router.navigateByUrl("/timeTracking/" + wo.wowId)
    } else {
      this.toastr.info("Ne možete započeti novi radni nalog dok ne završite započeti.")
    }
    
  }

  getTractorDriver(){
    this.authService.getLogged().subscribe(data => {
      var id = data.sapUserId;
      this.userService.getUserByPerNumber(id).subscribe(data => {
        this.logged = data.Name;
        console.log(this.logged)
      })
    })
  }

}
