import { ɵAnimationGroupPlayer } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { FieldGroup } from 'src/app/models/FieldGroup';
import { FieldGroupService } from 'src/app/service/field-group.service';

@Component({
  selector: 'app-field-group',
  templateUrl: './field-group.component.html',
  styleUrls: ['./field-group.component.css']
})
export class FieldGroupComponent implements OnInit {

  constructor(private fieldGroupService:FieldGroupService, private spinner: NgxSpinnerService,) { }

  fieldGroups : FieldGroup[] = [];
  fieldGroup : FieldGroup = new FieldGroup();

  fieldGroupFC: FormControl = new FormControl("")

  loading;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.fieldGroupService.getAll().subscribe(data => {
      this.spinner.hide();
      this.loading = false;
      //data = this.convertKeysToLowerCase(data);
      this.fieldGroups = data;
    }, error =>{
      this.spinner.hide();
    })
  }

  getFieldGroup(id){
    this.fieldGroup = this.fieldGroups.find(fieldGroup => fieldGroup.dbId == id);
    console.log(this.fieldGroup)
  }

  editFieldGroup(){
    var object = Object.assign({}, this.fieldGroup);
    this.fieldGroupService.editFieldGroup(object).subscribe(res => {
      console.log(res);
      this.fieldGroupService.getAll().subscribe(data => {
        data = this.convertKeysToLowerCase(data);
        this.fieldGroups = data;
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

  displayFnFieldGroup(fieldGroup : FieldGroup): string {
    return fieldGroup && fieldGroup.Id + " - " + fieldGroup.Name;
  }

}
