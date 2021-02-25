import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { EmitterVisitorContext } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})
export class OperationService {

  constructor(private http:HttpClient) { }

  getOne(id) : Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/operation/getOperation/" 
      + id)
  }

  getAll() : Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/operation/getAll")
  }

  editOperation(operation): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "api/operation/editOperation",
      operation
    );
  }

  getAllByTypeAndGroup(type, groupId): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/operation/getAllByTypeAndGroup/" +
      type + "/" + groupId
    );
  }

  getAllByType(type): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/operation/getAllByType/" +
      type
    );
  }

  getAllByGroup(groupId): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/operation/getAllByGroup/" +
      groupId
    );
  }

  getAllByErpGroup(groupId): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/operation/getAllByErpGroup/" +
      groupId
    );
  }

  getAllGroupByType(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/operation/getAllGroupByType"
    )
  }

  syncOperation(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/operation/"
    )
  }
}
