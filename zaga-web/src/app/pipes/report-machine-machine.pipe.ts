import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportMachineMachine',
  pure: false,
})
export class ReportMachineMachinePipe implements PipeTransform {

  transform(machines: any[], query): any {
    if(!query){
      return machines;
    }
    var result = machines.filter(machine =>
      machine.machine.Name.toLowerCase().includes(query.toLowerCase()
    ));
    if(result.length === 0){
      return[-1];
    }
    return result;
  }

}
