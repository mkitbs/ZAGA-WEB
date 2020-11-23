import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersTable',
  pure: false
})
export class SearchWorkOrdersTablePipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    return workOrders.filter((wo =>
      wo.table.toLowerCase().includes(query.toLowerCase())
    ));
  }

}
