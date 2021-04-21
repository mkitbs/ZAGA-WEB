import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersTable',
  pure: false
})
export class SearchWorkOrdersTablePipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders;
    }
    var result = workOrders.filter((wo =>
      wo.table.toLowerCase().includes(query.toLowerCase())
    ));
    
    return result;
  }

}
