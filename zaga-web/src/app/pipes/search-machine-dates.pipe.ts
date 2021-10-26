import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachineDates',
  pure: false
})
export class SearchMachineDatesPipe implements PipeTransform {

  transform(machines: any[], dates): any {
    
    if(dates.dateTo == "" && dates.namdateFrome == ""){
      return machines;
    } else {
      if(dates.dateFrom !== null){
        var dateFrom = new Date(dates.dateFrom.year, dates.dateFrom.month - 1, dates.dateFrom.day);
        
      } else {
        return machines;
      }
      if(dates.dateTo!== null){
        var dateTo = new Date(dates.dateTo.year, dates.dateTo.month - 1, dates.dateTo.day);
      } else {
        return machines;
      }

      const filtered = machines
      // filter nested lists first
      .map(machine => {
           // here we can use Object.assign to make shallow copy of obj, to preserve previous instance unchanged
           return Object.assign({}, machine, {
               workOrders: machine.workOrders.filter(wo => {
                  var time = new Date(wo.split(",")[4]);
                  return (time <= dateTo && time >= dateFrom)
               })
           })
      })
      // then omit all items with empty list
      .filter(machine => machine.workOrders.length > 0)
     
      return filtered;
      
    }

  }
}
