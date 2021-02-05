import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from 'src/app/service/auth/auth.service';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private cookieService: CookieService
  ) { }

  admin;
  selectedProfile;


  ngOnInit() {
    this.authService.checkUserRoles().subscribe(res => {
      this.admin = true;
    }, error => {
      this.admin = false;
    })
  }

  signout(){
    sessionStorage.clear();
    this.cookieService.delete("UserIdentity");
    this.authService.signout().subscribe(res => {})
  }

  changeProfile(event){
    if(event.value == "qas"){
      localStorage["PROFILE"] = "QAS";
    }else if(event.value == "prd"){
      localStorage["PROFILE"] = "PRD";
    }

  }

}
