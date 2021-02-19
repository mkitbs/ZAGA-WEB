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
    return devicesPropulstion.filter(function(device){
      return JSON.stringify(device).toLowerCase().includes(query.toLowerCase())
    })
  }

}
