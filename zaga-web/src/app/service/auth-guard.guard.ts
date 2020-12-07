import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardGuard implements CanActivate {
  constructor(public router: Router,
    public authservice: AuthService,
    private cookieService: CookieService) { }
  canActivate() {
    let token = this.cookieService.get('UserIdentity');

    if (token !== null && sessionStorage.getItem('AuthToken') !== null) {
      if (token != sessionStorage.getItem('AuthToken')) {
        alert("Menjali ste token!")
        sessionStorage.removeItem('AuthToken');
        this.cookieService.deleteAll();
        this.router.navigate(['login'])
        return false;
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
