import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialSapId',
  pure: false,
})
export class ReportMaterialSapIdPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    return workOrders.filter((wo => 
      wo.sapId.toString().toLowerCase().includes(query.toLowerCase())
    ))
  }
}
