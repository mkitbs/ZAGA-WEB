import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "SearchEmployees",
  pure: false,
})
export class SearchEmployeesPipe implements PipeTransform {
  transform(employees: any[], query): any {
    return employees.filter((employee) =>
      this.matchValue(employee, query)
    );
  }

  matchValue(data, value) {
    if(Number(value)){
      return data.perNumber == value;
    } else{
      return data.name.toLowerCase().includes(value.toLowerCase()) 
    }
  }
}
