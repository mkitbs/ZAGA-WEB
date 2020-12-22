import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachine',
  pure: false,
})
export class SearchMachinePipe implements PipeTransform {

  transform(machines: any[], query): any {
    if(!query){
      return machines;
    }
    var result = machines.filter((machine) =>
      this.matchValue(machine, query)
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
