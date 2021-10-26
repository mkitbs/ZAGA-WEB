import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachineSapId',
  pure: false
  
})
export class SearchMachineSapIdPipe implements PipeTransform {

  transform(machines: any[], query): any {
    if(!query){
      return machines;
    
    }
    const filtered = machines
    // filter nested lists first
    .map(machine => {
         // here we can use Object.assign to make shallow copy of obj, to preserve previous instance unchanged
         return Object.assign({}, machine, {
             workOrders: machine.workOrders.filter(wo => wo.split(",")[2].toLowerCase().includes(query.toLowerCase()))
         })
    })
    // then omit all items with empty list
    .filter(machine => machine.workOrders.length > 0)
  
    return filtered;
  
  }

}
