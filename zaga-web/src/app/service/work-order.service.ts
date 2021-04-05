import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { WorkOrder } from "../models/WorkOrder";

@Injectable({
  providedIn: "root",
})
export class WorkOrderService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<any> {
    return this.http.get(environment.gatewayWorkOrderURL + "workOrder/getAll");
  }

  getOne(id): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "workOrder/getWorkOrder/" + id
    );
  }

  addWorkOrder(workOrder): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "workOrder/createWorkOrder",
      workOrder
    );
  }

  updateWorkOrder(workOrder): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "workOrder/updateWorkOrder",
      workOrder
    );
  }

  createWorkOrderCopy(id, date): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "workOrder/createCopy/" + id,
      date
    );
  }

  closeWorkOrder(workOrder): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "workOrder/closeWorkOrder",
      workOrder
    );
  }

  getAllByStatus(status): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "workOrder/getAllByStatus/" + status
    );
  }

  getMyWorkOrders(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "workOrder/getMyWorkOrders"
    )
  }

  getMyByStatus(status): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "workOrder/getMyWoByStatus/" + status
    )
  }

  getOperationsForToday(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "workOrder/getOperationsForToday"
    )
  }
  
}
