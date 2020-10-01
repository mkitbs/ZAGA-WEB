import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { WorkOrder } from "src/app/models/WorkOrder";

@Component({
  selector: "app-nalog",
  templateUrl: "./nalog.component.html",
  styleUrls: ["./nalog.component.css"],
})
export class NalogComponent implements OnInit {
  constructor(private router: Router) {}

  workOrderInfo: WorkOrder = new WorkOrder();

  ngOnInit() {}

  nextPage() {
    this.workOrderInfo.date =
      this.workOrderInfo.date.day +
      "." +
      this.workOrderInfo.date.month +
      "." +
      this.workOrderInfo.date.year;
    localStorage["workOrder"] = JSON.stringify(this.workOrderInfo);
    this.router.navigate(["/create/workOrder/new"]);
  }

  onItemChange(value) {
    this.workOrderInfo.status = value;
  }
}
