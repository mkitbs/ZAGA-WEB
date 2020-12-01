import { Component, OnInit } from "@angular/core";
import { FormControl } from '@angular/forms';
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
  fieldFC: FormControl = new FormControl("")

  ngOnInit() {
    this.fieldGroupService.getAll().subscribe((data) => {
      //data = this.convertKeysToLowerCase(data);
      this.fieldGroups = data;
      this.fieldService.getAll().subscribe((data) => {
        this.fields = data;
        this.fields.forEach((field) => {
          field.fieldGroupName = this.fieldGroups.find(
            (fieldGroup) => fieldGroup.dbId == field.fieldGroup
          ).Name;
        });
      });
    });
  }

  getField(id) {
    this.field = this.fields.find((field) => field.dbId == id);
  }

  editField() {
    this.fieldService.editField(this.field).subscribe((data) => {
      this.fieldService.getAll().subscribe((data) => {
        this.fields = data;
        this.fields.forEach((field) => {
          field.fieldGroupName = this.fieldGroups.find(
            (fieldGroup) => fieldGroup.dbId == field.fieldGroup
          ).Name;
        });
      });
    });
  }

  displayFnField(field : Field): string {
    return field && field.Id + " - " + field.Name;
  }
}
