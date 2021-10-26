import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMaterialUnit',
  pure: false,
})
export class SearchMaterialUnitPipe implements PipeTransform {

  transform(materials: any[], query): any {
    if(!query){
      return materials;
    }
   
    var result = materials.filter(material =>
      material.material.material.Unit.toLowerCase().includes(query.toLowerCase()
    ));
    if(result.length === 0){
      return[-1];
    }
    return result;
  }

}
