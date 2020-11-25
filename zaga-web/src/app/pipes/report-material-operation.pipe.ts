import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialOperation',
  pure: false,
})
export class ReportMaterialOperationPipe implements PipeTransform {

  transform(materials: any[], query): any {
    return materials.filter((mat =>
      mat.workOrders.find((wo => 
        wo.operationName.toLowerCase().includes(query.toLowerCase())))
      ))
  }

}
