import { Component, OnInit } from "@angular/core";
import { Field } from "src/app/models/Field";
import { FieldGroup } from "src/app/models/FieldGroup";
import { FieldGroupService } from "src/app/service/field-group.service";
import { FieldService } from "src/app/service/field.service";

@Component({
  selector: "app-field",
  templateUrl: "./field.component.html",
  styleUrls: ["./field.component.css"],
})
export class FieldComponent implements OnInit {
  constructor(
    private fieldService: FieldService,
    private fieldGroupService: FieldGroupService
  ) {}

  fields: Field[] = [];
  fieldGroups: FieldGroup[] = [];

  field: Field = new Field();

  ngOnInit() {
    this.fieldGroupService.getAll().subscribe((data) => {
      data = this.convertKeysToLowerCase(data);
      this.fieldGroups = data;
      this.fieldService.getAll().subscribe((data) => {
        this.fields = data;
        this.fields.forEach((field) => {
          field.fieldGroupName = this.fieldGroups.find(
            (x) => x.dbid == field.fieldGroup
          ).name;
        });
      });
    });
  }

  getField(id) {
    this.field = this.fields.find((x) => x.dbid == id);
    console.log(this.field);
  }

  editField() {
    this.fieldService.editField(this.field).subscribe((data) => {
      console.log(data);
      this.fieldService.getAll().subscribe((data) => {
        this.fields = data;
        this.fields.forEach((field) => {
          field.fieldGroupName = this.fieldGroups.find(
            (x) => x.dbid == field.fieldGroup
          ).name;
        });
      });
    });
  }

  //method for convert json property names to lower case
  convertKeysToLowerCase(obj) {
    var output = [];
    for (let i in obj) {
      if (Object.prototype.toString.apply(obj[i]) === "[object Object]") {
        output[i.toLowerCase()] = this.convertKeysToLowerCase(obj[i]);
      } else if (Object.prototype.toString.apply(obj[i]) === "[object Array]") {
        output[i.toLowerCase()] = [];
        output[i.toLowerCase()].push(this.convertKeysToLowerCase(obj[i][0]));
      } else {
        output[i.toLowerCase()] = obj[i];
      }
    }
    return output;
  }
}
