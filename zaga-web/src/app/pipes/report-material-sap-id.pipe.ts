import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialSapId',
  pure: false,
})
export class ReportMaterialSapIdPipe implements PipeTransform {

  transform(materials: any[], query): any {
    return materials.filter((mat =>
      mat.workOrders.find((wo => 
        wo.sapId.toString().toLowerCase().includes(query.toLowerCase())))
      ))
  }
}
