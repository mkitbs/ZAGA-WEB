import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
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
  MatSnackBar,
  MatTooltipModule,
  yearsPerPage,
  MatSelectModule,
  MatDialogContent,
  MatDialogActions,
  MatDialog,
  MatDialogModule
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
import { ReportMaterialComponent } from './components/report-material/report-material.component';
import { ReportMachineComponent } from './components/report-machine/report-machine.component';
import { ReportEmployeeComponent } from './components/report-employee/report-employee.component';
import { SearchOperationsPipe } from './pipes/search-operations.pipe';
import { SearchFieldsPipe } from './pipes/search-fields.pipe';
import { SearchCulturesPipe } from './pipes/search-cultures.pipe';
import { SearchPropulsionMachinesPipe } from './pipes/search-propulsion-machines.pipe';
import { SearchCouplingMachinesPipe } from './pipes/search-coupling-machines.pipe';
import { SearchMaterialsPipe } from './pipes/search-materials.pipe';
import { ReportMaterialMaterialPipe } from './pipes/report-material-material.pipe';
import { ReportMaterialSapIdPipe } from './pipes/report-material-sap-id.pipe';
import { ReportEmployeeWorkerPipe } from './pipes/report-employee-worker.pipe';
import { ReportMachineMachinePipe } from './pipes/report-machine-machine.pipe';
import { SearchWoBetweenDatesPipe } from './pipes/search-wo-between-dates.pipe';
import { SearchMaterialUnitPipe } from './pipes/search-material-unit.pipe';
import { SearchMachineCouplingPipe } from './pipes/search-machine-coupling.pipe';
import { SearchEmployeePositionPipe } from './pipes/search-employee-position.pipe';
import { SearchCropFieldPipe } from './pipes/search-crop-field.pipe';
import { SearchCropCulturePipe } from './pipes/search-crop-culture.pipe';
import { SearchCulturePipe } from './pipes/search-culture.pipe';
import { SearchCropPipe } from './pipes/search-crop.pipe';
import { SerachMachineTypePipe } from './pipes/serach-machine-type.pipe';
import { SerachMachineGroupPipe } from './pipes/serach-machine-group.pipe';
import { SearchMachineMachineGroupPipe } from './pipes/search-machine-machine-group.pipe';
import { SearchMachinePipe } from './pipes/search-machine.pipe';
import { SearchFieldGroupPipe } from './pipes/search-field-group.pipe';
import { SearchOperationGroupPipe } from './pipes/search-operation-group.pipe';
import { SearchOperationTypePipe } from './pipes/search-operation-type.pipe';
import { SearchOperationOperationGroupPipe } from './pipes/search-operation-operation-group.pipe';
import { SearchCultureGroupPipe } from './pipes/search-culture-group.pipe';
import { SearchCultureCultureTypePipe } from './pipes/search-culture-culture-type.pipe';
import { SearchCultureProductionTypePipe } from './pipes/search-culture-production-type.pipe';
import { SearchCultureOrgConPipe } from './pipes/search-culture-org-con.pipe';
import { SearchCultureTypePipe } from './pipes/search-culture-type.pipe';
import { SearchCultureCultureGroupPipe } from './pipes/search-culture-culture-group.pipe';
import { SearchVarietyPipe } from './pipes/search-variety.pipe';
import { SearchVarietyCultureGroupPipe } from './pipes/search-variety-culture-group.pipe';
import { LoginComponent } from './components/login/login.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { AuthGuardGuard } from './service/auth-guard.guard';
import { AuthInterceptor } from './service/auth/auth';
import { SettingsComponent } from './components/settings/settings.component';
import { MatTabsModule, MatIconModule } from '@angular/material';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AdminGuardGuard } from "./service/admin-guard.guard"
import { CookieService } from 'ngx-cookie-service';
import { WorkOrderTractorDriverComponent } from './components/work-order-tractor-driver/work-order-tractor-driver.component';
import { NavbarTractorDriverComponent } from './components/navbar-tractor-driver/navbar-tractor-driver.component';
import { TimeTrackingComponent } from "./components/time-tracking/time-tracking.component";
import { CdTimerModule } from "angular-cd-timer";
import { PasswordResetComponent } from './components/password-reset/password-reset.component';
import { SearchWorkOrderNumberPipe } from './pipes/search-work-order-number.pipe';
import { SearchWorkOrderResponsiblePipe } from './pipes/search-work-order-responsible.pipe';
import { SearchWoStatusPipe } from './pipes/search-wo-status.pipe'
import { LeavePageGuard } from "./service/leave-page.guard";
import { LeavePageConfirmationComponent } from './components/leave-page-confirmation/leave-page-confirmation.component';
import { WorkOrderReportsComponent } from './components/work-order-reports/work-order-reports.component';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { ReportAtmComponent } from './components/report-atm/report-atm.component';
import { SearchMachineSapIdPipe } from './pipes/search-machine-sap-id.pipe';
import { SearchMachineDatesPipe } from './pipes/search-machine-dates.pipe';
import { SearchMachineDatePipe } from './pipes/search-machine-date.pipe';
import { SearchWorkOrderDayPipe } from './pipes/search-work-order-day.pipe';
import { SearchWorkOrderAtmPipe } from './pipes/search-work-order-atm.pipe';
import { SearchWorkOrderCropPipe } from './pipes/search-work-order-crop.pipe';
import { SearchWorkOrderFieldPipe } from './pipes/search-work-order-field.pipe';

declare var require: any;
var config = require("config");

const routes: Routes = [
  { path: "create/workOrder/:workId", component: CreateworkOrderComponent, canActivate: [AuthGuardGuard], canDeactivate: [LeavePageGuard] },
  { path: "nalog", component: NalogComponent, canActivate: [AuthGuardGuard] },
  { path: "login", component: LoginComponent },
  { path: "workOrder", component: WorkOrderComponent, canActivate: [AuthGuardGuard] },
  { path: "close/workOrder/:workId", component: CloseWorkOrderComponent, canActivate: [AuthGuardGuard] },
  { path: "yieldOverview", component: YieldComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/machine", component: MachineComponent, canActivate: [AuthGuardGuard] },
  { path: "workOrder/:urlParam", component: WorkOrderComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/field", component: FieldComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/crop", component: CropComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/employee", component: EmployeeComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/fieldGroup", component: FieldGroupComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/machineGroup", component: MachineGroupComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/operationGroup", component: OperationGroupComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/operation", component: OperationComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/cultureGroup", component: CultureGroupComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/culture", component: CultureComponent, canActivate: [AuthGuardGuard] },
  { path: "masterData/variety", component: VarietyComponent, canActivate: [AuthGuardGuard] },
  { path: "report/material", component: ReportMaterialComponent, canActivate: [AuthGuardGuard] },
  { path: "report/machine", component: ReportMachineComponent, canActivate: [AuthGuardGuard] },
  { path: "report/employee", component: ReportEmployeeComponent, canActivate: [AuthGuardGuard] },
  { path: "settings", component: SettingsComponent, canActivate: [AdminGuardGuard] },
  { path: "workOrderTractorDriver", component: WorkOrderTractorDriverComponent, canActivate: [AuthGuardGuard] },
  { path: "timeTracking/:id", component: TimeTrackingComponent },
  { path: "passwordReset/:token", component: PasswordResetComponent },
  { path: "workOrderReports", component: WorkOrderReportsComponent, canActivate: [AuthGuardGuard] },
  { path: "report/atm", component: ReportAtmComponent, canActivate: [AuthGuardGuard] },
  { path: "404", component: NotFoundComponent },
  { path: "", component: HomeComponent, canActivate: [AuthGuardGuard] },
  { path: "**", component: NotFoundComponent }
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
    ReportMaterialComponent,
    ReportMachineComponent,
    ReportEmployeeComponent,
    SearchOperationsPipe,
    SearchFieldsPipe,
    SearchCulturesPipe,
    SearchPropulsionMachinesPipe,
    SearchCouplingMachinesPipe,
    SearchMaterialsPipe,
    ReportMaterialMaterialPipe,
    ReportMaterialSapIdPipe,
    ReportEmployeeWorkerPipe,
    ReportMachineMachinePipe,
    SearchWoBetweenDatesPipe,
    SearchMaterialUnitPipe,
    SearchMachineCouplingPipe,
    SearchEmployeePositionPipe,
    SearchCropFieldPipe,
    SearchCropCulturePipe,
    SearchCulturePipe,
    SearchCropPipe,
    SerachMachineTypePipe,
    SerachMachineGroupPipe,
    SearchMachineMachineGroupPipe,
    SearchMachinePipe,
    SearchFieldGroupPipe,
    SearchOperationGroupPipe,
    SearchOperationTypePipe,
    SearchOperationOperationGroupPipe,
    SearchCultureGroupPipe,
    SearchCultureCultureTypePipe,
    SearchCultureProductionTypePipe,
    SearchCultureOrgConPipe,
    SearchCultureTypePipe,
    SearchCultureCultureGroupPipe,
    SearchVarietyPipe,
    SearchVarietyCultureGroupPipe,
    LoginComponent,
    NotFoundComponent,
    SettingsComponent,
    WorkOrderTractorDriverComponent,
    NavbarTractorDriverComponent,
    TimeTrackingComponent,
    PasswordResetComponent,
    SearchWorkOrderNumberPipe,
    SearchWorkOrderResponsiblePipe,
    SearchWoStatusPipe,
    LeavePageConfirmationComponent,
    WorkOrderReportsComponent,
    ReportAtmComponent,
    SearchMachineSapIdPipe,
    SearchMachineDatesPipe,
    SearchMachineDatePipe,
    SearchWorkOrderDayPipe,
    SearchWorkOrderAtmPipe,
    SearchWorkOrderCropPipe,
    SearchWorkOrderFieldPipe,
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
    NgMultiSelectDropDownModule.forRoot(),
    ReactiveFormsModule,
    NgbPaginationModule,
    MatTooltipModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatTabsModule,
    MatIconModule,
    FormsModule,
    ChartsModule,
    CdTimerModule,
    MatSelectModule,
    MatDialogModule,
    AgmCoreModule.forRoot({
      apiKey: "AIzaSyD160yNHv43GMMRFiNI7G5dyNA4e5nchug",
      libraries: ["drawing", "geometry", "places"],
    }),
  ],
  entryComponents: [
    LeavePageConfirmationComponent
  ],
  providers: [
    ThemeService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    MatSnackBar,
    DeviceDetectorService,
    CookieService,
    LeavePageGuard,
    MatDialog,
    { provide: NgbDateParserFormatter, useClass: NgbDateParser },
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }

//AIzaSyD160yNHv43GMMRFiNI7G5dyNA4e5nchug   
//AIzaSyCYhI5DKGX1t8Oref8HdsUXWsA9a25NXCg //devMode