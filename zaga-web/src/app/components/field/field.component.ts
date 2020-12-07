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

  lat = 45.588880;
  lng = 19.996049;
  map: any;
  paths;

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

  onMapReady(map) {
    this.map = map;
  }

  onChoseLocation(event){
    this.lat = event.coords.lat;    
    this.lng = event.coords.lng;
  }

  getFieldOnMap(){
    var lat1 = 45.585433;
    var lng1 = 20.008278;
    var lat2 = 45.583605;
    var lng2 = 20.016601;
    var lat3 = 45.591386;
    var lng3 = 20.020407;
    var lat4 = 45.593470;
    var lng4 = 20.011968;

    this.lat = lat1 + ((lat3 - lat1) / 2);
    this.lng = lng1 + ((lng3 - lng1) / 2);
    this.paths = [
      {lat: lat1, lng: lng1},
      {lat: lat2, lng: lng2},
      {lat: lat3, lng: lng3},
      {lat: lat4, lng: lng4}
    ]
  }

}
