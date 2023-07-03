$(document).ready(function() {
  loadJobs();
  scheduleTask();
  $('#create-job-form').on('submit', handleCreateJobFormSubmit);
});

function createJob(jobData, jobName, isUpdate) {
  var methodApi = 'POST';
  var urlApi = 'http://localhost:4564/jobs';
  
  if (isUpdate === "true") {
    methodApi = 'PUT';
    urlApi += '/' + jobName;
  }
  
  $.ajax({
    url: urlApi,
    type: methodApi,
    headers: {
      'Authorization': 'Bearer ' + getJwtToken(),
      'Content-Type': 'application/json'
    },
    data: JSON.stringify(jobData),
    success: function(createRes) {
      alert(createRes.message);
      resetForm();
      loadJobs();
    }
  });
}

function handleCreateJobFormSubmit(event) {
  event.preventDefault();

  const jobName = $('#jobName').val();
  const isUpdate = $('#isUpdate').val();
  const triggerName = $('#triggerName').val();
  const jobClass = $('#jobClass').val();
  const cronExpression = $('#cronExpression').val();

  const jobData = {
    jobName: jobName,
    triggerName: triggerName,
    jobClass: jobClass,
    cronExpression: cronExpression,
    jobData: {}
  };

  $('.input-group').each(function() {
    const keyInput = $(this).find('.key-input');
    const valueInput = $(this).find('.value-input');
    const key = keyInput.val();
    const value = valueInput.val();
    if (key !== '') {
      jobData.jobData[key] = value;
    }
  });

  createJob(jobData, jobName, isUpdate);
}

function loadJobs() {
  
  $.ajax({
    url: 'http://localhost:4564/jobs',
    type: 'GET',
    headers: {
      'Authorization': 'Bearer ' + getJwtToken()
    },
    success: function(jsonResponse) {
      const jobsContainer = $('#job-table-body');
      jobsContainer.empty();
      const jobs = jsonResponse.data;
      jobs.forEach(function(job) {
        
// var displayValue = job.lastFireTime || "-";
        const row = $('<tr>');
        var last=job.lastFireTime;
        console.log(last);
        var finaaa=last ?? '-'
        console.log(finaaa+"gf");
        row.html(`
          <td>${job.jobName}</td>
          <td>${job.triggerName}</td>
          <td>${job.jobClass}</td>
          <td>${job.cronExpression}</td>
          <td>${job.jobStartTime}</td>
          <td>${job.lastFireTime || "-"} </td>
          <td>${job.nextFireTime}</td>
          <td>${job.status}</td>
          <td>
            <button onclick="pauseOrResumeJob('${job.jobName}','pause')">Pause</button>
            <button onclick="pauseOrResumeJob('${job.jobName}','resume')">Resume</button>    
            <button onclick="updateJob('${job.jobName}')">Update</button>
            <button onclick="deleteJob('${job.jobName}')">Delete</button>
          </td>
        `);
        jobsContainer.append(row);
      });
    }
  });
}

function updateJob(jobName) {
  $.ajax({
    url: `http://localhost:4564/jobs/${jobName}`,
    type: 'GET',
    headers: {
      'Authorization': 'Bearer ' + getJwtToken(),
      'Content-Type': 'application/json',
      'Cookie': 'COOKIE_SUPPORT=true; GUEST_LANGUAGE_ID=en_US'
    },
    success: function(singleJob) {
      $('#jobName').val(singleJob.data.jobName);
      $('#jobName').prop('disabled', true);
      $('#isUpdate').val("true");
      $('#triggerName').val(singleJob.data.triggerName);
      $('#jobClass').val(singleJob.data.jobClass);
      $('#cronExpression').val(singleJob.data.cronExpression);

      const jobData = singleJob.data.jobData;
      $('.input-group').each(function(index) {
        if (jobData.hasOwnProperty(index)) {
          const keyInput = $(this).find('.key-input');
          const valueInput = $(this).find('.value-input');
          keyInput.val(index);
          valueInput.val(jobData[index]);
        }
      });
    }
  });
}

function removeQueryParams(){
  var url = new URL(window.location.href);
  var searchParams = url.searchParams;
  searchParams.delete('jobName');
  var updatedUrl = url.origin + url.pathname + searchParams.toString();
  window.history.replaceState({ path: updatedUrl }, '', updatedUrl);
}

  // Helper function to add a dynamic parameter input field
  function addDynamicParam(key, value) {
    const dynamicParams = document.getElementById('dynamicParams');

    // Create new input fields
    const keyInput = document.createElement('input');
    keyInput.type = 'text';
    keyInput.classList.add('form-control', 'key-input');
    keyInput.placeholder = 'Key';
    keyInput.value = key;
  
    const valueInput = document.createElement('input');
    valueInput.type = 'text';
    valueInput.classList.add('form-control', 'value-input');
    valueInput.placeholder = 'Value';
    valueInput.value = value;
    // valueInput.value = 'Value';
  
    // Create a new input group div and append key-value input fields
    const inputGroup = document.createElement('div');
    inputGroup.classList.add('input-group', 'mb-3', 'row');
    
    const keyInputGroup = document.createElement('div');
    keyInputGroup.classList.add('col-md-5');
    keyInputGroup.appendChild(keyInput);
    
    const valueInputGroup = document.createElement('div');
    valueInputGroup.classList.add('col-md-5');
    valueInputGroup.appendChild(valueInput);
    
    inputGroup.appendChild(keyInputGroup);
    inputGroup.appendChild(valueInputGroup);
  
    // Create a new remove button
    const removeBtn = document.createElement('button');
    removeBtn.classList.add('btn', 'btn-outline-secondary', 'remove-param-btn');
    removeBtn.type = 'button';
    removeBtn.innerText = '-';
  
    // Create a new column div for the remove button
    const removeCol = document.createElement('div');
    removeCol.classList.add('col-md-2');
    
    const removeInputGroup = document.createElement('div');
    removeInputGroup.classList.add('input-group-append');
    removeInputGroup.appendChild(removeBtn);
    removeCol.appendChild(removeInputGroup);
    
    inputGroup.appendChild(removeCol);
  
    // Add event listener to the remove button to remove the input fields
    removeBtn.addEventListener('click', function() {
      dynamicParams.removeChild(inputGroup);
    });
  
    // Append the input group to the dynamicParams container
    dynamicParams.appendChild(inputGroup);
  }  


function deleteJob(jobName) {
  $.ajax({
    url: `http://localhost:4564/jobs/${jobName}`,
    type: 'DELETE',
    headers: {
      'Authorization': 'Bearer ' + getJwtToken(),
      'Content-Type': 'application/json'
    }
  })
    .done(function(data) {
      alert(JSON.parse(data).message); // Display success message or perform other actions
      loadJobs(); // Reload the job listing after successful deletion
    })
    .fail(function(error) {
      console.error(error);
      // Handle error: display an error message or perform other actions
    });
}

function pauseOrResumeJob(jobName, action) {
  var apiUrl = `http://localhost:4564/jobs/${jobName}`;
  if (action === "resume") {
    apiUrl += '/resume';
  } else {
    apiUrl += '/pause';
  }

  $.ajax({
    url: apiUrl,
    method: 'POST',
    headers: {
      'Authorization': 'Bearer ' + getJwtToken(),
      'Content-Type': 'application/json'
    },
    success: function (data) {
      alert(data.message); // Display success message or perform other actions
      loadJobs(); // Reload the job listing after successful deletion
    },
    error: function (error) {
      console.error(error);
      // Handle error: display an error message or perform other actions
    }
  });
}

function resetForm() {
  $('#isUpdate').val('false');
  $('#job-submit').text('Create Job');
  $('.key-input').not(':first').closest('.form-group').remove();
  $('.value-input').not(':first').closest('.form-group').remove();
  $('#jobClass').val('');
  $('#cronExpression').val('');
  $('.key-input').val('');
  $('.value-input').val('');
  $('#jobName').prop('disabled', false).val('');
  $('#triggerName').prop('disabled', false).val('');
}

function getJwtToken() {
  var token = localStorage.getItem('jwt-token');
  if (token == null || token === '') {
    var myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    var raw = JSON.stringify({
      'email': 'default@gmail.com',
      'password': '99097@Pradip'
    });

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

    fetch('http://localhost:4564/authenticate', requestOptions)
      .then(response => response.json())
      .then(result => {
        localStorage.setItem('jwt-token', result.jwt);
      })
      .catch(error => {
        console.log('error', error);
        localStorage.removeItem('jwt-token');
        // getJwtToken();
      });
  }

  // Replace this with your logic to get the JWT token from the local storage or wherever it is stored
  return localStorage.getItem('jwt-token');
}

function addParamField() {
  addDynamicParam("", "");
}

$('.add-param-btn').on('click', addParamField);

$('#createJobForm').on('submit', handleCreateJobFormSubmit);

function scheduleTask() {
  $.ajax({
    url: 'http://localhost:4564/jobs/scheduler-job-notifications',
    method: 'GET',
    headers: {
      'Authorization': 'Bearer ' + getJwtToken(),
    },
    success: function(response) {
      var notifications = response.data;
      notifications.forEach(function(job) {
        var jobName = job.jobName;
        var delay = job.delay;

        Notification.requestPermission().then(function(permission) {
          if (permission === 'granted') {
            setTimeout(function() {
              showNotification(jobName);
            }, delay);
          } else {
            setCustomInterval(jobName, function() {
              alert("Hello! " + jobName + " Job is executed.");
            }, delay);
          }
        });
      });
    },
    error: function(error) {
      console.error('Error:', error);
    }
  });
}

function showNotification(jobName) {
  const notification = new Notification('Breaking:', {
    body: `Next turn is Pradip's and Sandip's for ${jobName}`,
    icon: 'asstes/images/notification.jpg',
  });
  notification.addEventListener('click', function() {
    window.open('localhost:8080');
  });
}

function setCustomInterval(id, callback, interval) {
  if (window.customIntervals && window.customIntervals[id]) {
    console.error("Custom interval ID already exists:", id);
    return;
  }

  var intervalId = setInterval(callback, interval);

  // Store the interval ID with the custom ID
  window.customIntervals = window.customIntervals || {};
  window.customIntervals[id] = intervalId;
}

function clearCustomInterval(id) {
  var intervalId = window.customIntervals[id];

  if (intervalId) {
    clearInterval(intervalId);
    delete window.customIntervals[id];
  }
}

