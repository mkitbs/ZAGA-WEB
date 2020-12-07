import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMaterialUnit',
  pure: false,
})
export class SearchMaterialUnitPipe implements PipeTransform {

  transform(materials: any[], query): any {
    return materials.filter(material =>
      material.material.material.Unit.toLowerCase().includes(query.toLowerCase()));
  }

}
