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
    return this.http.post(environment.auth + "signin", form);
  }

  checkToken(token): Observable<any> {
    console.log(token)
    return this.http.get(environment.auth + "check/" + token, { responseType: 'text' });
  }
}
