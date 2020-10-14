import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MachineService {

  constructor(private http:HttpClient) { }

  getAll() : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/machine/getAll")
  }

  getOne(id) : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/machine/getMachine/" + id)
  }

  editMachine(machine) : Observable<any>{
    return this.http.post(environment.gatewayWorkOrderURL + "api/machine/editMachine", machine);
  }

}
