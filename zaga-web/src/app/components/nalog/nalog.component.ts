import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { WorkOrderInfo } from 'src/app/models/WorkOrderInfo';

@Component({
  selector: 'app-nalog',
  templateUrl: './nalog.component.html',
  styleUrls: ['./nalog.component.css']
})
export class NalogComponent implements OnInit {

  constructor(private router: Router) { }

  workOrderInfo :WorkOrderInfo = new WorkOrderInfo();

  ngOnInit() {
  }

  nextPage() {
    this.workOrderInfo.start = this.workOrderInfo.start.day + '.' 
                              + this.workOrderInfo.start.month + '.' 
                              + this.workOrderInfo.start.year;
    this.workOrderInfo.end = this.workOrderInfo.end.day + '.' 
                              + this.workOrderInfo.end.month + '.' 
                              + this.workOrderInfo.end.year;
    localStorage["workOrder"] = JSON.stringify(this.workOrderInfo);
    this.router.navigate(['/create/workOrder/new']);
  }

  onItemChange(value) {
    this.workOrderInfo.status = value;
  }

}
