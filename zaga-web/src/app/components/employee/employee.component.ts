import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { Employee } from 'src/app/models/Employee';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {

  constructor(private userService: UserService) { }

  allEmployees: Employee[] = [];
  employee: Employee = new Employee();

  nameFC: FormControl = new FormControl("");

  filteredOptions: Observable<string[]>;

  ngOnInit() {
    this.userService.getAll().subscribe((data) => {
      this.allEmployees = data.content;
      console.log(this.allEmployees)
    });
  }

  getEmployee(id){
    this.employee = this.allEmployees.find(x => x.userId == id);
    console.log(this.employee)
  }

  editUser(){
    console.log(this.employee)
    this.userService.editUser(this.employee).subscribe(res => {
      console.log(res);
      this.userService.getAll().subscribe((data) => {
        this.allEmployees = data.content;
      });
    });
  }

  //autocomplete
  displayFn(emp: Employee): string {
    return emp && emp.name ? emp.name : "";
  }

}
