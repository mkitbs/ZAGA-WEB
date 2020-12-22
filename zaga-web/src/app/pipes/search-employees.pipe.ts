import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "SearchEmployees",
  pure: false,
})
export class SearchEmployeesPipe implements PipeTransform {
  transform(employees: any[], query): any {
    if(!query){
      return employees;
    }
    var result = employees.filter((employee) =>
      this.matchValue(employee, query)
    );
    if(result.length === 0){
      return[-1];
    }
    return result;
  }

  matchValue(data, value) {
    if(Number(value)){
      return data.perNumber == value;
    } else{
      return data.name.toLowerCase().includes(value.toLowerCase()) 
    }
  }
}
