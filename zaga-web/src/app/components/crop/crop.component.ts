import { Component, OnInit } from "@angular/core";
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from "ngx-spinner";
import { Crop } from "src/app/models/Crop";
import { Culture } from "src/app/models/Culture";
import { CultureGroup } from "src/app/models/CultureGroup";
import { Field } from "src/app/models/Field";
import { CropService } from "src/app/service/crop.service";
import { CultureGroupService } from "src/app/service/culture-group.service";
import { CultureService } from "src/app/service/culture.service";
import { FieldService } from "src/app/service/field.service";

@Component({
  selector: "app-crop",
  templateUrl: "./crop.component.html",
  styleUrls: ["./crop.component.css"],
})
export class CropComponent implements OnInit {
  constructor(
    private cropService: CropService,
    private cultureService: CultureService,
    private fieldService: FieldService,
    private cultureGroupService: CultureGroupService,
    private spinner: NgxSpinnerService
  ) {}

  crops: Crop[] = [];
  cultures: Culture[] = [];
  fields: Field[] = [];
  crop: Crop = new Crop();
  cultureGroups: CultureGroup[] = []; 

  fieldFC: FormControl = new FormControl("");
  cultureFC: FormControl = new FormControl("");
  cropFC: FormControl = new FormControl("");
  loading;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAll().subscribe((data) => {
        this.cultures = data;
        this.cultures.forEach(culture => {
          culture.cultureGroupName = this.cultureGroups.find(cultureGroup => cultureGroup.dbId == culture.cultureGroup).Name;
        }, error => {
          this.spinner.hide();
        })
        console.log(this.cultures)
        this.fieldService.getAll().subscribe(data => {
          this.fields = data;
          this.cropService.getAll().subscribe(data => {
            this.loading = false;
            this.spinner.hide();
            this.crops = data;
            this.crops.forEach(crop => {
              crop.field = this.fields.find(field => field.dbId == crop.fieldId).Name;
              crop.cultureName = this.cultures.find(culture => culture.dbId == crop.cultureId).Name;
            });
          }, error => {
            this.spinner.hide();
          });
        }, error => {
          this.spinner.hide();
        })
      });
    });
  }

  getCropForEdit(id) {
    this.crop = this.crops.find((crop) => crop.Id == id);
  }

  editCrop() {
    this.cropService.updateCrop(this.crop).subscribe((res) => {
      console.log(res);
      this.cropService.getAll().subscribe((data) => {
        this.crops = data;
        this.crops.forEach((crop) => {
          crop.cultureName = this.cultures.find(
            (culture) => culture.dbId == crop.cultureId
          ).Name;
          crop.field = this.fields.find((field) => field.dbId == crop.fieldId).Name;
        });
      });
    });
  }

  displayFnField(field: Field): string {
    return field && field.Id + " - " + field.Name
  }

  displayFnCulture(culture: Culture): string {
    return culture && culture.Id + " - " + culture.Name + "(" + culture.cultureGroupName + ")"
  }

  displayFnCrop(crop: Crop): string {
    return crop && crop.Id + " - " + crop.Name
  }
}
