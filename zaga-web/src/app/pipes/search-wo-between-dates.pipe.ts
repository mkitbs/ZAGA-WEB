import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWoBetweenDates',
  pure: false,
})
export class SearchWoBetweenDatesPipe implements PipeTransform {

  transform(materials: any[], dates): any {
    console.log(dates)
    if(dates.dateTo == "" && dates.namdateFrome == ""){
      return materials;
    } else {
      if(dates.dateFrom !== null){
        var dateFrom = new Date(dates.dateFrom.year, dates.dateFrom.month - 1, dates.dateFrom.day);
        console.log(dateFrom)
      } else {
        return materials;
      }
      if(dates.dateTo!== null){
        var dateTo = new Date(dates.dateTo.year, dates.dateTo.month - 1, dates.dateTo.day);
      } else {
        return materials;
      }

      const filtered = materials
      // filter nested lists first
      .map(mat => {
           // here we can use Object.assign to make shallow copy of obj, to preserve previous instance unchanged
           return Object.assign({}, mat, {
               workOrders: mat.workOrders.filter(wo => {
                  var time = new Date(wo.workOrderDate);
                  return (time <= dateTo && time >= dateFrom)
               })
           })
      })
      // then omit all items with empty list
      .filter(mat => mat.workOrders.length > 0)
     
      return filtered;
      
    }
  
  }

}
