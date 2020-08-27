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

  ngOnInit() {
     
  }

}
