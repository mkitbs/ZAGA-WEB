import { Component, OnInit } from '@angular/core';
import { ChartsModule } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

  constructor() { }

  pieChartLabels = ['Kukuruz', 'Pšenica', 'Soja'];
  pieChartData = [120, 150, 180];
  pieChartType = 'pie';
  optionsPieChart = {
    plugins: {
      datalabels: {
          formatter: () => {
              let sum = 0;
              this.pieChartData.forEach(data =>{
                sum += data;
              })
              this.pieChartData.forEach(data =>{
                let percentage = (data*100 / sum).toFixed(2)+"%";
                return percentage;
              })
              
          },
          color: '#fff',
      }
  }
  };
  chartColors: any[] = [{ backgroundColor:["#5C7495", "#f68901", "#ffc803", "#73964a", "#33490b"] }];

  barChartOptions = {
    responsive: true,
    scales: {
      xAxes: [
        {
          ticks: {
            beginAtZero: true
          }
        }
      ]
    }
  };
  barChartLabels = ['Kukuruz', 'Pšenica', 'Soja'];
  barChartType = 'horizontalBar';
  barChartLegend = false;
  barChartData = [
    {data: [84000, 50000, 123000]}
    
  ];
  
  ngOnInit() {
     
  }

}
