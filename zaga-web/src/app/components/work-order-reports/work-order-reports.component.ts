import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { SpentMaterialService } from 'src/app/service/spent-material.service';
import { WorkOrderWorkerService } from 'src/app/service/work-order-worker.service';

@Component({
  selector: 'app-work-order-reports',
  templateUrl: './work-order-reports.component.html',
  styleUrls: ['./work-order-reports.component.css']
})
export class WorkOrderReportsComponent implements OnInit {

  constructor(
    private spentMaterialService: SpentMaterialService,
    private workOrderWorkerService: WorkOrderWorkerService,
    private toastr: ToastrService
  ) { }

  spentMaterialsPerCulture: any[] = [];
  spentMaterialPerCultureEmpty;
  spentWorkerHourPerCulture: any[] = [];
  spentWorkerHourPerCultureEmpty;
  machineStatePerCulture: any[] = [];
  machineStatePerCultureEmpty;
  machineSumState: any [] = [];
  machineSumStateEmpty;
  machineFuelPerCulture: any[] = [];
  machineFuelPerCultureEmpty;
  
  spentMatBool = false;
  spentWorkHoursBool = false;
  spentMachineWorkHoursBool = false;
  spentMachineFuelBool = false;

  page = 1;
	pageSize = 5;

  
  ngOnInit() {
    this.spentMaterialService.getSpentMaterialPerCulture().subscribe(data => {
      this.spentMaterialsPerCulture = data;
      if(this.spentMaterialsPerCulture.length == 0){
        this.spentMaterialPerCultureEmpty = true;
      } else {
        this.spentMaterialPerCultureEmpty = false;
      }
    })
    this.workOrderWorkerService.getHourOfWorkerPerCulture().subscribe(data => {
      this.spentWorkerHourPerCulture = data;
      if(this.spentWorkerHourPerCulture.length == 0){
        this.spentWorkerHourPerCultureEmpty = true;
      } else {
        this.spentWorkerHourPerCultureEmpty = false;
      }
    })
    this.workOrderWorkerService.getMachineSumStatePerCulture().subscribe(data => {
      this.machineStatePerCulture = data;
      if(this.machineStatePerCulture.length == 0){
        this.machineStatePerCultureEmpty = true;
      } else {
        this.machineStatePerCultureEmpty = false;
      }
    })
    this.workOrderWorkerService.getMachineSumState().subscribe(data => {
      this.machineSumState = data;
      if(this.machineSumState.length == 0){
        this.machineSumStateEmpty = true;
      } else {
        this.machineSumStateEmpty = false;
      }
    })
    this.workOrderWorkerService.getMachineSumFuelPerCulture().subscribe(data => {
      this.machineFuelPerCulture = data;
      if(this.machineFuelPerCulture.length == 0){
        this.machineFuelPerCultureEmpty = true;
      } else {
        this.machineFuelPerCultureEmpty = false;
      }
    })
  }

  spentMat(){
    this.spentMatBool = !this.spentMatBool;
    this.spentWorkHoursBool = false;
    this.spentMachineWorkHoursBool = false;
    this.spentMachineFuelBool = false;
    if(this.spentMaterialPerCultureEmpty){
      this.toastr.info("Nema podataka za prikazivanje.", "Obaveštenje", {
        positionClass: 'toast-center-center'
      })
      this.spentMatBool = false;
    }
  }

  spentWorkHours(){
    this.spentWorkHoursBool = !this.spentWorkHoursBool;
    this.spentMatBool = false;
    this.spentMachineWorkHoursBool = false;
    this.spentMachineFuelBool = false;
    if(this.spentWorkerHourPerCultureEmpty){
      this.toastr.info("Nema podataka za prikazivanje.", "Obaveštenje", {
        positionClass: 'toast-center-center'
      })
      this.spentWorkHoursBool = false;
    }
  }

  spentMachineWorkHours(){
    this.spentMachineWorkHoursBool = !this.spentMachineWorkHoursBool;
    this.spentMatBool = false;
    this.spentWorkHoursBool = false;
    this.spentMachineFuelBool = false;
    if(this.machineStatePerCultureEmpty){
      this.toastr.info("Nema podataka za prikazivanje.", "Obaveštenje", {
        positionClass: 'toast-center-center'
      })
      this.spentMachineWorkHoursBool = false;
    }
  }

  spentMachineFuel(){
    this.spentMachineFuelBool = !this.spentMachineFuelBool;
    this.spentMatBool = false;
    this.spentWorkHoursBool = false;
    this.spentMachineWorkHoursBool = false;
    if(this.machineFuelPerCultureEmpty){
      this.toastr.info("Nema podataka za prikazivanje.", "Obaveštenje", {
        positionClass: 'toast-center-center'
      })
      this.spentMachineFuelBool = false;
    }
  }

  getMachineSumPerField(machines){
    let sum = 0.0;
    machines.forEach(machine => {
      sum += machine.state;
    });
    return sum + " MČ"
  }

  getSumWorkHourPerField(workers){
    let sum = 0.0;
    workers.forEach(worker => {
      sum += worker.hour;
    });
    return sum + " h";
  }

  getMachineFuelPerField(machines){
    let sum = 0.0;
    machines.forEach(machine => {
      sum += machine.fuel;
    });
    return sum + " l"
  }

}
