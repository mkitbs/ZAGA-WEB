import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersDate',
  pure: false,
})
export class SearchWorkOrdersDatePipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    return workOrders.filter((wo) =>
      wo.date.toLowerCase().includes(query.toLowerCase()));
  }

}
