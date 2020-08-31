import { Component, OnInit, Renderer2 } from '@angular/core';
import { ChartsModule } from 'ng2-charts';
import { SelectControlValueAccessor } from '@angular/forms';
import * as Chart from 'chart.js';
import { stringify } from 'querystring';
declare const google: any;


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

  constructor(private renderer: Renderer2) { }
  //ucitava se ovde da bi se na pregledu naloga videle promene postojecih i dodele novih
  tempJSON = [
    {
       "id":1,
       "name":"PRSKANJE FUNGICIDOM",
       "start":"13.08.2020.",
       "end":"13.08.2020.",
       "table":"T-46",
       "area": "40",
       "year": "2020",
       "culture":"KUKURUZ",
       "responsible":"PLAVŠIĆ BORKA",
       "status":"Novi",
       "treated": "",
       "category": "",
       "machines": [
         {
           "id":1,
           "machine":"JOHN DEERE 6810",
           "initialState": "680",
           "finalState": "750",
           "sumState": "70",
           "worker":"MEJIĆ PETRA",
           "date":"13.08.2020.",
           "workPeriod":"5",
           "fuel":"28",
           "fuelType":"disel",
           "storage":"Magacin 1"
         },
         {
           "id":2,
           "machine":"IMT 539",
           "initialState": "1430",
           "finalState": "1630",
           "sumState": "200",
           "worker":"VUJIČIĆ GORAN",
           "date":"13.08.2020.",
           "workPeriod":"2",
           "fuel":"10",
           "fuelType":"disel",
           "storage":"Magacin 1"
         }
       ],
       "workers": [
         {
           "id":1,
           "worker":"MEJIĆ PETRA",
           "date":"13.08.2020.",
           "dayWorkPeriod": "8",
           "nightWorkPeriod": "0",
           "operation": "PRSKANJE FUNGICIDOM",
           "workPeriod":"8",
         },
         {
           "id":2,
           "worker":"VUJIČIĆ GORAN",
           "date":"13.08.2020.",
           "dayWorkPeriod": "8",
           "nightWorkPeriod": "0",
           "operation": "PREVOZ VODE ZA PRSKANJE",
           "workPeriod":"8",
         }
       ],
       "materials": [
         {
           "id":1,
           "name":"FERTILEADER AXIS",
           "quantity":"400",
           "spent": "320",
           "spentPerHectar": "8",
           "quantityPerHectar": "10",
           "unit":"l"
         }
       ]
     },
     {
       "id":2,
       "name":"ORANJE DO 35 CM",
       "start":"14.08.2020.",
       "end":"15.08.2020.",
       "table":"T-28",
       "area": "50",
       "year": "2020",
       "culture":"SOJA",
       "responsible":"OBRADOVIĆ SLAVKO",
       "status":"U radu",
       "treated": "35",
       "category": "",
       "machines": [
         {
           "id":1,
           "machine":"NEW HOLAND CR7.90",
           "initialState": "1450",
           "finalState": "2750",
           "sumState": "1300",
           "worker":"VELJKOVIĆ BORKO",
           "date":"14.08.2020.",
           "workPeriod":"8",
           "fuel":"103",
           "fuelType":"disel",
           "storage":"Magacin 1"
         }
       ],
       "workers": [
         {
           "id":1,
           "worker":"VELJKOVIĆ BORKO",
           "date":"14.08.2020.",
           "dayWorkPeriod": "8",
           "nightWorkPeriod": "0",
           "operation": "ORANJE DO 35 CM",
           "workPeriod":"8",
         },
         {
           "id":2,
           "worker":"MILOVANČEV BRANISLAV",
           "date":"14.08.2020.",
           "dayWorkPeriod": "8",
           "nightWorkPeriod": "0",
           "operation": "OPŠTI POSLOVI",
           "workPeriod":"8",
         }
       ],
       "materials": [
         {
           "id":"",
           "name":"",
           "quantity":"",
           "spent": "",
           "spentPerHectar": "",
           "quantityPerHectar": "",
           "unit":""
         }
       ]
     },
     {
      "id":3,
      "name":"SETVA",
      "start":"23.07.2020.",
      "end":"24.07.2020.",
      "table":"T-31",
      "area": "35",
      "year": "2020",
      "culture":"SEMENSKA PŠENICA",
      "responsible":"ARSENOV NEMANJA",
      "status":"Zatvoren",
      "treated": "35",
      "category": "",
      "machines": [
        {
          "id":1,
          "machine":"JOHN DEERE 6810",
          "initialState": "680",
          "finalState": "750",
          "sumState": "70",
          "worker":"MEJIĆ PETRA",
          "date":"23.07.2020.",
          "workPeriod":"5",
          "fuel":"28",
          "fuelType":"disel",
          "storage":"Magacin 1"
        },
        {
          "id":2,
          "machine":"IMT 539",
          "initialState": "1430",
          "finalState": "1630",
          "sumState": "200",
          "worker":"VUJIČIĆ GORAN",
          "date":"23.07.2020.",
          "workPeriod":"4",
          "fuel":"20",
          "fuelType":"disel",
          "storage":"Magacin 1"
        }
      ],
      "workers": [
        {
          "id":1,
          "worker":"MEJIĆ PETRA",
          "date":"23.07.2020.",
          "dayWorkPeriod": "8",
          "nightWorkPeriod": "0",
          "operation": "SETVA",
          "workPeriod":"8",
        },
        {
          "id":2,
          "worker":"VUJIČIĆ GORAN",
          "date":"23.07.2020.",
          "dayWorkPeriod": "8",
          "nightWorkPeriod": "0",
          "operation": "SETVOSPREMANJE NOŠENIM SPREMAČ",
          "workPeriod":"8",
        }
      ],
      "materials": [
        {
          "id":1,
          "name":"DKC 5075",
          "quantity":"4000",
          "spent": "4200",
          "spentPerHectar": "120",
          "quantityPerHectar": "114.2",
          "unit":"kg"
        }
      ]
    },
    {
      "id":4,
      "name":"GRABLJ. LUCERKE, DETEL I TRAVA",
      "start":"01.08.2020.",
      "end":"01.08.2020.",
      "table":"T-37",
      "area": "10",
      "year": "2020",
      "culture":"LUCERKA",
      "responsible":"ĆUK RANKO",
      "status":"U radu",
      "treated": "5",
      "category": "",
      "machines": [
        {
          "id":1,
          "machine":"IMT 539",
          "initialState": "1680",
          "finalState": "1700",
          "sumState": "320",
          "worker":"JUHAS ROBERT",
          "date":"01.08.2020.",
          "workPeriod":"5",
          "fuel":"28",
          "fuelType":"disel",
          "storage":"Magacin 1"
        },
        {
          "id":2,
          "machine":"NEW HOLAND T 6.050",
          "initialState": "1430",
          "finalState": "1630",
          "sumState": "200",
          "worker":"VUJIČIĆ GORAN",
          "date":"01.08.2020.",
          "workPeriod":"4",
          "fuel":"29",
          "fuelType":"disel",
          "storage":"Magacin 1"
        }
      ],
      "workers": [
        {
          "id":1,
          "worker":"JUHAS ROBERT",
          "date":"01.08.2020.",
          "dayWorkPeriod": "8",
          "nightWorkPeriod": "0",
          "operation": "GRABLJ. LUCERKE, DETEL I TRAVA",
          "workPeriod":"8",
        },
        {
          "id":2,
          "worker":"VUJIČIĆ GORAN",
          "date":"01.08.2020.",
          "dayWorkPeriod": "8",
          "nightWorkPeriod": "0",
          "operation": "PREVOZ RADNIKA",
          "workPeriod":"8",
        }
      ],
      "materials": [
        {
          "id":"",
          "name":"",
          "quantity":"",
          "spent": "",
          "spentPerHectar": "",
          "quantityPerHectar": "",
          "unit":"l"
        }
      ]
    }
     
   ]

  chartColors: any[] = [{ backgroundColor:["#5C7495", "#f68901", "#ffc803", "#73964a", "#33490b", "#4c617c", "#b86907", "#f0dd9a", "#98d253",
  "#b1b6a7", "#7c784f", "#ccdd75"] }];

  //operacije danas
  barChartOptions = {
    responsive: true,
    showTooltips: true,
    scales: {
      xAxes: [
        {
          ticks: {
            beginAtZero: true
          },
          stacked: true
        }
      ],
      yAxes: [{
        stacked: true
      }]
    }
  };
  barChartLabels = ['PRIHRANA MIN. ĐUBRIVOM II', 'SETVOSPREMANJE NOŠENIM SPREMAČEM', 'FINO SETVOSPREMANJE', 'PREDSETVENO ĐUBRENJE', 'SETVA',
  'SETVOSPREMANJE VUČENIM SPREMAČEM', 'PRSKANJE KONT. HERBICIDOM I', 'PRSKANJE ZEMLJIŠNIM HERBICIDOM'];
  barChartType = 'horizontalBar';
  barChartLegend = true;
  barChartData = [
    {data: [357, 288, 378, 356, 300, 320, 164, 123 ], label:'urađeno ha', backgroundColor: '#73964a', hoverBackgroundColor: '#33490b'},
    {data: [230, 253, 149, 124, 150, 25, 0, 0], label:'preostalo ha', backgroundColor: '#f68901', hoverBackgroundColor: "#f68901"}
    
  ];

  //broj zaposlenih po operacijama
  barChartWorkerNoOptions = {
    responsive: true,
    scales: {
      xAxes: [
        {
          ticks: {
            beginAtZero: true
          },
          stacked: true
        }
      ],
      yAxes: [{
        stacked: true
   }]
    }
  };
  barChartWorkerNoLabels = ['OPŠTI POSLOVI', 'ŽETVA-TRANSPORT ROBE', 'ŽETVA', 'MEHAN. UKANJ. OCEVA U SEM. KUKURUZU', 'PREVOZ GORIVA I SIPANJE GORIVA',
  'TARUPIRANJE', 'MULČIRANJE', 'GRABLJ. LUCERKE, DETEL. I TRAVE'];
  barChartWorkerNoType = 'horizontalBar';
  barChartWorkerNoLegend = false;
  barChartWorkerNoData = [
    {data: [9, 8, 2, 2, 1, 1, 1, 1],  backgroundColor: '#73964a', hoverBackgroundColor: '#33490b'}
    
  ];

  //povrsine po kulturama
  pieChartAreaPerCultureType = 'pie';
  pieChartAreaPerCultureLabels = ["Šećerna repa", "Kukuruz", "Soja", "Semenski kukuruz", "Kukuruz šećerac", "Grašak", "Semenska pšenica",
  "Kukuruz kokičar", "Ozima pšenica", "Semenska soja", "Postrni kukuruz", "Boranija"];
  pieChartAreaPerCultureData = [
    {data: [1110, 840, 834, 434, 364, 293, 254, 202, 154, 151, 149, 112]}
  ]

  //prinos po kulturama
  barChartYieldPerCultureOptions = {
    responsive: true,
    scales: {
      xAxes: [
        {
          ticks: {
            beginAtZero: true
          },
          stacked: true
        }
      ],
      yAxes: [{
        stacked: true
   }]
    }
  };
  barChartYieldPerCultureLabels = ['KUKURUZ', 'MKC-SEMENSKA PŠENICA C-2', 'OZIMI JEČAM', 'AGROGLOBE-SEMENSKA PŠENICA C-1', 'OZIMA PŠENICA',
  'SOJA', 'SUNCOKRET', 'POSTRNA SOJA', 'POSTRNA SEMENSKA SOJA'];
  barChartYieldPerCultureType = 'horizontalBar';
  barChartYieldPerCultureLegend = false;
  barChartYieldPerCultureData = [
    {data: [11.8, 7.96, 7.55, 7.34, 7.19, 4.47, 4.32, 4.03, 2.90],  backgroundColor: '#73964a', hoverBackgroundColor: '#33490b'}
    
  ];

  //map

  lat = 45.588880;
  lng = 19.996049;
  map: any;

  markers = [
    {
      "id": "1",
      "worker": "Jockov Željko",
      "latWorker": "45.584951",
      "lngWorker": "19.984123",
      "machine": "IMT 533/539",
      "latMachine": "45.584951",
      "lngMachine": "19.984123",
      "table": "T-119",
      "lat1Table": "45.585607", 
      "lng1Table": "19.977274",
      "lat2Table": "45.590156", 
      "lng2Table": "19.979441",
      "lat3Table": "45.583342", 
      "lng3Table": "19.987478",
      "lat4Table": "45.583342",
      "lng4Table": "19.987478"
    },
    {
      "id": "2",
      "worker": "Jokanović Dragan",
      "latWorker": "45.588531",
      "lngWorker": "19.985576",
      "machine": "IMT-539",
      "latMachine": "45.588531",
      "lngMachine": "19.985576",
      "table": "T-120",
      "lat1Table": "45.590284", 
      "lng1Table": "19.979559",
      "lat2Table": "45.591731", 
      "lng2Table": "19.980150",
      "lat3Table": "45.589297", 
      "lng3Table": "19.990335",
      "lat4Table": "45.583443",
      "lng4Table": "19.987652"
    },
    {
      "id": "3",
      "worker": "Juhas Zoltan",
      "latWorker": "45.594654",
      "lngWorker": "19.990213",
      "machine": "JOHN DEERE 5510 N",
      "latMachine": "45.594766",
      "lngMachine": "19.989849",
      "table": "T-123",
      "lat1Table": "45.590438", 
      "lng1Table": "19.986123",
      "lat2Table": "45.589543", 
      "lng2Table": "19.990443",
      "lat3Table": "45.597602", 
      "lng3Table": "19.994150",
      "lat4Table": "45.598620",
      "lng4Table": "19.989914"
    },
    {
      "id": "4",
      "worker": "Juhas Žolt",
      "latWorker": "45.592272",
      "lngWorker": "19.993654",
      "machine": "JOHN DEERE 6620",
      "latMachine": "45.592232",
      "lngMachine": "19.993480",
      "table": "T-124",
      "lat1Table": "45.589497", 
      "lng1Table": "19.990642",
      "lat2Table": "45.588524", 
      "lng2Table": "19.994878",
      "lat3Table": "45.596583", 
      "lng3Table": "19.998650",
      "lat4Table": "45.597509",
      "lng4Table": "19.994481"
    },
    {
      "id": "5",
      "worker": "Kljajić Mićko",
      "latWorker": "45.599145",
      "lngWorker": "19.997025",
      "machine": "JOHN DEERE 6630",
      "latMachine": "45.599430",
      "lngMachine": "19.995514",
      "table": "T-125",
      "lat1Table": "45.597625", 
      "lng1Table": "19.994381",
      "lat2Table": "45.596675", 
      "lng2Table": "19.998749",
      "lat3Table": "45.601306", 
      "lng3Table": "20.000867",
      "lat4Table": "45.602232",
      "lng4Table": "19.996598"
    },
    {
      "id": "6",
      "worker": "Knežević Goran",
      "latWorker": "45.593899",
      "lngWorker": "20.003534",
      "machine": "JOHN DEERE 6820",
      "latMachine": "45.593899",
      "lngMachine": "20.003534",
      "table": "T-131",
      "lat1Table": "45.591037", 
      "lng1Table": "20.001139",
      "lat2Table": "45.589502", 
      "lng2Table": "20.007475",
      "lat3Table": "45.593993", 
      "lng3Table": "20.009642",
      "lat4Table": "45.595491", 
      "lng4Table": "20.003251"
    },
    {
      "id": "7",
      "worker": "Koletar Ervin",
      "latWorker": "45.572138",
      "lngWorker": "20.018587",
      "machine": "JOHN DEERE 8320",
      "latMachine": "45.572056",
      "lngMachine": "20.017773",
      "table": "T-132",
      "lat1Table": "45.573074", 
      "lng1Table": "20.017973",
      "lat2Table": "45.572403", 
      "lng2Table": "20.020951",
      "lat3Table": "45.570017", 
      "lng3Table": "20.016417",
      "lat4Table": "45.573074",
      "lng4Table": "20.017973"
    },
    {
      "id": "8",
      "worker": "Koletar Adrian",
      "latWorker": "45.571527",
      "lngWorker": "19.999292",
      "machine": "JOHN DEERE 8330",
      "latMachine": "45.571039",
      "lngMachine": "19.999640",
      "table": "T-133",
      "lat1Table": "45.571034", 
      "lng1Table": "19.996651",
      "lat2Table": "45.570200", 
      "lng2Table": "20.000787",
      "lat3Table": "45.573003", 
      "lng3Table": "20.002144",
      "lat4Table": "45.573906",
      "lng4Table": "19.998074"
    },
    {
      "id": "9",
      "worker": "Koletar Žolt",
      "latWorker": "45.575067",
      "lngWorker": "20.005452",
      "machine": "NEW HOLAND T 6.050",
      "latMachine": "45.574701",
      "lngMachine": "20.004115",
      "table": "T-134",
      "lat1Table": "45.573443", 
      "lng1Table": "20.002475",
      "lat2Table": "45.572424", 
      "lng2Table": "20.006711",
      "lat3Table": "45.575435", 
      "lng3Table": "20.008266",
      "lat4Table": "45.576385",
      "lng4Table": "20.003997"
    },
    {
      "id": "10",
      "worker": "Mađar Milenko",
      "latWorker": "45.580884",
      "lngWorker": "20.021958",
      "machine": "NEW HOLLAND T 7.210",
      "latMachine": "45.580233",
      "lngMachine": "20.021667",
      "table": "T-135",
      "lat1Table": "45.583519", 
      "lng1Table": "20.023014",
      "lat2Table": "45.583401", 
      "lng2Table": "20.023391",
      "lat3Table": "45.577915", 
      "lng3Table": "20.020903",
      "lat4Table": "45.578020",
      "lng4Table": "20.020395"
    },
    {
      "id": "11",
      "worker": "Matić Biljana",
      "latWorker": "45.585770",
      "lngWorker": "20.012857",
      "machine": "NEW HOLAND T 8.410",
      "latMachine": "45.585974",
      "lngMachine": "20.012509",
      "table": "T-136",
      "lat1Table": "45.585433", 
      "lng1Table": "20.008278",
      "lat2Table": "45.583605", 
      "lng2Table": "20.016601",
      "lat3Table": "45.591386", 
      "lng3Table": "20.020407",
      "lat4Table": "45.593470",
      "lng4Table": "20.011968"
    },
    {
      "id": "12",
      "worker": "Mesaroš Ištvan",
      "latWorker": "45.598657",
      "lngWorker": "20.024922",
      "machine": "",
      "latMachine": "",
      "lngMachine": "",
      "table": "T-137",
      "lat1Table": "45.597528", 
      "lng1Table": "20.023959",
      "lat2Table": "45.597379", 
      "lng2Table": "20.024608",
      "lat3Table": "45.599525", 
      "lng3Table": "20.025657",
      "lat4Table": "45.599656",
      "lng4Table": "20.025088"
    }
  ]

  

  onMapReady(map) {
    this.map = map;
  }

  //zatvaranje info prozora na klik drugog markera
  previous;
  elments: any[] = [];
  previousElement;

  clickedMarker(latitude, longitude, workerId) {
    this.lat = latitude;    
    this.lng = longitude;
   
    const element: HTMLElement = document.getElementById(workerId);
    this.renderer.setStyle(element, "background-color", "#ffc803");
    var currentElement = element.id;
    this.elments.push(currentElement);
    for(var i=0; i<this.elments.length; i++){
       this.previousElement = this.elments[i-1];
    }
    const elementP: HTMLElement = document.getElementById(this.previousElement);
    if(currentElement !== this.previousElement){
      this.renderer.setStyle(elementP, "background-color", "white");
    }
  }

  onChoseLocation(event){
    this.lat = event.coords.lat;    
    this.lng = event.coords.lng;
  }

  //iscrtavanje tabli
  paths: any[]
 
  onTableClick(lat1, lng1, lat2, lng2, lat3, lng3, lat4, lng4, workerId){
    this.lat = lat1 + ((lat3 - lat1) / 2);
    this.lng = lng1 + ((lng3 - lng1) / 2);
    this.paths = [
      {lat: lat1, lng: lng1},
      {lat: lat2, lng: lng2},
      {lat: lat3, lng: lng3},
      {lat: lat4, lng: lng4}
    ]
    
    const element: HTMLElement = document.getElementById(workerId);
    this.renderer.setStyle(element, "background-color", "#ffc803");
    var currentElement = element.id;
    this.elments.push(currentElement);
    for(var i=0; i<this.elments.length; i++){
       this.previousElement = this.elments[i-1];
    }
    const elementP: HTMLElement = document.getElementById(this.previousElement);
    if(currentElement !== this.previousElement){
      this.renderer.setStyle(elementP, "background-color", "white");
    }
  }

  //bounce marker
  workerLocationMarkerAnimation: string;
  markerAnimate(id){
    this.markers.forEach(data => {
      if(data.id !== id){
        this.workerLocationMarkerAnimation = null;
      }
      else{
        this.workerLocationMarkerAnimation = 'BOUNCE';
      }
    })
  }
    
  ngOnInit() {
     localStorage["workOrders"] = JSON.stringify(this.tempJSON);
  }

}
