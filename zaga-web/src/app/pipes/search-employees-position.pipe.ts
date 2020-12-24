import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchEmployeesPosition',
  pure: false,
})
export class SearchEmployeesPositionPipe implements PipeTransform {

  transform(employees: any[], query): any {
    if(!query){
      return employees;
    }
    var result = employees.filter((employee) => 
      employee.position.toLowerCase().includes(query.toLowerCase()));
    
    if(result.length == 0){
      return[-1];
    }
    return result;
  }

}
