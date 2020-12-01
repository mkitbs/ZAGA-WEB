import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchEmployeePosition',
  pure: false,
})
export class SearchEmployeePositionPipe implements PipeTransform {

  transform(employees: any[], query): any {
    return employees.filter(employee =>
      employee.worker.Position.toLowerCase().includes(query.toLowerCase()));
  }

}
