import { Component, OnInit } from '@angular/core';
import { OperationGroup } from 'src/app/models/OperationGroup';
import { OperationGroupService } from 'src/app/service/operation-group.service';

@Component({
  selector: 'app-operation-group',
  templateUrl: './operation-group.component.html',
  styleUrls: ['./operation-group.component.css']
})
export class OperationGroupComponent implements OnInit {

  constructor(private operationGroupService:OperationGroupService) { }

  operationGroups: OperationGroup[] = [];

  ngOnInit() {
    this.operationGroupService.getAll().subscribe(data => {
      this.operationGroups = data;
    });
  }

}
