import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardGuard implements CanActivate {
  constructor(public router: Router, public authservice: AuthService) { }
  canActivate() {

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
