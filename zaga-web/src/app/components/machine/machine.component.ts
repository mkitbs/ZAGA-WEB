import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { Machine } from 'src/app/models/Machine';
import { MachineGroup } from 'src/app/models/MachineGroup';
import { MachineGroupService } from 'src/app/service/machine-group.service';
import { MachineService } from 'src/app/service/machine.service';

@Component({
  selector: 'app-machine',
  templateUrl: './machine.component.html',
  styleUrls: ['./machine.component.css']
})
export class MachineComponent implements OnInit {

  constructor(
    private machineService:MachineService, 
    private machineGroupSerivce:MachineGroupService,
    private spinner: NgxSpinnerService,
  ) { }

  machines : Machine[] = [];
  machineGroups : MachineGroup[] = [];
  machine : Machine = new Machine();
  machinesByType : Machine[] = [];

  machineFC: FormControl = new FormControl("");
  machineTypeFC: FormControl = new FormControl("");
  machineGroupFC: FormControl = new FormControl("");

  loading;

  page = 1;
  pageSize = 12;

  ngOnInit() {
   this.getAll();
   this.getAllGroupByType();
  }

  getAllGroupByType(){
    this.machineService.getAllGroupByType().subscribe(data => {
      this.machinesByType = data;
      this.machinesByType.forEach(machine => {
        if(machine.Type == "PROPULSION"){
          machine.Type = "POGONSKA"
        } else if(machine.Type == "COUPLING"){
          machine.Type = "PRIKLJUČNA"
        }
      })
    })
  }

  getAll(){
    this.spinner.show();
    this.loading = true;
    this.machineGroupSerivce.getAll().subscribe(data => {
      //data = this.convertKeysToLowerCase(data);
      this.machineGroups = data;
      console.log(this.machineGroups)
      this.machineService.getAll().subscribe(data => {
        //data = this.convertKeysToLowerCase(data);
        this.spinner.hide();
        this.loading = false;
        this.machines = data;
        console.log(this.machines)
        this.machines.forEach(machine => {
          if(machine.Type == "PROPULSION"){
            machine.Type = "POGONSKA"
          } else if(machine.Type == "COUPLING"){
            machine.Type = "PRIKLJUČNA"
          }
          if(machine.FuelType == "NOT_SELECTED"){
            machine.FuelType = "NIJE IZABRANO"
          } else if(machine.FuelType == "GASOLINE"){
            machine.FuelType = "BENZIN"
          } else if(machine.FuelType == "GAS"){
            machine.FuelType = "GAS"
          } else if(machine.FuelType == "EURO_DIESEL"){
            machine.FuelType = "EVRO DIZEL"
          } else if(machine.FuelType == "BIO_DIESEL"){
            machine.FuelType = "BIO DIZEL"
          } else if(machine.FuelType == "DIESEL"){
            machine.FuelType = "DIZEL"
          }
          machine.machineGroupName = this.machineGroups.find(machineGroup => machineGroup.dbId == machine.machineGroup).Name
        })
      }, error =>{
        this.spinner.hide();
      })
    }, error =>{
      this.spinner.hide();
    })
  }

  getMachine(id){
    this.machine = this.machines.find((x) => x.dbid == id);
  }

  editMachine(){
    console.log(this.machine)
    this.machineService.editMachine(this.machine).subscribe(res => {
      console.log(res);
      this.getAll();
    })
  }

  displayFnMachineType(machine: Machine): string {
    return machine && machine.Type;
  }

  displayFnMachineGroup(machine: MachineGroup): string {
    return machine && machine.Name;
  }

  displayFnMachine(machine: Machine): string {
    return machine && machine.Id + " - " + machine.Name
  }

}
