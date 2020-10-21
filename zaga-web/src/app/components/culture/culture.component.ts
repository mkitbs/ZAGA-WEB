import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';
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

  selectedOrgCon;
  selectedCultureType;
  selectedCulture;

  ngOnInit() {
    this.getAll();
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

  getAllByOrgCon(){
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAllByOrgCon(this.selectedOrgCon).subscribe(data => {
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

  getAllByCultureType(){
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAllByCultureType(this.selectedCultureType).subscribe(data => {
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

  getAllByCultureGroup(){
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAllByCultureGroup(this.selectedCulture).subscribe(data => {
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

  getAllByOrgConCultureTypeCultureGroup(){
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAllByOrgConCultureTypeCultureGroup(
        this.selectedOrgCon, this.selectedCultureType, this.selectedCulture
        ).subscribe(data => {
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

  getAllByOrgConAndCultureType(){
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAllByOrgConAndCultureType(this.selectedOrgCon, this.selectedCultureType).subscribe(data => {
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

  getAllByOrgConAndCultureGroup(){
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAllByOrgConAndCultureGroup(this.selectedOrgCon, this.selectedCulture).subscribe(data => {
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

  getAllByCultureTypeAndCultureGroup(){
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
      this.cultureService.getAllByCultureTypeAndCultureGroup(this.selectedCultureType, this.selectedCulture).subscribe(data => {
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

  getCultures(){
    if (
      //izabrani su i tip proizvodnje i tip kulture i grupa kulture
      this.selectedOrgCon != undefined &&
      this.selectedCultureType != undefined &&
      this.selectedCulture != undefined &&
      this.selectedOrgCon != -1 &&
      this.selectedCultureType != -1 &&
      this.selectedCulture != -1 
    ) {
      this.getAllByOrgConCultureTypeCultureGroup();
    } else if (
      //nije izabrano nista, odnosono ponisteni su prethodni izbori
      (this.selectedOrgCon == -1 || this.selectedOrgCon == undefined) &&
      (this.selectedCultureType == -1 || this.selectedCultureType == undefined) &&
      (this.selectedCulture == -1 || this.selectedCulture == undefined)
    ) {
      this.getAll();
    } else if (
      //izabran je samo tip proizvodnje
      (this.selectedCultureType == undefined || this.selectedCultureType == -1) &&
      (this.selectedCulture == undefined || this.selectedCulture == -1) &&
      (this.selectedOrgCon != undefined || this.selectedOrgCon != -1)
    ) {
      this.getAllByOrgCon();
    } else if (
      //izabran je samo tip kulture
      (this.selectedCultureType != undefined || this.selectedCultureType != -1) &&
      (this.selectedCulture == undefined || this.selectedCulture == -1) &&
      (this.selectedOrgCon == undefined || this.selectedOrgCon == -1)
    ) {
      this.getAllByCultureType();
    } else if (
      //izabrana je samo grupa kultura
      (this.selectedCultureType == undefined || this.selectedCultureType == -1) &&
      (this.selectedCulture != undefined || this.selectedCulture != -1) &&
      (this.selectedOrgCon == undefined || this.selectedOrgCon == -1)
    ) {
      this.getAllByCultureGroup();
    } else if (
      //izabran je tip proizvodnje i tip kulture
      (this.selectedCultureType != undefined || this.selectedCultureType != -1) &&
      (this.selectedCulture == undefined || this.selectedCulture == -1) &&
      (this.selectedOrgCon != undefined || this.selectedOrgCon != -1)
    ) {
      this.getAllByOrgConAndCultureType();
    } else if (
      //izabran je tip proizvodnje i grupa kulture
      (this.selectedCultureType == undefined || this.selectedCultureType == -1) &&
      (this.selectedCulture != undefined || this.selectedCulture != -1) &&
      (this.selectedOrgCon != undefined || this.selectedOrgCon != -1)
    ) {
      this.getAllByOrgConAndCultureGroup();
    } else if (
      //izabran je tip kulture i grupa kulture
      (this.selectedCultureType != undefined || this.selectedCultureType != -1) &&
      (this.selectedCulture != undefined || this.selectedCulture != -1) &&
      (this.selectedOrgCon == undefined || this.selectedOrgCon == -1)
    ) {
      this.getAllByCultureTypeAndCultureGroup();
    }
    
  }

  setValueForSelectedOrgCon(){
    this.selectedOrgCon = -1;
    this.getCultures();
  }

  setValueForSelectedCultureType() {
    this.selectedCultureType = -1;
    this.getCultures();
  }

  setValueForSelectedCulture(){
    this.selectedCulture = -1;
    this.getCultures();
  }

}
