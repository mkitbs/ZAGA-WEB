import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ReportEmployeeWorker',
  pure: false,
})
export class ReportEmployeeWorkerPipe implements PipeTransform {

  transform(workers: any[], query): any {
    return workers.filter(worker =>
      worker.worker.Name.toLowerCase().includes(query.toLowerCase()));
  }

}
