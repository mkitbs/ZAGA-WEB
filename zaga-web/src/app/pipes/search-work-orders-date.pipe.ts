import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersDate',
  pure: false,
})
export class SearchWorkOrdersDatePipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders;
    }
    console.log(workOrders)
    var result = workOrders.filter((wo) =>
      wo.date.includes(query
    ));
    if(result.length === 0){
      return[-1];
    }
    return result;
  }

}
