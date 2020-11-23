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
    return fields.filter((field) =>
      this.matchValue(field, query)
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
