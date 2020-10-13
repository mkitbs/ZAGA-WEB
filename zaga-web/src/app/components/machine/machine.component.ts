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

  ngOnInit() {
    this.machineGroupSerivce.getAll().subscribe(data => {
      this.machineGroups = data;
    })
    this.machineService.getAll().subscribe(data => {
      this.machines = data;
      console.log(this.machines);
    })


  }

}
