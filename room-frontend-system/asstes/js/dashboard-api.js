var yData=["Jan","Feb","March","April","May"];
var xData=[29540,30450,25435,38790,20435];

async function getDashboardData() {
  // $('body').loader('show');
  var myHeaders = new Headers();
  myHeaders.append("Authorization", getJwtTokenFromLocalStrorage());

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
  };

  try {
    const response = await fetch(backendServerUrl + "/user/expense/dashboard", requestOptions);
    const result = await response.json();
    var apiData = result.data;

    if (result.status == 200 && result.data != '') {
      $("#total-exp").text("RS. " + apiData.totalAmount);
      $("#my-total-exp").text("RS. " + apiData.myTotalAmount);
      $("#previous-exp").text("RS. " + apiData.previousMonthAmount);
      $("#current-prev-exp-percent").text(apiData.currentPreviousMonthPercent + "%");
     // yData = Object.keys(apiData.graphData);
    //  xData = Object.values(apiData.graphData);
      $("#month-data").text(yData[0]+" - "+yData[yData.length-1] + " 2023");
    }

    // $('body').loader('hide');
  } catch (error) {
    showToast("Expense not fetched", 'error');
    // $('body').loader('hide');
  }
}

function setdataInGraph(){
  Chart.defaults.pointHitDetectionRadius = 1;
Chart.defaults.plugins.tooltip.enabled = false;
Chart.defaults.plugins.tooltip.mode = 'index';
Chart.defaults.plugins.tooltip.position = 'nearest';
Chart.defaults.plugins.tooltip.external = coreui.ChartJS.customTooltips;
Chart.defaults.defaultFontColor = '#646470';
const maxGraph=Math.max(...xData);
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
}
