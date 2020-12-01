import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchMachineCoupling',
  pure: false,
})
export class SearchMachineCouplingPipe implements PipeTransform {

  transform(workOrders: any[], query): any {
    if(!query){
      return workOrders
    }
    return workOrders.filter(wo => 
      wo.workers.find(worker => 
      worker.connectingMachine.Name.toLowerCase().includes(query.toLowerCase()))
    )
  }

}
