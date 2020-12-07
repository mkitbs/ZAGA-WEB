import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCultureGroup',
  pure: false,
})
export class SearchCultureGroupPipe implements PipeTransform {

  transform(cultureGroups: any[], query): any {
    if(!query) {
      return cultureGroups;
    }
    return cultureGroups.filter((cultureGroup) =>
      this.matchValue(cultureGroup, query)
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
