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


const routes: Routes = [
  {path:"", component:HomeComponent},
  {path: "create/workOrder/:workId", component: CreateworkOrderComponent},
  {path:"workOrder", component: WorkOrderComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CreateworkOrderComponent,
    NavbarComponent,
    NalogComponent,
    WorkOrderComponent

  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes,{useHash:true}),
    BrowserAnimationsModule,
    NgbDatepickerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
