import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { NgxSpinnerService } from 'ngx-spinner';
import { Roles } from 'src/app/models/Roles';
import { AuthService } from 'src/app/service/auth/auth.service';
import { AuthForm } from 'src/app/service/auth/model/AuthForm';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})



export class LoginComponent implements OnInit {

  authForm: AuthForm = new AuthForm();
  isLoginFailed: boolean = false;
  roles: Roles[] = [];


  constructor(private authService: AuthService,
    private router: Router,
    private spinner: NgxSpinnerService,
    private cookieService: CookieService) { }

  ngOnInit() {
    this.spinner.show();
    let cookie = this.cookieService.get('UserIdentity');
    if (cookie !== null) {
      this.authService.checkToken(cookie).subscribe(data => {
        window.sessionStorage.setItem("AuthToken", cookie);
        this.spinner.hide();
        this.router.navigate(['']);
      }, err => {
        this.cookieService.deleteAll();
        this.spinner.hide();
      })
    }
  }

  login() {
    this.spinner.show();
    this.authService.signin(this.authForm).subscribe(data => {
      console.log(data)
      this.roles = data.roles;
      window.sessionStorage.setItem("AuthToken", data.ac_id);
      this.cookieService.set('UserIdentity', data.ac_id, { expires: 5 });
      this.spinner.hide();
      this.roles.forEach(role => {
        if(role.name == 'ROLE_TRACTOR_DRIVER'){
          this.router.navigate(['/workOrderTractorDriver']);
        } else {
          this.router.navigate(['']);
        }
      })
      
    }, err => {
      this.spinner.hide();
      this.isLoginFailed = true;
    })
  }

}
