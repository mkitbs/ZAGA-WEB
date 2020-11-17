import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersCrop',
  pure: false,
})
export class SearchWorkOrdersCropPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    return workOrders.filter((wo) =>
    wo.cropName.split(",")[1].toLowerCase().includes(query.toLowerCase()));
  }

}
