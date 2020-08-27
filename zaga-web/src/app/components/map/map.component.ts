import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  constructor() { }

  lat = 45.588880;
  lng = 19.996049;
  map: any;
  

  markersWorkers = [
    {
      "lat": "45.584951",
      "lng": "19.984123",
      "name": "Jockov Željko"
    },
    {
      "lat": "45.588531",
      "lng": "19.985576",
      "name": "Jokanović Dragan"
    },
    {
      "lat": "45.593736",
      "lng": "19.984471",
      "name": "Juhas Zoltan"
    },
    {
      "lat": "45.592272",
      "lng": "19.993654",
      "name": "Juhas Žolt"
    },
    {
      "lat": "45.599145",
      "lng": "19.997025",
      "name": "Kljajić Mićko"
    },
    {
      "lat": "45.593899",
      "lng": "20.003534",
      "name": "Knežević Goran"
    },
    {
      "lat": "45.572138",
      "lng": "20.018587",
      "name": "Koletar Ervin"
    },
    {
      "lat": "45.571527",
      "lng": "19.999292",
      "name": "Koletar Adrian"
    },
    {
      "lat": "45.575067",
      "lng": "20.005452",
      "name": "Koletar Žolt"
    },
    {
      "lat": "45.580884",
      "lng": "20.021958",
      "name": "Mađar Milenko"
    },
    {
      "lat": "45.585770",
      "lng": "20.012857",
      "name": "Matić Biljana"
    },
    {
      "lat": "45.598657",
      "lng": "20.024922",
      "name": "Mesaroš Ištvan"
    }
  ]

  markersMachines = [
    {
      "lat": "45.584951",
      "lng": "19.984123",
      "name": "IMT 533/539"
    },
    {
      "lat": "45.588531",
      "lng": "19.985576",
      "name": "IMT-539"
    },
    {
      "lat": "45.594387",
      "lng": "19.984820",
      "name": "JOHN DEERE 5510 N"
    },
    {
      "lat": "45.592232",
      "lng": "19.993480",
      "name": "JOHN DEERE 6620"
    },
    {
      "lat": "45.599430",
      "lng": "19.995514",
      "name": "JOHN DEERE 6630"
    },
    {
      "lat": "45.593899",
      "lng": "20.003534",
      "name": "JOHN DEERE 6820"
    },
    {
      "lat": "45.572056",
      "lng": "20.017773",
      "name": "JOHN DEERE 8320"
    },
    {
      "lat": "45.571039",
      "lng": "19.999640",
      "name": "JOHN DEERE 8330"
    },
    {
      "lat": "45.574701",
      "lng": "20.004115",
      "name": "NEW HOLAND T 6.050"
    },
    {
      "lat": "45.580233",
      "lng": "20.021667",
      "name": "NEW HOLLAND T 7.210"
    },
    {
      "lat": "45.585974",
      "lng": "20.012509",
      "name": "NEW HOLAND T 8.410"
    }
  ]

  onMapReady(map) {
    this.map = map;
    
  }

  ngOnInit() {
  }

}
