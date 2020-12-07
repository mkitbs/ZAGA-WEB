import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
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
    private cultureGroupService:CultureGroupService
    ) { }

    cultureGroups: CultureGroup[] = [];

    cultureGroupFC: FormControl = new FormControl("");

  ngOnInit() {
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
    })
  }

  displayFnCultureGroup(cultureGroup: CultureGroup): string {
    return cultureGroup && cultureGroup.Id + " - " + cultureGroup.Name;
  }

}
