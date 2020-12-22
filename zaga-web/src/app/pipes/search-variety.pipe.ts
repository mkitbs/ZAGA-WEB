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
    var result = varieties.filter((variety) =>
      this.matchValue(variety, query)
    );
    if(result.length === 0){
      return[-1];
    }
    return result;
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
