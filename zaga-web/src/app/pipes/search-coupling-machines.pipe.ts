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
    return devicesCoupling.filter((device) =>
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
