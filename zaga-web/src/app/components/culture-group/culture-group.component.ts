import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { Culture } from 'src/app/models/Culture';
import { CultureGroup } from 'src/app/models/CultureGroup';
import { CultureGroupService } from 'src/app/service/culture-group.service';

@Component({
  selector: 'app-culture-group',
  templateUrl: './culture-group.component.html',
  styleUrls: ['./culture-group.component.css']
})
export class CultureGroupComponent implements OnInit {

  constructor(
    private cultureGroupService:CultureGroupService,
    private spinner: NgxSpinnerService,
    ) { }

    cultureGroups: CultureGroup[] = [];

    cultureGroupFC: FormControl = new FormControl("");

    loading;

  ngOnInit() {
    this.spinner.show();
    this.loading = true;
    this.cultureGroupService.getAll().subscribe(data => {
      this.spinner.hide();
      this.loading = false;
      this.cultureGroups = data;
    }, error =>{
      this.spinner.hide();
    })
  }

  displayFnCultureGroup(cultureGroup: CultureGroup): string {
    return cultureGroup && cultureGroup.Id + " - " + cultureGroup.Name;
  }

}
