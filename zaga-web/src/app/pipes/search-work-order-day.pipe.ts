import { Pipe, PipeTransform } from '@angular/core';


@Pipe({
  name: 'SearchWorkOrderDay',
  pure: false
})
export class SearchWorkOrderDayPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query) {
      return workOrders;
    }

    var result = workOrders.filter(wo => wo.date == query)
    return result;
  }

}
