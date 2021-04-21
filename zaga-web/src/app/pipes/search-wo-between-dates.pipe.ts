import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchWoBetweenDates',
  pure: false,
})
export class SearchWoBetweenDatesPipe implements PipeTransform {

  transform(workOrders: any[], dates): any {
    if(dates.dateTo == "" && dates.namdateFrome == ""){
      return workOrders;
    } else {
      if(dates.dateFrom !== null){
        var dateFrom = new Date(dates.dateFrom.year, dates.dateFrom.month, dates.dateFrom.day);
      } else {
        return workOrders;
      }
      if(dates.dateTo!== null){
        var dateTo = new Date(dates.dateTo.year, dates.dateTo.month, dates.dateTo.day);
      } else {
        return workOrders;
      }
      var wos: any[] = [];
      workOrders.forEach(wo => {
        var woDate = new Date( wo.date.split(".")[2], wo.date.split(".")[1], wo.date.split(".")[0])
        if(woDate <= dateTo && woDate >= dateFrom){
          wos.push(wo);
        }
      });
     
      return wos;
      
    }
  
  }

}
