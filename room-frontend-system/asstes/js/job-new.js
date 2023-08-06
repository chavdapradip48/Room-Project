// $(document).ready(function() {
//   $(".form-control").blur(function(){
    
//         if ($(this).val() === '') {
//           $(this).addClass('error');
//           $(this).append('<p class="error-message">This field is required.</p>');
//         } else {
//           console.log("Helo");
//           $(this).children('.form-control').hide();
//           $(this).parent().children('.error-message').hide()
//           $(this).children('.form-control').removeClass('error');
//           $(this).children('.error-message').remove();
//         }
      
//   });
// });

function operationsCheck() {  
    var jobName = new URLSearchParams(window.location.search).get('jobName');
    
    if (jobName != null && jobName != "") {
        updateJob(jobName);
    }
  }
  


$('.add-param-btn').on('click', addParamField);

$('#createJobForm').on('submit', handleCreateJobFormSubmit);


function addParamField() {
    addDynamicParam("", "");
}

function removeDynamicField(element){
    $(element).parent().parent().parent().remove()
}

function addDynamicParam(key, value) {    
    $('#dynamicParams').append(`<div class="input-group mb-3">
    <div class="col-md-5"><input type="text" class="form-control key-input" value="${key}" placeholder="Key"></div>
    <div class="col-md-5"><input type="text" class="form-control value-input" value="${value}" placeholder="Value"></div>
    
    <div class="col-md-1"><div class="input-group-append">
    <button class="btn btn-outline-secondary remove-param-btn" onclick="removeDynamicField(this)" type="button">-</button></div></div></div>`);
}  

function handleCreateJobFormSubmit(event) {
    event.preventDefault();

    // If form is valid, submit it
    if (!validateForm()) {
      return;
    }
    $('.card-body').loader('show');
    const jobData = {
      jobName: $('#jobName').val(),
      triggerName: $('#jobName').val(),
      jobClass: $('#jobClass').val(),
      cronExpression: $('#cronExpression').val(),
      jobData: {}
    };
  
    $('.input-group').each(function() {
      const key = $(this).find('.key-input').val();
      if (key !== '') {
        jobData.jobData[key] = $(this).find('.value-input').val();
      }
    });
  
    createJob(jobData, jobData.jobName, $('#isUpdate').val());
    resetForm();

    $('.card-body').loader('hide');
}

function resetForm() {
    $('.remove-param-btn').each(function() {
        $(this).parent().parent().parent().remove()    
    });
    $('#isUpdate').val('false');
    $('#job-submit').text('Create Job');
    $('.card-title').text('Create Job');
    $('#jobClass').val('0 0/1 * * * ?');
    $('#cronExpression').val('0 0/1 * * * ?');
    $('.key-input').val('');
    $('.value-input').val('');
    $('#jobName').prop('disabled', false).val('');
    $('#jobClass').prop('disabled', false).val('SimpleJob');
}

function createJob(jobData, jobName, isUpdate) {
    var methodApi = 'POST';
    var urlApi = backendServerUrl+ '/jobs';
    
    if (isUpdate === "true") {
      methodApi = 'PUT';
      urlApi += '/' + jobName;
    }
    
    $.ajax({
      url: urlApi,
      type: methodApi,
      headers: {
        'Authorization': getJwtTokenFromLocalStrorage(),
        'Content-Type': 'application/json'
      },
      data: JSON.stringify(jobData),
      success: function(createRes) {
        showToast(createRes.message, 'success');
        window.location.href = "/job-listing-fr.html"
      },
      error: function(xhr, textStatus, errorThrown) {
        showToast(xhr.responseJSON.message, 'error');
        // Handle error case here
      }
    });
}

function updateJob(jobName) {
    $.ajax({
      url: `${backendServerUrl}/jobs/${jobName}`,
      type: 'GET',
      headers: {
        'Authorization': getJwtTokenFromLocalStrorage(),
        'Content-Type': 'application/json',
      },
      success: function(singleJob) {
        $('#jobName').val(singleJob.data.jobName);
        $('#jobName').prop('disabled', true);
        $('#jobClass').prop('disabled', true);
        $('#isUpdate').val("true");
        $('#jobClass').val(singleJob.data.jobClass);
        $('#cronExpression').val(singleJob.data.cronExpression);
  
        const mapData = new Map(Object.entries(singleJob.data.jobData));

        mapData.forEach((value, key, map) => {
            const index = Array.from(map.keys()).indexOf(key);
            if(index === 0){
                $('.key-input').eq(index).val(key);
                $('.value-input').eq(index).val(value);
            } else {
                addDynamicParam(key, value);
            }
        });
        $('.card-title').text('Update Job');
        $('#job-submit').text('Update Job');
      }
    });
    removeQueryParams();
  }

function removeQueryParams(){
    var url = new URL(window.location.href);
    var searchParams = url.searchParams;
    searchParams.delete('jobName');
    var updatedUrl = url.origin + url.pathname + searchParams.toString();
    window.history.replaceState({ path: updatedUrl }, '', updatedUrl);
}

function getJobs(operationType) {
    const jobsContainer = $('#job-table-body');
    jobsContainer.empty();
    $.ajax({
      url: backendServerUrl +'/jobs',
      type: 'GET',
      headers: {
        'Authorization': getJwtTokenFromLocalStrorage()
      },
      success: function(jsonResponse) {
        const jobs = jsonResponse.data;
        setJobDataInTable(jobs,jobsContainer , operationType);

      },
      error: function(xhr, textStatus, errorThrown) {
        var errorResponse=xhr.responseJSON.message
        showToast(errorResponse, 'error');
        $('.table').html("<div class='alert alert-danger' role='alert'>"+ errorResponse +"</div>");
      }
    });
}

function setJobDataInTable(jobs, jobsContainer, operationType) {

  // const mapData = new Map(Object.entries(jobs.jobData));
  // console.log(mapData+"sfsd");

  jobs.forEach(function(job, index) {
    const mapData = new Map(Object.entries(jobs[index].jobData));
    var jobTurn = job.numberOfJobExecutingCount % mapData.size;
    console.log();
    console.log(Array.from(mapData.values())[jobTurn]);
    const row = $('<tr>');
    if(operationType === "User Friendly"){
      row.html(`
        <td data-title="SR No">${index+1}</td>
        <td data-title="Job Name">${job.jobName ?? '-'}</td>
        <td data-title="Cron Expression" >${job.cronExpression ?? '-'}</td>
        <td data-title="Current Turn">${job.currentTurn ?? '-'}</td>
        <td data-title="Next Turn">${job.nextTurn ?? '-'}</td>
        <td data-title="Total Exection">${job.numberOfJobExecutingCount ?? '-'}</td>
        <td data-title="Status">${job.status ?? '-'}</td>
        <td data-title="Actions" class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center">
          <div class="dropdown">
            <img src="asstes/images/three-dots-vertical.svg" onclick="showOptions(this,'show')" class="dropdown-toggle">
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <li><a class="dropdown-item" id="edit${job.jobName}row" onclick="pauseOrResumeJob('${job.jobName}','pause', '${operationType}')" >Pause</a></li>
              <li><a class="dropdown-item" id="update${job.jobName}row" onclick="pauseOrResumeJob('${job.jobName}','resume', '${operationType}')">Resume</a></li>
              <li><a class="dropdown-item" id="delete${job.jobName}row" href="create-job.html?jobName=${job.jobName}">Update</a></li>
              <li><a class="dropdown-item" id="delete${job.jobName}row" onclick="deleteJob('${job.jobName}', '${operationType}')">Delete</a></li>
            </ul>
          </div>
        </td>
      `);  
    } else {
      row.html(`
        <td data-title="SR No">${index+1}</td>
        <td data-title="Job Name">${job.jobName ?? '-'}</td>
        <td data-title="Trigger Name">${job.triggerName ?? '-'}</td>
        <td data-title="Job Class">${job.jobClass ?? '-'}</td>
        <td data-title="Cron Expression" >${job.cronExpression ?? '-'}</td>
        <td data-title="Job Start Time" >${job.jobStartTime ?? '-'}</td>
        <td data-title="Last Fire Time">${job.lastFireTime ?? '-'}</td>
        <td data-title="Next Fire Time">${job.nextFireTime ?? '-'}</td>
        <td data-title="Status">${job.status ?? '-'}</td>
        <td data-title="Actions" class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center">
          <div class="dropdown">
            <img src="asstes/images/three-dots-vertical.svg" onclick="showOptions(this,'show')" class="dropdown-toggle">
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <li><a class="dropdown-item" id="edit${job.jobName}row" onclick="pauseOrResumeJob('${job.jobName}','pause')" >Pause</a></li>
              <li><a class="dropdown-item" id="update${job.jobName}row" onclick="pauseOrResumeJob('${job.jobName}','resume')">Resume</a></li>
              <li><a class="dropdown-item" id="delete${job.jobName}row" href="create-job.html?jobName=${job.jobName}">Update</a></li>
              <li><a class="dropdown-item" id="delete${job.jobName}row" onclick="deleteJob('${job.jobName}')">Delete</a></li>
            </ul>
          </div>
        </td>
      `);  
    }
    
    jobsContainer.append(row);
  });
}

function deleteJob(jobName, operationType) {
    $.ajax({
      url: `${backendServerUrl}/jobs/${jobName}`,
      type: 'DELETE',
      headers: {
        'Authorization': getJwtTokenFromLocalStrorage(),
        'Content-Type': 'application/json'
      },
      success: function(createRes) {
        showToast(createRes.message, 'success');
        getJobs(operationType);
      },
      error: function(xhr, textStatus, errorThrown) {
        showToast(xhr.responseJSON.message, 'error');
        
      }
    });
  }
  
function pauseOrResumeJob(jobName, action, operationType) {
    var apiUrl = `${backendServerUrl}/jobs/${jobName}`;
    if (action === "resume") {
      apiUrl += '/resume';
    } else {
      apiUrl += '/pause';
    }
  
    $.ajax({
      url: apiUrl,
      method: 'POST',
      headers: {
        'Authorization': getJwtTokenFromLocalStrorage(),
        'Content-Type': 'application/json'
      },
      success: function (data) {
        showToast(data.message, 'success');
        getJobs(operationType); // Reload the job listing after successful deletion
      },
      error: function (error) {
        console.error(error);
        // Handle error: display an error message or perform other actions
      }
    });
}