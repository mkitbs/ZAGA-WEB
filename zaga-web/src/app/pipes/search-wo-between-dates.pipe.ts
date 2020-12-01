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
      var dateFrom = new Date(dates.dateFrom.year, dates.dateFrom.month, dates.dateFrom.day);
      var dateTo = new Date(dates.dateTo.year, dates.dateTo.month, dates.dateTo.day);
      //var dateFrom = dates.dateFrom.month + "/" + dates.dateFrom.day + "/" + dates.dateFrom.year;
      //var dateTo = dates.dateTo.month + "/" + dates.dateTo.day + "/" + dates.dateTo.year;
      var wos: any[] = [];
      workOrders.forEach(wo => {
        var woDate = new Date( wo.date.split(".")[2], wo.date.split(".")[1], wo.date.split(".")[0])
        if(woDate <= dateTo && woDate >= dateFrom){
          console.log(wo)
          wos.push(wo);
        }
      });
      console.log(wos)
      return wos;
      
    }
  
  }

}
