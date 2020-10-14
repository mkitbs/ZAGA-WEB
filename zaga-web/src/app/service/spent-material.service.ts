import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class SpentMaterialService {
  constructor(private http: HttpClient) {}

  addSpentMaterial(id, material): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "api/spentMaterial/" + id,
      material
    );
  }

  updateSpentMaterial(id, material): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL +
        "api/spentMaterial/updateSpentMaterial/" +
        id,
      material
    );
  }

  deleteSpentMaterial(id): Observable<any> {
    return this.http.delete(
      environment.gatewayWorkOrderURL +
        "api/spentMaterial/deleteSpentMaterial/" +
        id
    );
  }
}
