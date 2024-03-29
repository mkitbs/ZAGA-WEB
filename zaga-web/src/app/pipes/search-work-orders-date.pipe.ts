import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersDate',
  pure: false,
})
export class SearchWorkOrdersDatePipe implements PipeTransform {

  transform(materials: any[], query): any {
    if(!query){
      return materials;
    }
    
    const filtered = materials
      // filter nested lists first
      .map(mat => {
           // here we can use Object.assign to make shallow copy of obj, to preserve previous instance unchanged
           return Object.assign({}, mat, {
               workOrders: mat.workOrders.filter(wo => {
                  var time = wo.workOrderDate.split("T")[0];
                  var formattedTime = time.split("-")[2] + "." + time.split("-")[1] + "." + time.split("-")[0] + ".";
                  return formattedTime == query
               })
           })
      })
      // then omit all items with empty list
      .filter(mat => mat.workOrders.length > 0)
    
    return filtered;
  }

}
