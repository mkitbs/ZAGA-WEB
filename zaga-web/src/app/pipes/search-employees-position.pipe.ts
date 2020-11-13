import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchEmployeesPosition',
  pure: false,
})
export class SearchEmployeesPositionPipe implements PipeTransform {

  transform(employees: any[], query): any {
    return employees.filter((employee) => 
      employee.position.toLowerCase().includes(query.toLowerCase()));
  }

}
