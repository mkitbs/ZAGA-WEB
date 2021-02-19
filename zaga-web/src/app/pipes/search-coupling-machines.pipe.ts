import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchCouplingMachines',
  pure: false
})
export class SearchCouplingMachinesPipe implements PipeTransform {

  transform(devicesCoupling: any[], query): any {
    if(!query) {
      return devicesCoupling;
    }
    return devicesCoupling.filter(function(device){
      return JSON.stringify(device).toLowerCase().includes(query.toLowerCase())
    })
  }
}
