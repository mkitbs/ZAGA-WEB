import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchOperationType',
  pure: false,
})
export class SearchOperationTypePipe implements PipeTransform {

  transform(operations: any[], query): any {
    if(!query){
      return operations;
    }
    var result = operations.filter(op =>
      op.Type.toLowerCase().includes(query.toLowerCase()));
    
    if(result.length === 0){
      return[-1];
    }
    return result;
  }

}
