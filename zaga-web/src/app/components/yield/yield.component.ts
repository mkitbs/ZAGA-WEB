import {Component, OnInit} from '@angular/core';


@Component({
    selector: 'app-yield',
    templateUrl: './yield.component.html',
    styleUrls: ['./yield.component.css']
})

export class YieldComponent implements OnInit{

    constructor() { }

    pieChartLabels = ['Kukuruz', 'Pšenica', 'Soja'];
    pieChartData = [120, 150, 180];
    pieChartType = 'pie';
    optionsPieChart = {
        plugins: {
            datalabels:{
                display: false
            },
            labels: {
                render: 'percentage',
                fontColor: 'white',
                precision: 2
              }
        }
    };

    chartColors: any[] = [{ backgroundColor:["#5C7495", "#f68901", "#ffc803", "#73964a", "#33490b"] }];

    barChartOptions = {
        responsive: true,
            scales: {
                xAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            },
            plugins: {
                datalabels: {
                   display: true,
                   align: 'center',
                   anchor: 'center',
                   color: 'white'
                }
             }
    };
    barChartLabels = ['Kukuruz', 'Pšenica', 'Soja'];
    barChartType = 'horizontalBar';
    barChartLegend = false;
    barChartData = [
        {data: [8, 5, 12]}
    ];

    ngOnInit() {

    }
}