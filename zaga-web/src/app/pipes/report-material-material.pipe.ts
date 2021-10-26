import { analyzeAndValidateNgModules } from '@angular/compiler';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMaterialMaterial',
  pure: false,
})
export class ReportMaterialMaterialPipe implements PipeTransform {

  transform(materials: any[], query): any {
    if(!query){
     
      return materials;
    }
    var result = materials.filter((mat =>
      mat.material.material.Name.toLowerCase().includes(query.toLowerCase())
    ))
    if(result.length === 0){
      return[-1];
    }
    return result;
  }
}
