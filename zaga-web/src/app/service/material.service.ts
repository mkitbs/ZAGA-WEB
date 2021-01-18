import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MaterialService {

  constructor(private http : HttpClient) { }

  getAll() : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/material/getAll")
  }

  getOne(id) : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/material/getMaterial/" + id)
  }

  syncMaterial(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/material/"
    )
  }
}
