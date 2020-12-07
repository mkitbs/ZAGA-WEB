import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchOperations',
  pure: false,
})
export class SearchOperationsPipe implements PipeTransform {

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
      var idString = data.Id.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else if(value.includes(" - ")){
      return data.Name.toLowerCase().includes(value.split(" - ")[1].toLowerCase())
    } else{
      return data.Name.toLowerCase().includes(value.toLowerCase()) 
    }
  }

}
