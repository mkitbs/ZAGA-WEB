import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachineMachineGroup',
  pure: false,
})
export class SearchMachineMachineGroupPipe implements PipeTransform {

  transform(machines: any[], query): any {
    if(!query){
      return machines;
    }
    var result = machines.filter(machine =>
      machine.machineGroupName.toLowerCase().includes(query.toLowerCase()));
    
    if(result.length === 0){
      return[-1]
    }
    return result;
  }

}
