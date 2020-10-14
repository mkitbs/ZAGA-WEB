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

  ngOnInit() {
    this.machineGroupSerivce.getAll().subscribe(data => {
      data = this.convertKeysToLowerCase(data);
      this.machineGroups = data;
      this.machineService.getAll().subscribe(data => {
        data = this.convertKeysToLowerCase(data);
        this.machines = data;
        this.machines.forEach(machine => {
          if(machine.type == "PROPULSION"){
            machine.type = "POGONSKA"
          } else if(machine.type == "COUPLING"){
            machine.type = "PRIKLJUČNA"
          }
          if(machine.fueltype == "NOT_SELECTED"){
            machine.fueltype = "NIJE IZABRANO"
          } else if(machine.fueltype == "GASOLINE"){
            machine.fueltype = "BENZIN"
          } else if(machine.fueltype == "GAS"){
            machine.fueltype = "GAS"
          } else if(machine.fueltype == "EURO_DIESEL"){
            machine.fueltype = "EVRO DIZEL"
          } else if(machine.fueltype == "BIO_DIESEL"){
            machine.fueltype = "BIO DIZEL"
          } else if(machine.fueltype == "DIESEL"){
            machine.fueltype = "DIZEL"
          }
          machine.machineGroupName = this.machineGroups.find(x => x.dbid == machine.machinegroup).name
        })
      })
    })
  }

  getMachine(id){
    this.machine = this.machines.find((x) => x.id == id);
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
