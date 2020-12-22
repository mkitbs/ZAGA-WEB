import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialSapId',
  pure: false,
})
export class ReportMaterialSapIdPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders;
    }
    var result = workOrders.filter((wo => 
      wo.sapId.toString().toLowerCase().includes(query.toLowerCase())
    ))
    if(result.length === 0){
      return[-1];
    }
    return result;
  }
}
