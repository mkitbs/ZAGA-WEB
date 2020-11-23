import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultures',
  pure: false,
})
export class SearchCulturesPipe implements PipeTransform {

  transform(crops: any[], query): any {
    if(!query) {
      return crops;
    }
    return crops.filter((crop) =>
      this.matchValue(crop, query)
    );
  }

  matchValue(data, value) {
    if(Number(value)){
      var numString = value.toString();
      var idString = data.Id.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else{
      return data.Name.split(",")[1].toLowerCase().includes(value.toLowerCase()) 
    }
  }

}
