import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchEmployees',
  pure: false
})
export class SearchEmployeesPipe implements PipeTransform {

  transform(employees: any[], query): any {
    
    return employees.filter(employee => employee.name.toLowerCase().includes(query) );
  }

}
