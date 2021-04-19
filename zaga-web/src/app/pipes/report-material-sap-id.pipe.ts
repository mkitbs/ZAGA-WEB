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
    console.log(workOrders)
    workOrders.forEach((wo, index) => {
      if(wo.sapId == null || wo.sapId == 0) {
        workOrders.splice(index, 1);
      }
    })
    var result = workOrders.filter((wo => 
      wo.sapId.toString().toLowerCase().includes(query.toLowerCase())
    ))
    if(result.length === 0){
      return[-1];
    }
    return result;
  }
}
