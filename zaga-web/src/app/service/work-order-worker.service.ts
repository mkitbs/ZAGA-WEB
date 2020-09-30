import { HttpClient } from "@angular/common/http";
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
}
