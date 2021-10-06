import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { MachineReport } from 'src/app/models/MachineReport';
import { MachineService } from 'src/app/service/machine.service';

@Component({
  selector: 'app-report-machine',
  templateUrl: './report-machine.component.html',
  styleUrls: ['./report-machine.component.css']
})
export class ReportMachineComponent implements OnInit {

  constructor(
    private machineService: MachineService,
    private router: Router,
    private spinner: NgxSpinnerService
  ) { }

  machines: MachineReport[] = [];
  collapseBool = true;

  dates: any  = { dateFrom: "", dateTo: "" };
  filters: any = { namdateFrome: "", dateTo: "" };

  loading;

  page = 1;
  pageSize = 1;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.machineService.getDataForReport().subscribe(data =>{
      this.spinner.hide();
      this.loading = false;
      this.machines = data;
      console.log(this.machines)
    }, error => {
      this.spinner.hide();
      this.loading = false;
    })
  }

  changeRoute(id){
    this.router.navigateByUrl("/create/workOrder/" + id);
  }

  collapse() {
    this.collapseBool = !this.collapseBool;
  }

  getFinalMachineSum(workOrders){
    let machineSum = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
        var state = wo.split(",")[1];
        if(state == 'Nije uneto'){
          state = 0.0;
        }
        machineSum += +state;
      })
      return machineSum + " MČ"
    } else {
      return null;
    }
   
  }

  getFuelSum(workOrders){
    let fuelSum = 0.0;
    if(workOrders != -1){
      workOrders.forEach(wo => {
        var fuel = wo.split(",")[3];
        if(fuel == "Nije uneto"){
          fuel = 0.0;
        }
        fuelSum += +fuel;
      })
      return fuelSum + " l"
    } else {
      return null;
    }
    
  }

  updateFilters(): void {
    this.filters = Object.assign({}, this.dates);
    console.log(this.filters)
  }


}
