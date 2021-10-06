import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportEmployeeWorker',
  pure: false,
})
export class ReportEmployeeWorkerPipe implements PipeTransform {

  transform(workers: any[], query): any {
    if(!query){
      return workers;
    }
    var result =  workers.filter(worker =>
      worker.worker.toLowerCase().includes(query.toLowerCase()
    ));
    
    if(result.length === 0){
      return[-1];
    }
    return result;
  }

}
