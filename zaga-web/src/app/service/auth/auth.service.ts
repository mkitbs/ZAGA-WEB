import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  signin(form): Observable<any> {
    return this.http.post(environment.authURL + "signin", form);
  }

  checkToken(token): Observable<any> {
    return this.http.get(environment.authURL + "check/" + token, { responseType: 'text' });
  }

  checkUserRoles(): Observable<any> {
    return this.http.get(environment.authURL + "getAdminRole");
  }

  getLogged(): Observable<any> {
    return this.http.get(environment.authURL + "getLogged")
  }

  signout(): Observable<any> {
    return this.http.get(environment.authURL + "signout");
  }

  getUserSettings(): Observable<any> {
    return this.http.get(environment.authURL + "getUserSettings");
  }

  updateUserSettings(tenantId, setting): Observable<any> {
    return this.http.put(environment.authURL + "updateSettings/" + tenantId, setting);
  }

  requestResetPassword(email): Observable<any> {
    return this.http.get(
      environment.authURL + "requestResetPassword/" + email
    )
  }

  resetPassword(resetPassword): Observable<any> {
    return this.http.post(
      environment.authURL + "resetPassword", resetPassword
    )
  }

}
