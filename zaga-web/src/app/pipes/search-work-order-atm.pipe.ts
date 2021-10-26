import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrderAtm',
  pure: false
})
export class SearchWorkOrderAtmPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query) {
      return workOrders;
    }

    var result = workOrders.filter(wo => wo.operationName.toLowerCase().includes(query.toLowerCase()))
    return result;
  }

}
