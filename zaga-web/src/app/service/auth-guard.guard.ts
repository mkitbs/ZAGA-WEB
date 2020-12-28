import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Roles } from '../models/Roles';
import { User } from '../models/User';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardGuard implements CanActivate {
  constructor(public router: Router,
    public authservice: AuthService,
    private cookieService: CookieService) { }
  roles: Roles[] = [];
  canActivate() {
    let token = this.cookieService.get('UserIdentity');

    if (token !== null && sessionStorage.getItem('AuthToken') !== null) {
      if (token != sessionStorage.getItem('AuthToken')) {
        alert("Menjali ste token!")
        sessionStorage.removeItem('AuthToken');
        this.cookieService.deleteAll();
        this.router.navigate(['login'])
        return false;
      } else {
        this.authservice.getLogged().subscribe(data => {
          this.roles = data.roles;
          let index = this.roles.findIndex(x => x.name === "ROLE_TRACTOR_DRIVER");
          if(index != -1){
            this.router.navigateByUrl('/workOrderTractorDriver')
          } else {
            this.router.navigate([''])
          }
        })
      }
    }
    if (sessionStorage.getItem('AuthToken') === null) {
      this.router.navigate(['login']);
      return false;
    } else {
      this.authservice.checkToken(sessionStorage.getItem('AuthToken')).subscribe(data => {
        return true;
      }, err => {
        sessionStorage.removeItem('AuthToken');
        this.router.navigate(['login']);
        return false;
      })

      //this.router.navigate(['home']);
      return true;
    }

  }
}
