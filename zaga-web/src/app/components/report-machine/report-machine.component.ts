import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
    private router: Router
  ) { }

  machines: MachineReport[] = [];
  collapseBool = true;

  dates: any  = { dateFrom: "", dateTo: "" };
  filters: any = { namdateFrome: "", dateTo: "" };

  ngOnInit() {
    this.machineService.getDataForReport().subscribe(data =>{
      this.machines = data;
      this.machines.forEach(machine => {
        var date = "";
        machine.workOrders.forEach(workOrder => {
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

  changeRoute(id){
    this.router.navigateByUrl("/create/workOrder/" + id);
  }

  collapse() {
    this.collapseBool = !this.collapseBool;
  }

  getFinalMachineSum(workOrders){
    let machineSum = 0.0;
    workOrders.forEach(wo => {
      wo.workers.forEach(w => {
        if(w.sumState == -1){
          w.sumState = 0.0;
        }
        machineSum += w.sumState;
      })
    })
    return machineSum
  }

  getFuelSum(workOrders){
    let fuelSum = 0.0;
    workOrders.forEach(wo => {
      wo.workers.forEach(w => {
        if(w.fuel == -1){
          w.fuel = 0.0;
        }
        fuelSum += w.fuel;
      })
    })
    return fuelSum
  }

  updateFilters(): void {
    this.filters = Object.assign({}, this.dates);
    console.log(this.filters)
  }


}
