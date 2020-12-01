import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchVariety',
  pure: false,
})
export class SearchVarietyPipe implements PipeTransform {

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
      var idString = data.Id.toString();
      return idString.toLowerCase().includes(numString.toLowerCase())
    } else if(value.includes(" - ")){
      return data.Name.toLowerCase().includes(value.split(" - ")[1].toLowerCase())
    } else{
      return data.Name.toLowerCase().includes(value.toLowerCase()) 
    }
  }


}
