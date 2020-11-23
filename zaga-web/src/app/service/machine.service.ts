import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class MachineService {
  constructor(private http: HttpClient) {}

  getAllPropulsion(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/machine/getAll/propulsion"
    );
  }

  getAllCoupling(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/machine/getAll/coupling"
    );
  }

  editMachine(machine) : Observable<any>{
    return this.http.post(
      environment.gatewayWorkOrderURL + "api/machine/editMachine", 
      machine);
  }

  getOne(id): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/machine/getMachine/" + id
    );
  }

  getAll(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/machine/getAll"
    );
  }

  getMachinesByMachineGroup(id): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/machine/getMachinesByMachineGroup/" + id
    );
  }
}
