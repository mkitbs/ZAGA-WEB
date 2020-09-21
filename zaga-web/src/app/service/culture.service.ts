import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CultureService {

  constructor(private http:HttpClient) { }

  getOne(id) : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/culture/getCulture/" + id);
  }
  
  getAll() : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL+ "api/culture/getAll")
  }
}