import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrderField',
  pure: false
})
export class SearchWorkOrderFieldPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query) {
      return workOrders;
    }

    var result = workOrders.filter(wo => wo.table.toLowerCase().includes(query.toLowerCase()))
    return result;
  }

}
