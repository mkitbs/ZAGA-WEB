import { Component, OnInit } from '@angular/core';
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

  ngOnInit() {
    this.cultureGroupService.getAll().subscribe(data => {
      this.cultureGroups = data;
    })
  }

}
