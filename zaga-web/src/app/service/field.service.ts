import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class FieldService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<any> {
    return this.http.get(environment.gatewayWorkOrderURL + "api/field/getAll");
  }

  getOne(id): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/field/getOne/" + id
    );
  }

  editField(field): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "api/field/editField",
      field
    );
  }

  setFieldCoordinates(data): Observable<any> {
    return this.http.put(
      environment.gatewayWorkOrderURL + "api/field/setFieldCoordinates", 
        data
    )
  } 
}
