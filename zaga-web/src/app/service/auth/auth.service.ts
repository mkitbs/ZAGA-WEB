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
    console.log(token)
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
}
