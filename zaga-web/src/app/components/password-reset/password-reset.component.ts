import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/service/auth/auth.service';
import { ResetPassword } from 'src/app/service/auth/model/ResetPassword';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private toastr: ToastrService,
    private spinner: NgxSpinnerService,
    private router: Router
  ) { }

  token = this.route.snapshot.params.token;
  new;
  email;
  password;
  confirmPassword;
  errorConfirm;
  passwordFaild = false;
  resetPassword: ResetPassword = new ResetPassword();

  ngOnInit() {
    if(this.token == "new"){
      this.new = true;
    } else {
      this.new = false;
    }
  }

  requestResetPassword(){
    this.spinner.show();
    this.authService.requestResetPassword(this.email).subscribe(res => {
      this.spinner.hide();
      this.toastr.success("Email je poslat.")
    }, error => {
      this.spinner.hide();
      if(error.status === 400){
        this.toastr.error("Email ne postoji.")
      } else {
        this.toastr.error("Email nije poslat. Probajte ponovo.")
      }
      
    })
  }

  confirmResetPassword(){
    this.spinner.show();
    if(this.password != this.confirmPassword){
      this.spinner.hide();
      this.passwordFaild = true;
      this.errorConfirm = "Lozinke nisu iste"
    } else {
      this.resetPassword.password = this.password;
      this.resetPassword.token = this.token;
      this.authService.resetPassword(this.resetPassword).subscribe(res => {
        this.spinner.hide();
        this.toastr.success("Lozinka je promenjena.")
        this.router.navigateByUrl("/login");
      }, error => {
        this.spinner.hide();
        this.toastr.error("Lozinka nije promenjena. Probajte ponovo.")
      })
    }
  }

}
