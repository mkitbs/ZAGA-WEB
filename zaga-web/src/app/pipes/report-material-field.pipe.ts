import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialField',
  pure: false,
})
export class ReportMaterialFieldPipe implements PipeTransform {

  transform(materials: any[], query): any {
    return materials.filter((mat =>
      mat.workOrders.find((wo => 
        wo.table.toLowerCase().includes(query.toLowerCase())))
      ))
  }

}
