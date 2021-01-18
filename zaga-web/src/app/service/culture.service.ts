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
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getCulture/" + 
      id
    );
  }
  
  getAll() : Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL+ "api/culture/getAll"
    );
  }

  editCulture(culture): Observable<any>{
    return this.http.post(
      environment.gatewayWorkOrderURL + "api/culture/editCulture",
      culture
    );
  }

  getAllByOrgCon(orgCon): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllByOrgCon/" +
      orgCon
    );
  }

  getAllByCultureType(type): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllByCultureType/" +
      type
    );
  }

  getAllByCultureGroup(id): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllByCultureGroup/" +
      id
    );
  }

  getAllByOrgConCultureTypeCultureGroup(orgCon, type, id): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllByOrgConCultureTypeCultureGroup/" +
      orgCon + "/" + type + "/" + id
    );
  }

  getAllByOrgConAndCultureType(orgCon, type): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllByOrgConAndCultureType/" +
      orgCon + "/" + type
    );
  }

  getAllByOrgConAndCultureGroup(orgCon, id): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllByOrgConAndCultureGroup/" +
      orgCon + "/" + id
    );
  }

  getAllByCultureTypeAndCultureGroup(type, id): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllByCultureTypeAndCultureGroup/" +
      type + "/" + id
    );
  }

  getAllGroupByCultureType(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllGroupByCultureType"
    )
  }

  getAllGroupByProductionType(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/getAllGroupByProductionType"
    )
  }

  syncCulture(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/culture/"
    )
  }
}
