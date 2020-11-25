import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialCrop',
  pure: false,
})
export class ReportMaterialCropPipe implements PipeTransform {

  transform(materials: any[], query): any {
    return materials.filter((mat =>
      mat.workOrders.find((wo => 
        wo.cropName.split(",")[1].toLowerCase().includes(query.toLowerCase())))
      ))
  }

}
