import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachineGroup',
  pure: false,
})
export class SerachMachineGroupPipe implements PipeTransform {

  transform(machines: any[], query): any {
    if(!query) {
      return machines;
    }
    return machines.filter((machine) =>
      this.matchValue(machine, query)
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
