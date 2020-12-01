import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchOperationType',
  pure: false,
})
export class SearchOperationTypePipe implements PipeTransform {

  transform(operations: any[], query): any {
    return operations.filter(op =>
      op.Type.toLowerCase().includes(query.toLowerCase()));
  }

}
