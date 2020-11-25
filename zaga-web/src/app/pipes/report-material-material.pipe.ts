import { analyzeAndValidateNgModules } from '@angular/compiler';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialMaterial',
  pure: false,
})
export class ReportMaterialMaterialPipe implements PipeTransform {

  transform(materials: any[], query): any {
    return materials.filter((mat =>
        mat.material.material.Name.toLowerCase().includes(query.toLowerCase())
      ))
  }
}
