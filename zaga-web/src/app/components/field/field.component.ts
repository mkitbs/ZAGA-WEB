import { Component, OnInit } from "@angular/core";
import { FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Field } from "src/app/models/Field";
import { FieldGroup } from "src/app/models/FieldGroup";
import { FieldPolygon } from 'src/app/models/FieldPolygon';
import { FieldGroupService } from "src/app/service/field-group.service";
import { FieldService } from "src/app/service/field.service";

declare const google: any;

export interface IHash {
  [details: number] : number;
}

@Component({
  selector: "app-field",
  templateUrl: "./field.component.html",
  styleUrls: ["./field.component.css"],
})
export class FieldComponent implements OnInit {
  constructor(
    private fieldService: FieldService,
    private fieldGroupService: FieldGroupService,
    private toastr: ToastrService
  ) {}

  fields: Field[] = [];
  fieldGroups: FieldGroup[] = [];

  field: Field = new Field();
  fieldFC: FormControl = new FormControl("")

  lat = 45.588880;
  lng = 19.996049;
  map: any;
  paths;

  polyLatLng: any[] = [];
  allOverlays: any[] = [];
  fieldPolygon: FieldPolygon = new FieldPolygon();

  ngOnInit() {
    this.getAll();
    
  }

  getAll(){
    this.fieldGroupService.getAll().subscribe((data) => {
      //data = this.convertKeysToLowerCase(data);
      this.fieldGroups = data;
      this.fieldService.getAll().subscribe((data) => {
        this.fields = data;
        console.log(this.fields)
        this.fields.forEach((field) => {
          field.fieldGroupName = this.fieldGroups.find(
            (fieldGroup) => fieldGroup.dbId == field.fieldGroup
          ).Name;
        });
      });
    });
  }

  getField(id) {
    this.field = this.fields.find((field) => field.dbId == id);
    this.dismissPolygon();
  }

  editField() {
    this.fieldService.editField(this.field).subscribe((data) => {
      this.fieldService.getAll().subscribe((data) => {
        this.fields = data;
        this.fields.forEach((field) => {
          field.fieldGroupName = this.fieldGroups.find(
            (fieldGroup) => fieldGroup.dbId == field.fieldGroup
          ).Name;
        });
      });
    });
  }

  displayFnField(field : Field): string {
    return field && field.Id + " - " + field.Name;
  }

  onMapReady(map) {
    this.map = map;
    this.initDrawingManager(map);
  }

  onChoseLocation(event){
    this.lat = event.coords.lat;    
    this.lng = event.coords.lng;
  }

  getFieldOnMap(){
    var lat1 = 45.585433;
    var lng1 = 20.008278;
    var lat2 = 45.583605;
    var lng2 = 20.016601;
    var lat3 = 45.591386;
    var lng3 = 20.020407;
    var lat4 = 45.593470;
    var lng4 = 20.011968;

    this.lat = lat1 + ((lat3 - lat1) / 2);
    this.lng = lng1 + ((lng3 - lng1) / 2);
    this.paths = [
      {lat: lat1, lng: lng1},
      {lat: lat2, lng: lng2},
      {lat: lat3, lng: lng3},
      {lat: lat4, lng: lng4}
    ]
  }

  initDrawingManager(map: any) {
    const options = {
      drawingControl: true,
      drawingControlOptions: {
        drawingModes: ["polygon"]
      },
      polygonOptions: {
        draggable: true,
        editable: true
      },
      drawingMode: google.maps.drawing.OverlayType.POLYGON
    };
    var self = this;
    var drawingManager = new google.maps.drawing.DrawingManager(options);
    drawingManager.setMap(map);
     google.maps.event.addListener(drawingManager, 'polygoncomplete', function (polygon) {
      const len = polygon.getPath().getLength();
      console.log(this.poly)
      var polyArrayLatLng = [];
      

      for (let i = 0; i < len; i++) {
        var vertex = polygon.getPath().getAt(i);
        var vertexLatLng = {lat: vertex.lat(), lng: vertex.lng()};
        polyArrayLatLng.push(vertexLatLng);
      }
    
      polyArrayLatLng.push(polyArrayLatLng[0]);

      self.polyLatLng = polyArrayLatLng;

      console.log('coordinates', polyArrayLatLng);
    });
    
    google.maps.event.addListener(drawingManager, 'overlaycomplete', function(e) {
      self.allOverlays.push(e);
      console.log(self.allOverlays)
    })
  }

  setPolygon(id){
    console.log(this.polyLatLng)
    let myhash: IHash = {};
    this.polyLatLng.forEach(poly => {
      myhash[poly.lat] = poly.lng;
    })
    this.fieldPolygon.values = myhash;
    this.fieldPolygon.id = id;
    console.log(this.fieldPolygon)
    this.fieldService.setFieldCoordinates(this.fieldPolygon).subscribe(res => {
      this.getAll();
      this.toastr.success("Parcela je ucrtana na mapi.")
    }, error => {
      this.toastr.error("Parcela nije ucrtana na mapi.")
    })
  }

  dismissPolygon(){
    for (var i=0; i < this.allOverlays.length; i++){
      this.allOverlays[i].overlay.setMap(null);
    }
    this.allOverlays = [];
  }

}
