import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Roles } from '../models/Roles';
import { User } from '../models/User';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuardGuard implements CanActivate {
  constructor(public router: Router, public authservice: AuthService) { }
  canActivate() {
    if (sessionStorage.getItem('AuthToken') === null) {
      this.router.navigate(['login']);
      return false;
    } else {
      this.authservice.checkUserRoles().subscribe(data => {
        return true;
      }, err => {
        this.router.navigate(['']);
        return false;
      })
      return true;
    }
    
  }
  
}
