import { Component, OnInit } from "@angular/core";
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
    private cultureGroupService: CultureGroupService
  ) {}

  crops: Crop[] = [];
  culutres: Culture[] = [];
  fields: Field[] = [];
  crop: Crop = new Crop();
  cultureGroups: CultureGroup[] = [];

  selectedTable;
  selectedCulture;

  ngOnInit() {
    this.cultureService.getAll().subscribe((data) => {
      data = this.convertKeysToLowerCase(data);
      this.culutres = data;
      this.cultureGroupService.getAll().subscribe((data) => {
        data = this.convertKeysToLowerCase(data);
        this.cultureGroups = data;
        this.culutres.forEach((culture) => {
          culture.culturegroup = this.cultureGroups.find(
            (x) => x.dbid == culture.culturegroup
          ).name;
        });
      });
    });
    this.fieldService.getAll().subscribe((data) => {
      data = this.convertKeysToLowerCase(data);
      this.fields = data;
      console.log(this.fields);
      this.cropService.getAll().subscribe((data) => {
        this.crops = data;
        console.log(this.crops);
        this.crops.forEach((crop) => {
          crop.cultureName = this.culutres.find(
            (x) => x.dbid == crop.cultureId
          ).name;
          crop.field = this.fields.find((x) => x.dbid == crop.fieldId).name;
        });
      });
    });
  }

  //method for filters
  getCrops() {
    console.log(this.selectedTable);
    if (
      //izabrani su i tabla i kultura
      this.selectedCulture != undefined &&
      this.selectedTable != undefined &&
      this.selectedCulture != -1 &&
      this.selectedTable != -1
    ) {
      this.cropService
        .getAllByFieldAndCulture(this.selectedTable, this.selectedCulture)
        .subscribe((data) => {
          console.log(data);
          this.crops = data;
          this.crops.forEach((crop) => {
            crop.cultureName = this.culutres.find(
              (x) => x.dbid == crop.cultureId
            ).name;
            crop.field = this.fields.find((x) => x.dbid == crop.fieldId).name;
          });
        });
    } else if (
      //nije izabrano nista, odnosono ponisteni su prethodni izbori
      (this.selectedTable == -1 || this.selectedTable == undefined) &&
      (this.selectedCulture == -1 || this.selectedCulture == undefined)
    ) {
      this.cropService.getAll().subscribe((data) => {
        this.crops = data;
        this.crops.forEach((crop) => {
          crop.cultureName = this.culutres.find(
            (x) => x.dbid == crop.cultureId
          ).name;
          crop.field = this.fields.find((x) => x.dbid == crop.fieldId).name;
        });
      });
    } else if (
      //izabrana je samo kultura
      (this.selectedTable == undefined || this.selectedTable == -1) &&
      (this.selectedCulture != undefined || this.selectedCulture != -1)
    ) {
      console.log(this.selectedTable);
      this.cropService
        .getAllByCulture(this.selectedCulture)
        .subscribe((data) => {
          this.crops = data;
          this.crops.forEach((crop) => {
            crop.cultureName = this.culutres.find(
              (x) => x.dbid == crop.cultureId
            ).name;
            crop.field = this.fields.find((x) => x.dbid == crop.fieldId).name;
          });
        });
    } else if (
      //izabrana je samo tabla
      (this.selectedCulture == undefined || this.selectedCulture == -1) &&
      (this.selectedTable != undefined || this.selectedTable != -1)
    ) {
      console.log(this.selectedTable);
      this.cropService.getAllByField(this.selectedTable).subscribe((data) => {
        this.crops = data;
        console.log(this.crops);
        this.crops.forEach((crop) => {
          crop.cultureName = this.culutres.find(
            (x) => x.dbid == crop.cultureId
          ).name;
          crop.field = this.fields.find((x) => x.dbid == crop.fieldId).name;
        });
      });
    }
  }

  getCropForEdit(id) {
    this.crop = this.crops.find((x) => x.id == id);
  }

  editCrop() {
    console.log(this.crop);
    this.cropService.updateCrop(this.crop).subscribe((res) => {
      console.log(res);
      this.cropService.getAll().subscribe((data) => {
        this.crops = data;
        this.crops.forEach((crop) => {
          crop.cultureName = this.culutres.find(
            (x) => x.dbid == crop.cultureId
          ).name;
          crop.field = this.fields.find((x) => x.dbid == crop.fieldId).name;
        });
      });
    });
  }

  setValueForSelectedTable() {
    this.selectedTable = -1;
    this.getCrops();
  }

  setValueForSelectedCulture() {
    this.selectedCulture = -1;
    this.getCrops();
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
