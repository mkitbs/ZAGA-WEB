import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersOperation',
  pure: false,
})
export class SearchWorkOrdersOperationPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders;
    }
    var result = workOrders.filter((wo) =>
      wo.operationName.toLowerCase().includes(query.toLowerCase()
    ));
    if(result.length === 0){
      return[-1];
    }
    return result;
  }

}
