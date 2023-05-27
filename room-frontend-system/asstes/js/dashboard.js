/* global Chart, coreui */

/**
 * --------------------------------------------------------------------------
 * CoreUI Boostrap Admin Template (v4.2.2): main.js
 * Licensed under MIT (https://coreui.io/license)
 * --------------------------------------------------------------------------
 */

// Disable the on-canvas tooltip
Chart.defaults.pointHitDetectionRadius = 1;
Chart.defaults.plugins.tooltip.enabled = false;
Chart.defaults.plugins.tooltip.mode = 'index';
Chart.defaults.plugins.tooltip.position = 'nearest';
Chart.defaults.plugins.tooltip.external = coreui.ChartJS.customTooltips;
Chart.defaults.defaultFontColor = '#646470';
const random = (min, max) => Math.floor(Math.random() * (max - min + 1) + min);

const yData=['January', 'February', 'March', 'April', 'May', 'June', 'July'];
const xData=[114, 250, 150, 50, 10, 100, 100];
const maxGraph=Math.max(...xData);
console.log(maxGraph);
// eslint-disable-next-line no-unused-vars
const mainChart = new Chart(document.getElementById('main-chart'), {
  type: 'line',
  data: {
    labels: yData,
    datasets: [{
      label: 'My First dataset',
      backgroundColor: coreui.Utils.hexToRgba(coreui.Utils.getStyle('--cui-info'), 10),
      borderColor: coreui.Utils.getStyle('--cui-info'),
      pointHoverBackgroundColor: '#fff',
      borderWidth: 2,
      data: xData,
      fill: true
    }]
  },
  options: {
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false
      }
    },
    scales: {
      x: {
        grid: {
          drawOnChartArea: false
        }
      },
      y: {
        ticks: {
          beginAtZero: true,
          maxTicksLimit: 5,
          stepSize: Math.ceil(250 / 5),
          max: 250
        }
      }
    },
    elements: {
      line: {
        tension: 0.4
      },
      point: {
        radius: 0,
        hitRadius: 10,
        hoverRadius: 4,
        hoverBorderWidth: 3
      }
    }
  }
});
//# sourceMappingURL=main.js.map