import { Component, OnInit } from '@angular/core';
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
    private operationGroupService:OperationGroupService
    ) { }

  operations: Operation[] = [];
  operationGroups: OperationGroup[] = [];
  operation: Operation = new Operation();

  selectedType;
  selectedGroup;

  ngOnInit() {
    this.getAll();
  }

  getAll(){
    this.operationGroupService.getAll().subscribe(data => {
      this.operationGroups = data;
      this.operationService.getAll().subscribe(data => {
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
      });
    });
  }

  getOperation(id){
    this.operation = this.operations.find(x => x.id == id);
  }

  editOperation(){
    this.operationService.editOperation(this.operation).subscribe(res => {
      console.log(res);
      this.getAll();
    })
  }

  getOperations(){
    if (
      //izabrani su i tip i grupa
      this.selectedType != undefined &&
      this.selectedGroup != undefined &&
      this.selectedType != -1 &&
      this.selectedGroup != -1
    ) {
      this.operationGroupService.getAll().subscribe(data => {
        this.operationGroups = data;
        this.operationService.getAllByTypeAndGroup(this.selectedType, this.selectedGroup).subscribe(data => {
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
        });
      });
    } else if (
      //nije izabrano nista, odnosono ponisteni su prethodni izbori
      (this.selectedType == -1 || this.selectedType == undefined) &&
      (this.selectedGroup == -1 || this.selectedGroup == undefined)
    ) {
      this.getAll();
    } else if (
      //izabran je samo tip
      (this.selectedGroup == undefined || this.selectedGroup == -1) &&
      (this.selectedType != undefined || this.selectedType != -1)
    ) {
      this.operationGroupService.getAll().subscribe(data => {
        this.operationGroups = data;
        this.operationService.getAllByType(this.selectedType).subscribe(data => {
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
        });
      });
    } else if (
      //izabrana je samo grupa
      (this.selectedType == undefined || this.selectedType == -1) &&
      (this.selectedGroup != undefined || this.selectedGroup != -1)
    ) {
      this.operationGroupService.getAll().subscribe(data => {
        this.operationGroups = data;
        this.operationService.getAllByGroup(this.selectedGroup).subscribe(data => {
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
        });
      });
    }
  }

  setValueForSelectedType(){
    this.selectedType = -1;
    this.getOperations();
  }

  setValueForSelectedGroup(){
    this.selectedGroup = -1;
    this.getOperations();
  }
  
}
