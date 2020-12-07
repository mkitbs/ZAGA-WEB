import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachineType',
  pure: false,
})
export class SerachMachineTypePipe implements PipeTransform {

  transform(machines: any[], query): any {
    return machines.filter(machine =>
      machine.Type.toLowerCase().includes(query.toLowerCase()));
  }

}
