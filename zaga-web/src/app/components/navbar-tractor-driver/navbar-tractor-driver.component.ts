import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from 'src/app/service/auth/auth.service';

@Component({
  selector: 'app-navbar-tractor-driver',
  templateUrl: './navbar-tractor-driver.component.html',
  styleUrls: ['./navbar-tractor-driver.component.css']
})
export class NavbarTractorDriverComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private cookieService: CookieService
  ) { }

  ngOnInit() {
  }

  signout(){
    sessionStorage.clear();
    this.cookieService.delete("UserIdentity");
    this.authService.signout().subscribe(res => {})
  }

}
