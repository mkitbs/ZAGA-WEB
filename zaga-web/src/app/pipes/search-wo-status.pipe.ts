import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchStatus'
})
export class SearchWoStatusPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders;
    }
    var result = workOrders.filter((wo =>
      wo.status.toLowerCase().includes(query.toLowerCase()
    )));
    if(result.length === 0){
      return[-1]
    }
    return result;
  }

}
