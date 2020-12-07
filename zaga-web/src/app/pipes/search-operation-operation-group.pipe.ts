import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchOperationOperationGroup',
  pure: false,
})
export class SearchOperationOperationGroupPipe implements PipeTransform {

  transform(operations: any[], query): any {
    if(!query) {
      return operations;
    }
    return operations.filter((operation) =>
      this.matchValue(operation, query)
    );
  }

  matchValue(data, value) {
    if(Number(value)){
      var numString = value.toString();
      var idString = data.operationGroupErpId.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else if(value.includes(" - ")){
      return data.operationGroupName.toLowerCase().includes(value.split(" - ")[1].toLowerCase())
    } else{
      return data.operationGroupName.toLowerCase().includes(value.toLowerCase()) 
    }
  }

}
