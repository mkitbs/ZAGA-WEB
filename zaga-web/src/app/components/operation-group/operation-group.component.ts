import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { OperationGroup } from 'src/app/models/OperationGroup';
import { OperationGroupService } from 'src/app/service/operation-group.service';

@Component({
  selector: 'app-operation-group',
  templateUrl: './operation-group.component.html',
  styleUrls: ['./operation-group.component.css']
})
export class OperationGroupComponent implements OnInit {

  constructor(private operationGroupService:OperationGroupService, private spinner: NgxSpinnerService) { }

  operationGroups: OperationGroup[] = [];

  operationGroupFC: FormControl = new FormControl("");

  loading;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.operationGroupService.getAll().subscribe(data => {
      this.spinner.hide();
      this.loading = false;
      this.operationGroups = data;
    });
  }

  displayFnOperationGroup(operation: OperationGroup): string {
    return operation && operation.Id + " - " + operation.Name;
  }

}
