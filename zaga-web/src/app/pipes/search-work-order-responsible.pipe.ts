import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrderResponsible',
  pure: false
})
export class SearchWorkOrderResponsiblePipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders;
    }
    var result = workOrders.filter((wo) =>
      wo.responsibleName.toLowerCase().includes(query.toLowerCase()
    ));
    if(result.length === 0){
      return[-1]
    }
    return result;
  }
}


