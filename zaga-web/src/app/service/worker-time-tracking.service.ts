import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WorkerTimeTrackingService {

  constructor(private http: HttpClient) { }

  getWorkerTask(id): Observable<any> {
    return this.http.get(environment.gatewayWorkOrderURL + "api/timeTracking/getOne/" + id);
  }

  setTracking(timeTracking): Observable<any> {
    return this.http.post(environment.gatewayWorkOrderURL + "api/timeTracking/setTracking", timeTracking);
  }

  updateTracking(timeTracking): Observable<any> {
    return this.http.put(environment.gatewayWorkOrderURL + "api/timeTracking/updateTracking", timeTracking);
  }
}
