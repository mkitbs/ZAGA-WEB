import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrderCrop',
  pure: false
})
export class SearchWorkOrderCropPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query) {
      return workOrders;
    }

    var result = workOrders.filter(wo => wo.cropName.toLowerCase().includes(query.toLowerCase()))
    return result;
  }

}
