import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class FieldGroupService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/field/group/getAll"
    );
  }

  editFieldGroup(fieldGroup): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "api/field/group/editFieldGroup",
      fieldGroup
    );
  }

  syncFieldGroup(): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/field/group/"
    )
  }
}
