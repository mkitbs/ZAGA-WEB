import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchFields',
  pure: false
})
export class SearchFieldsPipe implements PipeTransform {

  transform(fields: any[], query): any {
    if(!query) {
      return fields;
    }
    let result =  fields.filter((field) =>
      this.matchValue(field, query)
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
