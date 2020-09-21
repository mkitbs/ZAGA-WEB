import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OperationService {

  constructor(private http:HttpClient) { }

  getOne(id) : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/operation/getOperation/" + id)
  }

  getAll() : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/operation/getAll")
  }
}