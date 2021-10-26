import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachineDate',
  pure: false
})
export class SearchMachineDatePipe implements PipeTransform {

  transform(machines: any[], query): any {
    if(!query){
      return machines;
    }
    
    const filtered = machines
      // filter nested lists first
      .map(machine => {
           // here we can use Object.assign to make shallow copy of obj, to preserve previous instance unchanged
           return Object.assign({}, machine, {
               workOrders: machine.workOrders.filter(wo => {
                  var time = wo.split(",")[4];
                  
                  time = time.split(" ")[0];
                  var formattedTime = time.split("-")[2] + "." + time.split("-")[1] + "." + time.split("-")[0] + ".";
                  return formattedTime == query
               })
           })
      })
      // then omit all items with empty list
      .filter(machine => machine.workOrders.length > 0)
    
    return filtered;
  }

}
