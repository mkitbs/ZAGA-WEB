import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchPropulsionMachines',
  pure: false,
})
export class SearchPropulsionMachinesPipe implements PipeTransform {

  transform(devicesPropulstion: any[], query): any {
    if(!query) {
      return devicesPropulstion;
    }
    return devicesPropulstion.filter((device) =>
      this.matchValue(device, query)
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
