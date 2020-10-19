import { Component, OnInit } from '@angular/core';
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

  constructor(private machineService:MachineService, private machineGroupSerivce:MachineGroupService) { }

  machines : Machine[] = [];
  machineGroups : MachineGroup[] = [];
  machine : Machine = new Machine();

  selectedMachineType;

  ngOnInit() {
   this.getAll();
  }

  getAll(){
    this.machineGroupSerivce.getAll().subscribe(data => {
      //data = this.convertKeysToLowerCase(data);
      this.machineGroups = data;
      console.log(this.machineGroups)
      this.machineService.getAll().subscribe(data => {
        //data = this.convertKeysToLowerCase(data);
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
      })
    })
  }

  getMachine(id){
    this.machine = this.machines.find((x) => x.id == id);
  }

  editMachine(){
    console.log(this.machine)
    this.machineService.editMachine(this.machine).subscribe(res => {
      console.log(res);
      this.getAll();
    })
  }

  getMachinePerType(){
    if(this.selectedMachineType == "PROPULSION"){
      this.machineService.getAllPropulsion().subscribe(data => {
        this.machines = data;
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
      })
    } else if(this.selectedMachineType == "COUPLING"){
      this.machineService.getAllCoupling().subscribe(data => {
        this.machines = data;
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
      })
    }
  }

  setValueForSelectedMachineType(){
    this.selectedMachineType = -1;
    this.getAll();
    console.log(this.selectedMachineType)
  }


  //method for convert json property names to lower case
  convertKeysToLowerCase(obj) {
    var output = [];
    for (let i in obj) {
      if (Object.prototype.toString.apply(obj[i]) === "[object Object]") {
        output[i.toLowerCase()] = this.convertKeysToLowerCase(obj[i]);
      } else if (Object.prototype.toString.apply(obj[i]) === "[object Array]") {
        output[i.toLowerCase()] = [];
        output[i.toLowerCase()].push(this.convertKeysToLowerCase(obj[i][0]));
      } else {
        output[i.toLowerCase()] = obj[i];
      }
    }
    return output;
  }

}
