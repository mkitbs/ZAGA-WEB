import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'SearchEmployeePosition',
  pure: false,
})
export class SearchEmployeePositionPipe implements PipeTransform {

  transform(employees: any[], query): any {
    if(!query){
      return employees;
    }
    var result = employees.filter(employee =>
      employee.worker.Position.toLowerCase().includes(query.toLowerCase()
    ));
    if(result.length === 0){
      return[-1];
    }
    return result;
  }

}
