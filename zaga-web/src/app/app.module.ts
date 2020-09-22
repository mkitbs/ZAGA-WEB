import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";
import { Routes, RouterModule } from "@angular/router";

import { AppComponent } from "./app.component";
import { HomeComponent } from "./components/home/home.component";
import { CreateworkOrderComponent } from "./components/creatework-order/creatework-order.component";
import { NavbarComponent } from "./components/navbar/navbar.component";
import { NalogComponent } from "./components/nalog/nalog.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgbDatepickerModule } from "@ng-bootstrap/ng-bootstrap";
import { WorkOrderComponent } from "./components/workOrder/workOrder.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import {
  MatAutocomplete,
  MatAutocompleteModule,
  MatFormFieldModule,
  MatInputModule,
  MatTooltipModule,
  yearsPerPage,
} from "@angular/material";
import { CloseWorkOrderComponent } from "./components/closework-order/closework-order.component";
import { ToastrModule } from "ngx-toastr";
import { ChartsModule, ThemeService } from "ng2-charts";
import { YieldComponent } from "./components/yield/yield.component";
import { AgmCoreModule } from "@agm/core";
import "../../node_modules/chartjs-plugin-datalabels/dist/chartjs-plugin-datalabels.js";
import "chartjs-plugin-labels";
import { SearchEmployeesPipe } from "./pipes/search-employees.pipe";
import { DeviceDetectorService } from "ngx-device-detector";
import { SearchEmployeesPhonePipe } from "./pipes/search-employees-phone.pipe";

declare var require: any;
var config = require("config");

const routes: Routes = [
  { path: "create/workOrder/:workId", component: CreateworkOrderComponent },
  { path: "nalog", component: NalogComponent },
  { path: "workOrder", component: WorkOrderComponent },
  { path: "close/workOrder/:workId", component: CloseWorkOrderComponent },
  { path: "yieldOverview", component: YieldComponent },
  { path: "", component: HomeComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CreateworkOrderComponent,
    NavbarComponent,
    NalogComponent,
    WorkOrderComponent,
    DashboardComponent,
    CloseWorkOrderComponent,
    YieldComponent,
    SearchEmployeesPipe,
    SearchEmployeesPhonePipe,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes, { useHash: true }),
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      preventDuplicates: false,
    }),
    NgbDatepickerModule,
    ReactiveFormsModule,
    MatTooltipModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ChartsModule,
    AgmCoreModule.forRoot({
      apiKey: "${API_KEY}",
      libraries: ["drawing"],
    }),
  ],
  providers: [ThemeService, DeviceDetectorService],
  bootstrap: [AppComponent],
})
export class AppModule {}
