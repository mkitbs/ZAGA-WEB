import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { CreateworkOrderComponent } from './components/creatework-order/creatework-order.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { NalogComponent } from './components/nalog/nalog.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbDatepickerModule} from '@ng-bootstrap/ng-bootstrap';
import { WorkOrderComponent } from './components/workOrder/workOrder.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MatTooltipModule } from '@angular/material';
import { CloseWorkOrderComponent } from './components/closework-order/closework-order.component';
import { ToastrModule } from 'ngx-toastr';

const routes: Routes = [
  {path:"", component:HomeComponent},
  {path: "create/workOrder/:workId", component: CreateworkOrderComponent},
  {path:"workOrder", component: WorkOrderComponent},
  {path: "nalog", component: NalogComponent},
  {path: "workOrder", component: WorkOrderComponent},
  {path: "close/workOrder/:workId", component: CloseWorkOrderComponent}

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
    CloseWorkOrderComponent

  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes,{useHash:true}),
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      preventDuplicates: false
    }),
    NgbDatepickerModule,
    ReactiveFormsModule,
    MatTooltipModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
