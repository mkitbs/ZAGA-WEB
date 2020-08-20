import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { CreateworkOrderComponent } from './components/creatework-order/creatework-order.component';
import { NavbarComponent } from './components/navbar/navbar.component';

const routes: Routes = [
  {path:"", component:HomeComponent},
  {path: "create/workOrder", component: CreateworkOrderComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CreateworkOrderComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes,{useHash:true}),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
