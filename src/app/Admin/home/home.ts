import { Component } from '@angular/core';
import { HighchartsChartComponent } from 'highcharts-angular';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-home',
  imports: [HighchartsChartComponent],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

  attandance = [
    { name: 'Java', count: 10 },
    { name: '.Net', count: 12 },
    { name: 'DevOps', count: 20 },
    { name: 'Cyber Security', count: 21 }
  ];

  trackers = [
    {
      name: 'Java',
      count: 18,
      change: '+14%',
      icon: '⚡',
      badge: 'Active Track',
      color: '#6366f1'
    },
    {
      name: '.Net',
      count: 12,
      change: '+8%',
      icon: '🌐',
      badge: 'Steady Growth',
      color: '#06b6d4'
    },
    {
      name: 'DevOps',
      count: 20,
      change: '+22%',
      icon: '🚀',
      badge: 'High Demand',
      color: '#10b981'
    },
    {
      name: 'Cyber Security',
      count: 21,
      change: '+19%',
      icon: '🛡️',
      badge: 'Top Enrolled',
      color: '#f59e0b'
    }
  ];

  totalCandidates = this.trackers.reduce((acc, curr) => acc + curr.count, 0);

  chartOptions: Highcharts.Options = {
    chart: {
      type: 'column',
      backgroundColor: 'transparent',
      style: {
        fontFamily: "'Inter', sans-serif"
      }
    },

    title: {
      text: 'Trackers Enrolled Distribution',
      style: {
        fontSize: '16px',
        fontWeight: '700',
        color: '#1e293b'
      }
    },

    subtitle: {
      text: 'Total Active Trainees per Tech Stream',
      style: {
        fontSize: '13px',
        color: '#64748b'
      }
    },

    xAxis: {
      categories: this.trackers.map(t => t.name),
      lineColor: '#e2e8f0',
      labels: {
        style: {
          color: '#475569',
          fontWeight: '600'
        }
      }
    },

    yAxis: {
      title: {
        text: 'Total Candidates',
        style: {
          color: '#64748b'
        }
      },
      gridLineColor: '#f1f5f9'
    },

    tooltip: {
      backgroundColor: '#0f172a',
      borderRadius: 12,
      borderWidth: 0,
      shadow: true,
      style: {
        color: '#f8fafc'
      },
      pointFormat: '<span style="color:{point.color}">\u25CF</span> <b>{point.y} Candidates</b>'
    },

    series: [
      {
        name: 'Trackers',
        type: 'column',
        colorByPoint: true,
        colors: ['#6366f1', '#06b6d4', '#10b981', '#f59e0b'],
        data: this.trackers.map(t => t.count),
        showInLegend: false
      }
    ],

    plotOptions: {
      column: {
        borderRadius: 8,
        pointPadding: 0.15,
        groupPadding: 0.15,
        borderWidth: 0
      }
    },

    credits: {
      enabled: false
    }
  };

  attandancechartOptions: Highcharts.Options = {
    chart: {
      type: 'pie',
      backgroundColor: 'transparent',
      style: {
        fontFamily: "'Inter', sans-serif"
      }
    },
    title: {
      text: 'Attendance Overview',
      style: {
        fontSize: '16px',
        fontWeight: '700',
        color: '#1e293b'
      }
    },
    subtitle: {
      text: 'Active Attendance per Batch',
      style: {
        fontSize: '13px',
        color: '#64748b'
      }
    },
    tooltip: {
      backgroundColor: '#0f172a',
      borderRadius: 12,
      borderWidth: 0,
      shadow: true,
      style: {
        color: '#f8fafc'
      },
      pointFormat: '<b>{point.y} Attended</b> ({point.percentage:.1f}%)'
    },
    series: [
      {
        name: 'Attendance',
        type: 'pie',
        colors: ['#6366f1', '#06b6d4', '#10b981', '#f59e0b'],
        data: this.attandance.map(t => ({
          name: t.name,
          y: t.count
        }))
      }
    ],
    plotOptions: {
      pie: {
        innerSize: '65%',
        borderWidth: 2,
        borderColor: '#ffffff',
        dataLabels: {
          enabled: true,
          format: '<b>{point.name}</b>: {point.y}',
          style: {
            fontSize: '12px',
            color: '#334155',
            fontWeight: '600'
          }
        }
      }
    },
    credits: {
      enabled: false
    }
  };
}