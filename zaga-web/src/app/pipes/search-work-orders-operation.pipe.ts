import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersOperation',
  pure: false,
})
export class SearchWorkOrdersOperationPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    return workOrders.filter((wo) =>
    wo.operationName.toLowerCase().includes(query.toLowerCase()));
  }

}
