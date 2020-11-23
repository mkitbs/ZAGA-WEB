import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMaterials',
  pure: false,
})
export class SearchMaterialsPipe implements PipeTransform {

  transform(substances: any[], query): any {
    if(!query) {
      return substances;
    }
    return substances.filter((sub) =>
      this.matchValue(sub, query)
    );
  }

  matchValue(data, value) {
    if(Number(value)){
      var numString = value.toString();
      var idString = data.Id.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else{
      return data.Name.toLowerCase().includes(value.toLowerCase()) 
    }
  }

}
