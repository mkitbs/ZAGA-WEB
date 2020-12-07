import { ValueTransformer } from '@angular/compiler/src/util';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCulture',
  pure: false,
})
export class SearchCulturePipe implements PipeTransform {

  transform(cultures: any[], query): any {
    if(!query){
      return cultures;
    }
    return cultures.filter((culture) =>
      this.matchValue(culture, query)
    );
    
  }

  matchValue(data, value) {
    if(Number(value)){
      var numString = value.toString();
      var idString = data.Id.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else if(value.includes(" - ")){
      return data.Name.toLowerCase().includes(value.split(" - ")[1].toLowerCase()) 
    } else{
      return data.Name.toLowerCase().includes(value.toLowerCase()) 
    }
  }

}
