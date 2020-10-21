import { Component, OnInit } from '@angular/core';
import { MachineGroup } from 'src/app/models/MachineGroup';
import { MachineGroupService } from 'src/app/service/machine-group.service';

@Component({
  selector: 'app-machine-group',
  templateUrl: './machine-group.component.html',
  styleUrls: ['./machine-group.component.css']
})
export class MachineGroupComponent implements OnInit {

  constructor(private machineGroupService:MachineGroupService) { }

  machineGroups: MachineGroup[] = [];

  ngOnInit() {
    this.machineGroupService.getAll().subscribe(data => {
      this.machineGroups = data;
      console.log(this.machineGroups)
    })
  }

}
