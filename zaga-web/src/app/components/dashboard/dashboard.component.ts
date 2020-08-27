import { Component, OnInit } from '@angular/core';
import { ChartsModule } from 'ng2-charts';
import { SelectControlValueAccessor } from '@angular/forms';
import * as Chart from 'chart.js';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

  constructor() { }
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

  //bounce marker
  userLocationMarkerAnimation: string;
  markerAnimate(){
    if (this.userLocationMarkerAnimation !== null) {
      this.userLocationMarkerAnimation = null;
    } else {
      this.userLocationMarkerAnimation = 'BOUNCE';
    }
  }
    
  ngOnInit() {
     localStorage["workOrders"] = JSON.stringify(this.tempJSON);
  }

}
