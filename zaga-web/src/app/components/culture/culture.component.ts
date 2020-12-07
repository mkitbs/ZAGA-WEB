import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Culture } from 'src/app/models/Culture';
import { CultureGroup } from 'src/app/models/CultureGroup';
import { CultureGroupService } from 'src/app/service/culture-group.service';
import { CultureService } from 'src/app/service/culture.service';

@Component({
  selector: 'app-culture',
  templateUrl: './culture.component.html',
  styleUrls: ['./culture.component.css']
})
export class CultureComponent implements OnInit {

  constructor(
    private cultureService:CultureService,
    private cultureGroupService:CultureGroupService
  ) { }

  cultures: Culture[] = [];
  cultureGroups: CultureGroup[] = [];
  culture: Culture = new Culture();
  cultureTypes: Culture[] = [];
  productionTypes: Culture[] = [];

  selectedOrgCon;
  selectedCultureType;
  selectedCulture;

  cultureFC: FormControl = new FormControl("");
  cultureTypeFC: FormControl = new FormControl("");
  productionTypeFC: FormControl = new FormControl("");
  cultureGroupFC: FormControl = new FormControl("");

  ngOnInit() {
    this.getAll();
    this.getAllCultureTypes();
    this.getAllProductionTypes();
  }

  getAll(){
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAll().subscribe(data => {
        this.cultures = data;
        this.cultures.forEach(culture => {
          if(culture.Type == "FRUIT"){
            culture.Type = "VOĆE";
          } else if(culture.Type == "VEGETABLE"){
            culture.Type = "POVRĆE";
          } else if(culture.Type == "CROP_FARMING"){
            culture.Type = "RATARSKA";
          } else if(culture.Type == "VITICULTURE"){
            culture.Type = "VINOGRADARSKA";
          }
          if(culture.OrgKon == "ORGANIC"){
            culture.OrgKon = "ORGANSKA";
          } else if(culture.OrgKon == "CONVENTIONAL"){
            culture.OrgKon = "KONVENCIONALNA";
          }
          culture.cultureGroupName = this.cultureGroups.find(x => x.dbId == culture.cultureGroup).Name;
        })
      });
    });
  }

  getCultureForEdit(id){
    this.culture = this.cultures.find(culture => culture.dbId == id);
  }

  editCulture(){
    console.log(this.culture)
    this.cultureService.editCulture(this.culture).subscribe(res => {
     this.getAll();
    })
  }

  getAllCultureTypes(){
    this.cultureService.getAllGroupByCultureType().subscribe(data => {
      this.cultureTypes = data;
      this.cultureTypes.forEach(culture => {
        if(culture.Type == "FRUIT"){
          culture.Type = "VOĆE";
        } else if(culture.Type == "VEGETABLE"){
          culture.Type = "POVRĆE";
        } else if(culture.Type == "CROP_FARMING"){
          culture.Type = "RATARSKA";
        } else if(culture.Type == "VITICULTURE"){
          culture.Type = "VINOGRADARSKA";
        }
      })
    })
  }

  getAllProductionTypes(){
    this.cultureService.getAllGroupByProductionType().subscribe(data => {
      this.productionTypes = data; 
      this.productionTypes.forEach(culture => {
        if(culture.OrgKon == "ORGANIC"){
          culture.OrgKon = "ORGANSKA";
        } else if(culture.OrgKon == "CONVENTIONAL"){
          culture.OrgKon = "KONVENCIONALNA";
        }
      })
    })
  }

  displayFnCulture(culture: Culture): string {
    return culture && culture.Id + " - " + culture.Name;
  }

  displayFnProductionType(culture: Culture): string {
    return culture && culture.OrgKon;
  }

  displayFnCultureType(culture: Culture): string {
    return culture && culture.Type;
  }

  displayFnCultureGroup(cultureGroup: CultureGroup): string {
    return cultureGroup && cultureGroup.Id + " - " + cultureGroup.Name;
  }

}
