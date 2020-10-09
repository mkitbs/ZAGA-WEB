import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { WorkOrderService } from "src/app/service/work-order.service";
import { WorkOrder } from "src/app/models/WorkOrder";

@Component({
  selector: "app-workOrder",
  templateUrl: "./workOrder.component.html",
  styleUrls: ["./workOrder.component.css"],
})
export class WorkOrderComponent implements OnInit {
  click = false;
  collapseBool = true;
  empty = false;

  workOrders: WorkOrder[] = [];
  copyDate = new Date();

  workOrderId;
  copyWorkOrderId;

  urlParam = this.route.snapshot.params.urlParam;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private workOrderService: WorkOrderService
  ) {}

  ngOnInit() {
    if (this.urlParam == "closing") {
      this.getAllWorkOrdersByStatus("IN_PROGRESS");
    } else {
      this.workOrderService.getAll().subscribe((data) => {
        this.workOrders = data;
        console.log(this.workOrders);
        if (this.workOrders.length == 0) {
          this.empty = true;
        } else {
          this.empty = false;
          this.workOrders.forEach((workOrder) => {
            var date = "";
            date =
              workOrder.date.day.split(" ")[0] +
              "." +
              workOrder.date.month +
              "." +
              workOrder.date.year +
              ".";
            workOrder.date = date;
            if (workOrder.status == "NEW") {
              workOrder.status = "Novi";
            } else if (workOrder.status == "IN_PROGRESS") {
              workOrder.status = "U radu";
            } else if (workOrder.status == "CLOSED") {
              workOrder.status = "Zatvoren";
            }
          });
        }
      });
    }
  }

  changeClick() {
    this.click = true;
  }

  collapse() {
    this.collapseBool = !this.collapseBool;
  }

  changeRoute(id) {
    this.router.navigateByUrl("/create/workOrder/" + id);
  }

  getDate(value) {
    if (value == "tomorrow") {
      const today = new Date();
      this.copyDate = new Date(today.setDate(today.getDate() + 1));
    } else if (value == "today") {
      this.copyDate = new Date();
    } else {
      if (value != undefined) {
        const someDate = value.year + "-" + value.month + "-" + value.day;
        this.copyDate = new Date(someDate);
      }
    }
  }

  getWorkOrderId(id) {
    this.workOrderId = id;
  }

  createWorkOrderCopy() {
    var dateToAdd;
    var day;
    var month;
    if (this.copyDate.getDate() < 10) {
      day = "0" + this.copyDate.getDate();
    } else {
      day = this.copyDate.getDate();
    }
    if (this.copyDate.getMonth() < 9) {
      month = "0" + (this.copyDate.getMonth() + 1);
      console.log(month);
    } else {
      month = this.copyDate.getMonth() + 1;
    }
    dateToAdd = {
      day: day,
      month: month,
      year: this.copyDate.getFullYear(),
    };

    this.workOrderService
      .createWorkOrderCopy(this.workOrderId, dateToAdd)
      .subscribe((res) => {
        this.copyWorkOrderId = res;
        this.router.navigateByUrl("/create/workOrder/" + this.copyWorkOrderId);
      });
  }

  getAllWorkOrdersByStatus(status) {
    this.workOrderService.getAllByStatus(status).subscribe((res) => {
      this.workOrders = res;

      if (this.workOrders.length == 0) {
        this.empty = true;
      } else {
        this.empty = false;
        this.workOrders.forEach((workOrder) => {
          var date = "";
          date =
            workOrder.date.day.split(" ")[0] +
            "." +
            workOrder.date.month +
            "." +
            workOrder.date.year +
            ".";
          workOrder.date = date;
          if (workOrder.status == "NEW") {
            workOrder.status = "Novi";
          } else if (workOrder.status == "IN_PROGRESS") {
            workOrder.status = "U radu";
          } else if (workOrder.status == "CLOSED") {
            workOrder.status = "Zatvoren";
          }
        });
      }
    });
  }
}
