import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWorkOrdersCrop',
  pure: false,
})
export class SearchWorkOrdersCropPipe implements PipeTransform {

  transform(materials: any[], query): any {
    if(!query){
      return materials;
    }
   
    const filtered = materials
    // filter nested lists first
    .map(mat => {
         // here we can use Object.assign to make shallow copy of obj, to preserve previous instance unchanged
         return Object.assign({}, mat, {
             workOrders: mat.workOrders.filter(wo => wo.crop.toLowerCase().includes(query.toLowerCase()))
         })
    })
    // then omit all items with empty list
    .filter(mat => mat.workOrders.length > 0)

    return filtered;
  }

}
