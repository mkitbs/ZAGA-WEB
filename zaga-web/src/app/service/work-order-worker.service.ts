import { HttpClient } from "@angular/common/http";
import { identifierModuleUrl } from "@angular/compiler";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class WorkOrderWorkerService {
  constructor(private http: HttpClient) {}

  addWorker(worker, id): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "api/workOrderWorker/addWorker/" + id,
      worker
    );
  }

  updateWorkOrderWorker(id, wow): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL +
        "api/workOrderWorker/updateWorkOrderWorker/" +
        id,
      wow
    );
  }

  deleteWorkOrderWorker(id): Observable<any> {
    return this.http.delete(
      environment.gatewayWorkOrderURL +
        "api/workOrderWorker/deleteWorkOrderWorker/" +
        id
    );
  }

  updateWorkOrderWorkerBasicInfo(id, wow): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL +
        "api/workOrderWorker/updateWorkOrderWorkerBasicInfo/" +
        id,
      wow
    );
  }

  getDataForReport(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + 
      "api/workOrderWorker/getDataForReport"
    )
  }

  getWorkOrderForTractorDriver(): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + 
      "api/workOrderWorker/getWorkOrdersForTractorDriver"
    )
  }

  getOne(id): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/workOrderWorker/getOne/" + id
    )
  }
}
