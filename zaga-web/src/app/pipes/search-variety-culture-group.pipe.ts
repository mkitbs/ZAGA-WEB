import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchVarietyCultureGroup',
  pure: false,
})
export class SearchVarietyCultureGroupPipe implements PipeTransform {

  transform(varieties: any[], query): any {
    if(!query) {
      return varieties;
    }
    return varieties.filter((variety) =>
      this.matchValue(variety, query)
    );
  }

  matchValue(data, value) {
    if(Number(value)){
      var numString = value.toString();
      var idString = data.CultureId.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else if(value.includes(" - ")){
      return data.cultureName.toLowerCase().includes(value.split(" - ")[1].toLowerCase())
    } else{
      return data.cultureName.toLowerCase().includes(value.toLowerCase()) 
    }
  }

}
