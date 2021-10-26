import { Component, OnInit } from "@angular/core";
import { FormControl } from '@angular/forms';
import { NgxSpinnerService } from "ngx-spinner";
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
    private toastr: ToastrService,
    private spinner: NgxSpinnerService,
  ) {}

  fields: Field[] = [];
  fieldGroups: FieldGroup[] = [];

  field: Field = new Field();
  fieldFC: FormControl = new FormControl("")

  lat = 45.588880;
  lng = 19.996049;
  map: any;
  paths = [];
  firstPair = {};

  fieldId;
  clickedOnField = false;

  polyLatLng: any[] = [];
  allOverlays: any[] = [];
  fieldPolygon: FieldPolygon = new FieldPolygon();

  loading;

  page = 1;
	pageSize = 12;

  ngOnInit() {
    this.getAll();
    
  }

  getAll(){
    this.spinner.show();
    this.loading = true;
    this.fieldGroupService.getAll().subscribe((data) => {
      //data = this.convertKeysToLowerCase(data);
      this.fieldGroups = data;
      this.fieldService.getAll().subscribe((data) => {
        this.loading = false;
       this.spinner.hide();
        this.fields = data;
        console.log(this.fields)
        this.fields.forEach((field) => {
          field.fieldGroupName = this.fieldGroups.find(
            (fieldGroup) => fieldGroup.dbId == field.fieldGroup
          ).Name;
        });
      }, error =>{
        this.spinner.hide();
      });
    }, error =>{
      this.spinner.hide();
    });
  }

  getField(id) {
    this.clickedOnField = true;
    this.field = this.fields.find((field) => field.dbId == id);
    if(this.field.coordinates.length != 0){
      this.paths = this.field.coordinates;
      this.lat = this.field.coordinates[1].lat + ((this.field.coordinates[3].lat - this.field.coordinates[1].lat) / 2);
      this.lng = this.field.coordinates[1].lng + ((this.field.coordinates[3].lng - this.field.coordinates[1].lng) / 2);
      var polygon = new google.maps.Polygon({
        path : this.paths,
        map: this.map
      });
      this.field.Area = google.maps.geometry.spherical.computeArea(polygon.getPath()).toFixed(2);
    } 
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

  initDrawingManager(map: any) {
    const options = {
      drawingControl: false,
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
    var infowindow = new google.maps.InfoWindow();
     google.maps.event.addListener(drawingManager, 'polygoncomplete', function (polygon) {
      const len = polygon.getPath().getLength();
      var polyArrayLatLng = [];

      for (let i = 0; i < len; i++) {
        var vertex = polygon.getPath().getAt(i);
        var vertexLatLng = {lat: vertex.lat(), lng: vertex.lng()};
        polyArrayLatLng.push(vertexLatLng);
      }
    
      polyArrayLatLng.push(polyArrayLatLng[0]);

      self.polyLatLng = polyArrayLatLng;

      var area = google.maps.geometry.spherical.computeArea(polygon.getPath());
      infowindow.setContent("Površina: " + area.toFixed(2) + " metara kvadratnih.");
      infowindow.setPosition(polygon.getPath().getAt(0));
      infowindow.open(map);
    });
    
    google.maps.event.addListener(drawingManager, 'overlaycomplete', function(e) {
      self.allOverlays.push(e);
    })
  }

  setPolygon(id){
    let myhash = [];
    this.polyLatLng.forEach((poly, i) => {
      console.log(poly)
      let pair = {lat: poly.lat, lng: poly.lng}
      myhash.push(pair)
    })
    this.fieldPolygon.values = myhash;
    this.fieldPolygon.id = id;
    
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

  closeModal(){
    this.paths = [];
    this.clickedOnField = false;
    this.dismissPolygon();
  }

}
