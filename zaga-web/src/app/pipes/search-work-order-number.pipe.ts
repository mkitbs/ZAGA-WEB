import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrderNumber',
  pure: false
})
export class SearchWorkOrderNumberPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders;
    }
    workOrders.forEach((wo, index) => {
      if(wo.sapId == null) {
        workOrders.splice(index, 1);
      }
    })
    var result = workOrders.filter((wo =>
      wo.sapId.toString().toLowerCase().includes(query.toLowerCase()
    )));
    if(result.length === 0){
      return[-1]
    }
    return result;
  }

}
