import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MachineGroupService {

  constructor(private http:HttpClient) { }

  getAll() : Observable<any>{
    return this.http.get(environment.gatewayWorkOrderURL + "api/machine/group/getAll");
  }

  syncMachineGroup(): Observable<any>{
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/machine/group/"
    )
  }
}
