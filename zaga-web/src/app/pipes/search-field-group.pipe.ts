import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchFieldGroup',
  pure: false,
})
export class SearchFieldGroupPipe implements PipeTransform {

  transform(fieldGrous: any[], query): any {
    if(!query) {
      return fieldGrous;
    }
    return fieldGrous.filter((fieldGrou) =>
      this.matchValue(fieldGrou, query)
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
