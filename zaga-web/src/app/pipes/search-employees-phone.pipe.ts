import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "SearchEmployeesPhone",
  pure: false,
})
export class SearchEmployeesPhonePipe implements PipeTransform {
  transform(employees: any[], query): any {
    if(!query){
      return employees;
    }
    return employees.filter((employee) =>
      employee.name.toLowerCase().includes(query.toLowerCase())
    );
  }
}
