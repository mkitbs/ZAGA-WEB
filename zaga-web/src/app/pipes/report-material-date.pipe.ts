import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialDate',
  pure: false,
})
export class ReportMaterialDatePipe implements PipeTransform {

  transform(materials: any[], query): any {
    return materials.filter((mat =>
      mat.workOrders.find((wo => 
        wo.date.toLowerCase().includes(query.toLowerCase())))
      ))  
  }

}
