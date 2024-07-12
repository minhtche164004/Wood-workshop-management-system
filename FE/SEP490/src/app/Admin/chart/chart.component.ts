import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnInit {
  chart: any = [];
  ngOnInit(): void {
    this.chart = new Chart('canvas', {
      type: 'line',
      data: {
        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
        datasets: [
          {
            label: 'My First dataset',
            data: [12, 19, 3, 5, 2, 3],
            borderColor: '#3cba9f',

          },
          {
            label: 'My second dataset',
            data: [2, 3, 5, 2, 3, 12],
            borderColor: '#ffcc00',

          }
        ]
      },
    });

    this.chart = new Chart('canvas3', {
      type: 'bar',
      data: {
        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
        datasets: [
          {
            label: 'My First dataset',
            data: [12, 19, 3, 5, 2, 3],
            borderColor: '#3cba9f',

          },
          {
            label: 'My second dataset',
            data: [2, 3, 5, 2, 3, 12],
            borderColor: '#ffcc00',

          }
        ]
      },
    });
    this.chart = new Chart('canvas2', {
      type: 'pie',
      data: {
        labels: ['Red', 'Blue', 'Yellow' ],
        datasets: [
          {
            label: 'My First dataset',
            data: [23,43,34],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
              'rgb(255, 205, 86)'
            ],
            hoverOffset: 4
          }
       
        ]
      },

    });

  }
}

