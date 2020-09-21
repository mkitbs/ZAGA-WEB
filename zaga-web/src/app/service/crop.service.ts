import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CropService {

  constructor(private http:HttpClient) { }

  getOne(id) : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/crop/getCrop/" + id)
  }

  getAll() : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/crop/getAll")
  }

  getAllByFieldAndYear(fieldId, year) : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/crop/getAllByFieldAndYear/" + fieldId + "/" + year)
  }
}
