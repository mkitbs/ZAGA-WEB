import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { MachineGroup } from 'src/app/models/MachineGroup';
import { MachineGroupService } from 'src/app/service/machine-group.service';

@Component({
  selector: 'app-machine-group',
  templateUrl: './machine-group.component.html',
  styleUrls: ['./machine-group.component.css']
})
export class MachineGroupComponent implements OnInit {

  constructor(private machineGroupService:MachineGroupService, private spinner: NgxSpinnerService,) { }

  machineGroups: MachineGroup[] = [];
  machineGroupFC: FormControl = new FormControl("")

  loading;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.machineGroupService.getAll().subscribe(data => {
      this.spinner.hide();
      this.loading = false;
      this.machineGroups = data;
      console.log(this.machineGroups)
    })
  }

  displayFnMachineGroup(machine: MachineGroup): string {
    return machine && machine.Id + " - " + machine.Name;
  }

}
