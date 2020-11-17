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
import {
  NgbDateParserFormatter,
  NgbDatepickerModule,
} from "@ng-bootstrap/ng-bootstrap";
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
import { MachineComponent } from './components/machine/machine.component';
import { NgbDateParser } from "./config/NgbDateParser";
import { FieldComponent } from "./components/field/field.component";
import { CropComponent } from "./components/crop/crop.component";
import { EmployeeComponent } from './components/employee/employee.component';
import { FieldGroupComponent } from './components/field-group/field-group.component';
import { MachineGroupComponent } from './components/machine-group/machine-group.component';
import { OperationGroupComponent } from './components/operation-group/operation-group.component';
import { OperationComponent } from './components/operation/operation.component';
import { CultureGroupComponent } from './components/culture-group/culture-group.component';
import { CultureComponent } from './components/culture/culture.component';
import { VarietyComponent } from './components/variety/variety.component';
import { NgxSpinnerModule } from 'ngx-spinner';
import { SearchEmployeesPositionPipe } from './pipes/search-employees-position.pipe';
import { SearchWorkOrdersTablePipe } from './pipes/search-work-orders-table.pipe';
import { SearchWorkOrdersDatePipe } from './pipes/search-work-orders-date.pipe';
import { SearchWorkOrdersCropPipe } from './pipes/search-work-orders-crop.pipe';
import { SearchWorkOrdersOperationPipe } from './pipes/search-work-orders-operation.pipe';

declare var require: any;
var config = require("config");

const routes: Routes = [
  { path: "create/workOrder/:workId", component: CreateworkOrderComponent },
  { path: "nalog", component: NalogComponent },
  { path: "workOrder", component: WorkOrderComponent },
  { path: "close/workOrder/:workId", component: CloseWorkOrderComponent },
  { path: "yieldOverview", component: YieldComponent },
  { path: "masterData/machine", component: MachineComponent },
  { path: "workOrder/:urlParam", component: WorkOrderComponent },
  { path: "masterData/field", component: FieldComponent },
  { path: "masterData/crop", component: CropComponent },
  { path: "masterData/employee", component: EmployeeComponent },
  { path: "masterData/fieldGroup", component: FieldGroupComponent },
  { path: "masterData/machineGroup", component: MachineGroupComponent },
  { path: "masterData/operationGroup", component: OperationGroupComponent },
  { path: "masterData/operation", component: OperationComponent },
  { path: "masterData/cultureGroup", component: CultureGroupComponent },
  { path: "masterData/culture", component: CultureComponent },
  { path: "masterData/variety", component: VarietyComponent },
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
    MachineComponent,
    FieldComponent,
    CropComponent,
    EmployeeComponent,
    FieldGroupComponent,
    MachineGroupComponent,
    OperationGroupComponent,
    OperationComponent,
    CultureGroupComponent,
    CultureComponent,
    VarietyComponent,
    SearchEmployeesPositionPipe,
    SearchWorkOrdersTablePipe,
    SearchWorkOrdersDatePipe,
    SearchWorkOrdersCropPipe,
    SearchWorkOrdersOperationPipe,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgxSpinnerModule,
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
      apiKey: "AIzaSyD160yNHv43GMMRFiNI7G5dyNA4e5nchug",
      libraries: ["drawing"],
    }),
  ],
  providers: [
    ThemeService,
    DeviceDetectorService,
    { provide: NgbDateParserFormatter, useClass: NgbDateParser },
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
