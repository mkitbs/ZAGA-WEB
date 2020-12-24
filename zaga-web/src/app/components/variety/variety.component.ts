import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { Culture } from 'src/app/models/Culture';
import { Variety } from 'src/app/models/Variety';
import { CultureService } from 'src/app/service/culture.service';
import { VarietyService } from 'src/app/service/variety.service';

@Component({
  selector: 'app-variety',
  templateUrl: './variety.component.html',
  styleUrls: ['./variety.component.css']
})
export class VarietyComponent implements OnInit {

  constructor(
    private varietyService:VarietyService,
    private cultureService:CultureService,
    private spinner:NgxSpinnerService
    ) { }

  varieties: Variety[] = [];
  cultures: Culture[] = [];
  variety: Variety = new Variety();

  varietyFC: FormControl = new FormControl("");
  cultureFC: FormControl = new FormControl("");

  loading;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.cultureService.getAll().subscribe(data => {
      this.cultures = data;
      this.varietyService.getAll().subscribe(data => {
        this.spinner.hide();
        this.loading = false;
        this.varieties = data;
        this.varieties.forEach(variety => {
          variety.cultureName = this.cultures.find(culture => culture.dbId == variety.culture).Name;
        })
      })
    })
  }

  getVariety(id){
    this.variety = this.varieties.find(variety => variety.dbId == id);
    console.log(this.variety)
  }

  displayFnVariety(variety: Variety): string {
    return variety && variety.Id + " - " + variety.Name;
  }

  displayFnCulture(culture: Culture): string {
    return culture && culture.Id + " - " + culture.Name;
  }

}
