import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
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


  constructor(private authService: AuthService, private router: Router, private spinner: NgxSpinnerService) { }

  ngOnInit() {
  }

  login() {
    this.spinner.show();
    this.authService.signin(this.authForm).subscribe(data => {
      console.log(data)
      window.sessionStorage.setItem("AuthToken", data.ac_id);
      this.spinner.hide();
      this.router.navigate(['']);
    }, err => {
      this.spinner.hide();
      this.isLoginFailed = true;
    })
  }

}
