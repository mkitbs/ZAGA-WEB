import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { WorkOrderService } from "src/app/service/work-order.service";
import { WorkOrder } from "src/app/models/WorkOrder";
import { NgxSpinnerService } from "ngx-spinner";
import { AllWorkOrdersResponseDTO } from "src/app/models/AllWorkOrdersResponseDTO";
import { HttpParams } from "@angular/common/http";

@Component({
  selector: "app-workOrder",
  templateUrl: "./workOrder.component.html",
  styleUrls: ["./workOrder.component.css"],
})
export class WorkOrderComponent implements OnInit {
  @ViewChild('openFilters', null) openFilters: ElementRef<HTMLElement>;
  
  click = false;
  collapseBool = true;
  empty = false;

  page = 1;
  pageSize = 10;
  workOrders: WorkOrder[] = [];
  allWOResponse: AllWorkOrdersResponseDTO[] = [];
  workOrder: WorkOrder = new WorkOrder();
  copyDate = new Date();

  workOrderId;
  desc = false;
  copyWorkOrderId;
  model;

  urlParam = this.route.snapshot.params.urlParam;

  my = true;
  woSapId;
  loading;
  status: any[] = [];
  dateOfCreateWO;

  dateFilter;

  paramSapId;
  paramDate;
  paramField;
  paramCrop;
  paramAtm;
  paramResponsible;
  paramOwnership;
  paramWoStatus;

  filterSapNumber;
  filterWorkOrderDate;
  filterField;
  filterCrop;
  filterAtm;
  filterResponsible;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private workOrderService: WorkOrderService,
    private spinner: NgxSpinnerService
  ) { 
    this.route.queryParams.subscribe(params => {
      this.paramSapId = params['sapId'];
      this.paramDate = params['date'];
      this.paramField = params['field'];
      this.paramCrop = params['crop'];
      this.paramAtm = params['atm'];
      this.paramResponsible = params['responsible'];
      this.paramOwnership = params['ownership'];
      this.paramWoStatus = params['woStatus'];
  });

  }

  ngOnInit() {
    console.log(localStorage.getItem("route"))
    
      if (!localStorage.getItem('foo')) { 
        localStorage.setItem('foo', 'no reload') 
        location.reload() 
      } else {
        localStorage.removeItem('foo') 
      }
      
    
    
    if (this.urlParam == "closing") {
      this.getAllWorkOrdersByStatus("IN_PROGRESS");
    } else {
      if(this.paramOwnership == "my") {
        if(this.paramWoStatus != undefined && this.paramWoStatus != "") {
          this.getMyWorkOrdersByStatus(this.paramWoStatus);
        } else {
          this.getMyWorkOrders();
        }
        
      } else if (this.paramOwnership == "all") {
        if(this.paramWoStatus != undefined && this.paramWoStatus != "") {
          this.getAllWorkOrdersByStatus(this.paramWoStatus);
        } else {
          this.getAll();
        }
        
      } else {
        this.getMyWorkOrders();
      }
      
    }
    this.status.push(
      { name: "Novi", name2send: "NEW" },
      { name: "U radu", name2send: "IN_PROGRESS" },
      { name: "Zatvoreni", name2send: "CLOSED" },
      { name: "Stornirani", name2send: "CANCELLATION" }
    )

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

  onChangeSapId(id) {
    const queryParams: Params = { sapId: id };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
        skipLocationChange: false
        
      });
      
  }

  onChangeDate(day) {
    const queryParams: Params = { date: day };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
  }

  onChangeField(table) {
    const queryParams: Params = { field: table };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
  }

  onChangeCrop(cropname) {
    const queryParams: Params = { crop: cropname };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
  }

  onChangeAtm(operation) {
    const queryParams: Params = { atm: operation };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
  }

  onChangeResponsible(person) {
    const queryParams: Params = { responsible: person };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
  }

  sortBySapId() {
    this.desc = !this.desc;
    if (this.desc) {
      this.allWOResponse.sort((w1, w2) => w1.sapId - w2.sapId);
    } else {
      this.allWOResponse.sort((w1, w2) => w2.sapId - w1.sapId);
    }


  }

  getDate(value) {
    if (value == "tomorrow") {
      const today = new Date();
      this.copyDate = new Date(today.setDate(today.getDate() + 1));
    } else if (value == "today") {
      this.copyDate = new Date();
    } else {
      console.log(value)
      if (value != undefined) {
        const someDate = value.year + "-" + value.month + "-" + value.day;
        this.copyDate = new Date(someDate);
      }
    }
  }

  getWorkOrderId(wo) {
    this.workOrder = wo;
    this.workOrderId = wo.id;
    this.woSapId = wo.sapId;
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
    this.spinner.show();
    this.workOrderService.getAllByStatus(status).subscribe((res) => {
      this.spinner.hide();
      this.allWOResponse = res;

      if (this.allWOResponse.length == 0) {
        this.empty = true;
      } else {
        this.empty = false;
        if(this.paramSapId != undefined && this.paramSapId != "") {
          this.filterSapNumber = this.paramSapId;
        }
        if (this.paramDate != undefined && this.paramDate != ""){
          this.filterWorkOrderDate = this.paramDate;
        }
        if (this.paramField != undefined && this.paramField != ""){
          this.filterField = this.paramField;
        }
        if (this.paramCrop != undefined && this.paramCrop != ""){
          this.filterCrop = this.paramCrop;
        }
        if (this.paramAtm != undefined && this.paramAtm != ""){
          this.filterAtm = this.paramAtm;
        }
        if (this.paramResponsible != undefined && this.paramResponsible != ""){
          this.filterResponsible = this.paramResponsible;
        }
        
        if((this.paramSapId != undefined && this.paramSapId != "") 
            || (this.paramDate != undefined && this.paramDate != "") 
            || (this.paramField != undefined && this.paramField != "") 
            || (this.paramCrop != undefined && this.paramCrop != "") 
            || (this.paramAtm != undefined && this.paramAtm != "") 
            || (this.paramResponsible != undefined && this.paramResponsible != "")) {
              this.openFilters.nativeElement.click();
        }
      }
      this.allWOResponse.sort((w1, w2) => w2.sapId - w1.sapId);
    }, error => {
      this.spinner.hide();
    });
    const queryParams: Params = { woStatus: status };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
  }

  getAll() {
    this.my = false;
    this.loading = true;
    this.spinner.show();
    this.workOrderService.getAll().subscribe((data) => {
      this.loading = false;
      this.spinner.hide();
      this.allWOResponse = data;
      console.log(this.allWOResponse);
      if (this.allWOResponse.length == 0) {
        this.empty = true;
      } else {
        this.empty = false;
        if(this.paramSapId != undefined && this.paramSapId != "") {
          this.filterSapNumber = this.paramSapId;
        }
        if (this.paramDate != undefined && this.paramDate != ""){
          this.filterWorkOrderDate = this.paramDate;
        }
        if (this.paramField != undefined && this.paramField != ""){
          this.filterField = this.paramField;
        }
        if (this.paramCrop != undefined && this.paramCrop != ""){
          this.filterCrop = this.paramCrop;
        }
        if (this.paramAtm != undefined && this.paramAtm != ""){
          this.filterAtm = this.paramAtm;
        }
        if (this.paramResponsible != undefined && this.paramResponsible != ""){
          this.filterResponsible = this.paramResponsible;
        }

        if((this.paramSapId != undefined && this.paramSapId != "") 
            || (this.paramDate != undefined && this.paramDate != "") 
            || (this.paramField != undefined && this.paramField != "") 
            || (this.paramCrop != undefined && this.paramCrop != "") 
            || (this.paramAtm != undefined && this.paramAtm != "") 
            || (this.paramResponsible != undefined && this.paramResponsible != "")) {
              this.openFilters.nativeElement.click();
        }
      }
      this.allWOResponse.sort((w1, w2) => w2.sapId - w1.sapId);
    }, error => {
      this.spinner.hide();
    });
    
    const queryParams: Params = { ownership: 'all' };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
      
  }

  getMyWorkOrders() {
    this.my = true;
    this.loading = true;
    this.spinner.show();
    this.workOrderService.getMyWorkOrders().subscribe(data => {
      this.loading = false;
      this.spinner.hide();
      this.allWOResponse = data;
      console.log(this.allWOResponse)
      if (this.allWOResponse.length == 0) {
        this.empty = true;
      } else {
        this.empty = false;
        if(this.paramSapId != undefined && this.paramSapId != "") {
          this.filterSapNumber = this.paramSapId;
        }
        if (this.paramDate != undefined && this.paramDate != ""){
          this.filterWorkOrderDate = this.paramDate;
        }
        if (this.paramField != undefined && this.paramField != ""){
          this.filterField = this.paramField;
        }
        if (this.paramCrop != undefined && this.paramCrop != ""){
          this.filterCrop = this.paramCrop;
        }
        if (this.paramAtm != undefined && this.paramAtm != ""){
          this.filterAtm = this.paramAtm;
        }
        if (this.paramResponsible != undefined && this.paramResponsible != ""){
          this.filterResponsible = this.paramResponsible;
        }
        
        if((this.paramSapId != undefined && this.paramSapId != "") 
            || (this.paramDate != undefined && this.paramDate != "") 
            || (this.paramField != undefined && this.paramField != "") 
            || (this.paramCrop != undefined && this.paramCrop != "") 
            || (this.paramAtm != undefined && this.paramAtm != "") 
            || (this.paramResponsible != undefined && this.paramResponsible != "")) {
              this.openFilters.nativeElement.click();
        }
      }
      
      this.allWOResponse.sort((w1, w2) => w2.sapId - w1.sapId);
    }, error => {
      this.spinner.hide();
    })
    const queryParams: Params = { ownership: 'my' };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
  }

  getMyWorkOrdersByStatus(status) {
    this.spinner.show();
    this.workOrderService.getMyByStatus(status).subscribe((res) => {
      this.spinner.hide();
      this.allWOResponse = res;

      if (this.allWOResponse.length == 0) {
        this.empty = true;
      } else {
        this.empty = false;
        if(this.paramSapId != undefined && this.paramSapId != "") {
          this.filterSapNumber = this.paramSapId;
        }
        if (this.paramDate != undefined && this.paramDate != ""){
          this.filterWorkOrderDate = this.paramDate;
        }
        if (this.paramField != undefined && this.paramField != ""){
          this.filterField = this.paramField;
        }
        if (this.paramCrop != undefined && this.paramCrop != ""){
          this.filterCrop = this.paramCrop;
        }
        if (this.paramAtm != undefined && this.paramAtm != ""){
          this.filterAtm = this.paramAtm;
        }
        if (this.paramResponsible != undefined && this.paramResponsible != ""){
          this.filterResponsible = this.paramResponsible;
        }
        
        if((this.paramSapId != undefined && this.paramSapId != "") 
            || (this.paramDate != undefined && this.paramDate != "") 
            || (this.paramField != undefined && this.paramField != "") 
            || (this.paramCrop != undefined && this.paramCrop != "") 
            || (this.paramAtm != undefined && this.paramAtm != "") 
            || (this.paramResponsible != undefined && this.paramResponsible != "")) {
              this.openFilters.nativeElement.click();
        }
       
      }
      this.allWOResponse.sort((w1, w2) => w2.sapId - w1.sapId);
    }, error => {
      this.spinner.hide();
    });
    const queryParams: Params = { woStatus: status };
    this.router.navigate(
      [], 
      {
        relativeTo: this.route,
        queryParams: queryParams, 
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      });
  }

  displayFnStatus(status: any): string {
    return status.name;
  }

}
