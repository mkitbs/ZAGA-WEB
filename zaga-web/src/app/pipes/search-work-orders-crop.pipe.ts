import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersCrop',
  pure: false,
})
export class SearchWorkOrdersCropPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders;
    }
    var result = workOrders.filter((wo) =>
      wo.cropName.split(",")[1].toLowerCase().includes(query.toLowerCase()
    ));
    if(result.length === 0){
      return[-1]
    }
    return result;
  }

}
