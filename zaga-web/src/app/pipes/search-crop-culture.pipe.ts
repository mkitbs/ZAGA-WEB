import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCropCulture',
  pure: false,
})
export class SearchCropCulturePipe implements PipeTransform {

  transform(cultures: any[], query): any {
    if(!query){
      return cultures;
    }
    var result = cultures.filter((culture) =>
      this.matchValue(culture, query)
    );
    if(result.length === 0){
      return[-1]
    }
    return result;
    
  }

  matchValue(data, value) {
    if(Number(value)){
      var numString = value.toString();
      var idString = data.CultureId.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else if(value.includes(" - ")){
      return data.CultureId.toString().toLowerCase().includes(value.split(" - ")[0].toLowerCase())
    } else{
      return data.cultureName.toLowerCase().includes(value.toLowerCase()) 
    }
  }
}
