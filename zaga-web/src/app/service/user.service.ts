import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  getAll() : Observable<any>{
    return this.http.get(environment.gatewayElasticURL + "api/users/");
  }

  findByName(name) : Observable<any> {
    let params = new HttpParams();
    params = params.append('name', name);
    return this.http.get(environment.gatewayElasticURL + "api/users/name", {params: params});
  }

  getOne(id) : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/employee/getEmployee/" + id);
  }

  editUser(user) : Observable<any>{
    return this.http.post(environment.gatewayWorkOrderURL + "api/employee/editUser", user);
  }

}
