import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { Operation } from 'src/app/models/Operation';
import { OperationGroup } from 'src/app/models/OperationGroup';
import { OperationGroupService } from 'src/app/service/operation-group.service';
import { OperationService } from 'src/app/service/operation.service';

@Component({
  selector: 'app-operation',
  templateUrl: './operation.component.html',
  styleUrls: ['./operation.component.css']
})
export class OperationComponent implements OnInit {

  constructor(
    private operationService:OperationService,
    private operationGroupService:OperationGroupService,
    private spinner: NgxSpinnerService
    ) { }

  operations: Operation[] = [];
  operationGroups: OperationGroup[] = [];
  operation: Operation = new Operation();
  operationTypes: Operation[] = [];
  
  operationFC: FormControl = new FormControl("");
  operationTypeFC: FormControl = new FormControl("");
  operationGroupFC: FormControl = new FormControl("");

  loading;

  ngOnInit() {
    this.getAll();
    this.getAllGroupByType();
  }

  getAll(){
    this.spinner.show();
    this.loading = true;
    this.operationGroupService.getAll().subscribe(data => {
      this.operationGroups = data;
      this.operationService.getAll().subscribe(data => {
        this.spinner.hide();
        this.loading = false;
        this.operations = data;
        this.operations.forEach(operation => {
          if(operation.Type == "CROP_FARMING"){
            operation.Type = "RATARSTVO";
          } else if(operation.Type == "VITICULTURE"){
            operation.Type = "VINOGRADARSTVO";
          } else if(operation.Type == "FRUIT_GROWING"){
            operation.Type = "VOĆARSTVO";
          } else if(operation.Type == "VEGETABLE"){
            operation.Type = "POVRTARSTVO";
          } else if(operation.Type == "ANIMAL_HUSBANDRY"){
            operation.Type = "STOČARSTVO";
          }
          operation.operationGroup = this.operationGroups.find(operationGroup => operationGroup.dbId == operation.operationGroupId).Name
        });
      }, error =>{
        this.spinner.hide();
      });
    }, error =>{
      this.spinner.hide();
    });
  }

  getOperation(id){
    this.operation = this.operations.find(x => x.dbid == id);
  }

  editOperation(){
    this.operationService.editOperation(this.operation).subscribe(res => {
      console.log(res);
      this.getAll();
    })
  }

  getAllGroupByType(){
    this.operationService.getAllGroupByType().subscribe(data => {
      this.operationTypes = data;
      this.operationTypes.forEach(operation => {
        if(operation.Type == "CROP_FARMING"){
          operation.Type = "RATARSTVO";
        } else if(operation.Type == "VITICULTURE"){
          operation.Type = "VINOGRADARSTVO";
        } else if(operation.Type == "FRUIT_GROWING"){
          operation.Type = "VOĆARSTVO";
        } else if(operation.Type == "VEGETABLE"){
          operation.Type = "POVRTARSTVO";
        } else if(operation.Type == "ANIMAL_HUSBANDRY"){
          operation.Type = "STOČARSTVO";
        }
      })
    })
  }
  
  displayFnOperation(operation: Operation): string {
    return operation && operation.Id + " - " + operation.Name;
  }

  displayFnOperationType(operation: Operation): string {
    return operation && operation.Type;
  }

  displayFnOperationGroup(operationGroup: OperationGroup): string {
    return operationGroup && operationGroup.Id + " - " + operationGroup.Name;
  }
}
