import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultureCultureGroup',
  pure: false,
})
export class SearchCultureCultureGroupPipe implements PipeTransform {

  transform(cultures: any[], query): any {
    if(!query) {
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
      console.log(data.cultureGroupId)
      console.log(data.cultureGroupName)
      var numString = value.toString();
      var idString = data.CultureGroupId.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else if(value.includes(" - ")){
      return data.cultureGroupName.toLowerCase().includes(value.split(" - ")[1].toLowerCase())
    } else{
      return data.cultureGroupName.toLowerCase().includes(value.toLowerCase()) 
    }
  }

}
