import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class WorkOrderMachineService {
  constructor(private http: HttpClient) {}

  addMachineState(machine, id): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL +
        "api/workOrderMachine/addMachineState/" +
        id,
      machine
    );
  }
}
