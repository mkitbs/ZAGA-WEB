import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMachineMachine',
  pure: false,
})
export class ReportMachineMachinePipe implements PipeTransform {

  transform(machines: any[], query): any {
    return machines.filter(machine =>
      machine.machine.Name.toLowerCase().includes(query.toLowerCase()));
  }

}
