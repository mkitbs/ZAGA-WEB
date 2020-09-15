import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WorkOrderService {

  constructor(private http:HttpClient) { }

  getAll() : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "workOrder/getAll");
  }

  getOne(id) : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "workOrder/getWorkOrder/" + id);
  }

  addWorkOrder(workOrder) : Observable<any>{
    return this.http.post(environment.gatewayWorkOrderURL + "workOrder/createWorkOrder", workOrder);
  }

  updateWorkOrder(workOrder) : Observable<any>{
    return this.http.post(environment.gatewayWorkOrderURL + "workOrder/updateWorkOrder", workOrder);
  }
}
