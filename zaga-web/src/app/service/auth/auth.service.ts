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
    return this.http.post(environment.auth + "auth/signin", form);
  }

  checkToken(token): Observable<any> {
    console.log(token)
    return this.http.get(environment.auth + "auth/check/" + token, { responseType: 'text' });
  }

  checkUserRoles(): Observable<any> {
    return this.http.get(environment.auth + "auth/getAdminRole");
  }

  getLogged(): Observable<any> {
    return this.http.get(environment.auth + "auth/getLogged")
  }

  signout(): Observable<any> {
    return this.http.get(environment.auth + "auth/signout");
  }
}
