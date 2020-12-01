import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachineMachineGroup',
  pure: false,
})
export class SearchMachineMachineGroupPipe implements PipeTransform {

  transform(machines: any[], query): any {
    return machines.filter(machine =>
      machine.machineGroupName.toLowerCase().includes(query.toLowerCase()));
  }

}
