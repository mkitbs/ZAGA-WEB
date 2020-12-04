import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/service/auth/auth.service';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(
    private authService: AuthService
  ) { }

  admin;


  ngOnInit() {
    this.authService.checkUserRoles().subscribe(res => {
      this.admin = true;
    }, error => {
      this.admin = false;
    })
  }

  signout(){
    sessionStorage.clear();
    this.authService.signout().subscribe(res => {
      
    })
  }

}
