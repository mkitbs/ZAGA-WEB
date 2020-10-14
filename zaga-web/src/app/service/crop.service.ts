import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class CropService {
  constructor(private http: HttpClient) {}

  getOne(id): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/crop/getCrop/" + id
    );
  }

  getAll(): Observable<any> {
    return this.http.get(environment.gatewayWorkOrderURL + "api/crop/getAll");
  }

  getAllByFieldAndYear(fieldId, year): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL +
        "api/crop/getAllByFieldAndYear/" +
        fieldId +
        "/" +
        year
    );
  }

  getAllByFieldAndCulture(fieldId, cultureId): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL +
        "api/crop/getAllByFieldAndCulture/" +
        fieldId +
        "/" +
        cultureId
    );
  }

  getAllByField(fieldId): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/crop/getAllByField/" + fieldId
    );
  }

  getAllByCulture(cultureId): Observable<any> {
    return this.http.get(
      environment.gatewayWorkOrderURL + "api/crop/getAllByCulture/" + cultureId
    );
  }

  updateCrop(crop): Observable<any> {
    return this.http.post(
      environment.gatewayWorkOrderURL + "api/crop/updateCrop",
      crop
    );
  }
}
