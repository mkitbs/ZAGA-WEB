import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VarietyService {

  constructor(private http:HttpClient) { }

  getAll(): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/variety/getAll"
    );
  }

  syncVariety(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/variety/"
    )
  }
}
