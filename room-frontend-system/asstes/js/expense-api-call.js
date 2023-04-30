function operationsCheck() {
  // Create a new instance of URLSearchParams and pass in the query string
  var queryParams = new URLSearchParams(window.location.search);

  // Get the value of a specific parameter
  var type = queryParams.get('type');
  var expenseId = queryParams.get('expenseId');
  if (type != null && type != "") {
    if (expenseId != null && expenseId != "") {
      getExpenseById(type, expenseId);
    }
  }
}

function getExpenseById(type, expenseId) {
  $('.card-body').loader('show');
  var myHeaders = new Headers();
  myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
  };

  fetch(backendServerUrl + "/user/expense/" + expenseId, requestOptions)
    .then(response => response.json())
    .then(result => {
      var apiData = result.data;
      if (result.status == 200 && result.data != '') {
        $("#fullname-dropdown").val(apiData.user.id);
        $("#description").val(apiData.description);
        $("#payment-mode").val(apiData.paymentMode);
        $("#amount").val(apiData.amount);
      }
      if (type == "view") {
        $("#fullname-dropdown").attr('disabled', 'disabled');
        $("#description").attr('disabled', 'disabled');
        $("#payment-mode").attr('disabled', 'disabled');
        $("#amount").attr('disabled', 'disabled');
        $(".btn").css('display', 'none');
        $(".card-title").text("View Expense");
      }
      else {
        $(".card-title").text("Edit Expense");
        $(".btn").val("Update");
      }
      $('.card-body').loader('hide');
    })
    .catch(error => showToast("Expense not fetched", 'error'));

}


function operations(type, id, element) {
  if (type == "view") {
    window.location.href = "create-expense.html?expenseId=" + id + "&type=" + type;
  }
  else if (type == "edit") {
    window.location.href = "create-expense.html?expenseId=" + id + "&type=" + type;
  }
  else if (type == "delete") {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

    var requestOptions = {
      method: 'DELETE',
      headers: myHeaders,
    };

    fetch(backendServerUrl + "/user/expense/" + id, requestOptions)
      .then(response => response.json())
      .then(result => {
        if (result.status == 200 && result.data != '') {
          showToast(result.message, 'success');
          $('#delete' + id + 'row').parents().eq(4).remove();
          if ($('tbody tr').length == 0) {
            $('.table').html("<div class='alert alert-danger' role='alert'>Expenses are not available in system.</div>");
          }
        }
        else {
          showToast("Expense not deleted", 'error');
        }
      })
      .catch(error => showToast("Expense not deleted", 'error'));

  }
  $(".dropdown-menu.show").removeClass("show");
}

function getExpenses() {
  $('.card-body').loader('show');
  var apiUrl=backendServerUrl + "/user";
  var tokenId=decodeJwt(window.sessionStorage.getItem("token")).id;
  var operationType=new URLSearchParams(window.location.search).get('type');
  if(operationType == "my"){
    apiUrl+= "/"+tokenId+"/expense";
    $(".card-title").text("My Expenses")
  }
  else{
    apiUrl+= "/expense";
  }

  var settings = {
    "url": apiUrl,
    "method": "GET",
    "timeout": 0,
    "headers": {
      "Authorization": window.sessionStorage.getItem("token")
    },
  };

  $.ajax(settings)
    .done(function (response) {
      if (response.status == 200 && response.data != '') {
        if (response.data.length != 0) {
          var rows = "";
          $.each(response.data, function (i, expense) {
            var date = new Date(expense.createdAt).toLocaleDateString('en-IN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
            rows += `
          <tr>
            <td data-title="SR NO"class="col-xs-1 col-sm-1 col-md-1 col-lg-1  ">${i + 1}</td>
            <td data-title="Full Name">${expense.user.firstName} ${expense.user.lastName}</td>
            <td data-title="Payment Mode">${expense.paymentMode}</td>
            <td data-title="Amount">${expense.amount}</td>
            <td data-title="Description">${expense.description}</td>
            <td data-title="Created At">${date}</td>
            <td data-title="Actions" class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center">
              <div class="dropdown">
                <img src="asstes/images/three-dots-vertical.svg" onclick="showOptions(this,'show')" class="dropdown-toggle">
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                  <li><a class="dropdown-item" id="edit${expense.id}row" onclick="operations('view',${expense.id},this)" >View</a></li>
                  <li><a class="dropdown-item" id="update${expense.id}row" onclick="operations('edit',${expense.id},this)">Update</a></li>
                  <li><a class="dropdown-item" id="delete${expense.id}row" onclick="operations('delete',${expense.id},this)">Delete</a></li>
                </ul>
              </div>
            </td>
          </tr>
        `;
          });
          $('.table tbody').html(rows);
        }
        $('.card-body').loader('hide');
      }
    })
    .fail(function (jqXHR, textStatus, errorThrown) {
      var errorResponse=jqXHR.responseJSON.message;
      showToast(errorResponse, 'error');
      $('.table').html("<div class='alert alert-danger' role='alert'>"+errorResponse+"</div>");
      $('.card-body').loader('hide');
    });
}

function loadUsername() {
  $('.fullname').loader('show');
  var myHeaders = new Headers();
  myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };
  const nameSelect = $('#fullname-dropdown');
  fetch(backendServerUrl + "/user?projection=CreateUserPage", requestOptions)
    .then(result => result.json())
    .then(response => {

      if (response.status == 200 && response.data != '') {
        // Iterate over the response data and add rows to the table
        response.data.forEach(user => {
          const row = `
                <option value='${user.id}'>${user.firstName} ${user.lastName}</option>
            `;
          nameSelect.append(row);
        });
      }
      $('.fullname').loader('hide');
    })
    .catch(error => {
      $('.fullname').loader('hide');
      showToast("Names are not setted in dropdown", 'error');
    });
}

function createExpense() {
  $('.card-body').loader('show');
  var methodType = 'POST';
  var apiUrl = backendServerUrl + "/user/" + $("#fullname-dropdown").val() + "/expense";
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", window.sessionStorage.getItem("token"));
  var dataForSave = {
    "paymentMode": $("#payment-mode").val(),
    "amount": $("#amount").val(),
    "description": $("#description").val()
  };

  if (new URLSearchParams(window.location.search).get("type") == "edit") {
    apiUrl += "/" + new URLSearchParams(window.location.search).get("expenseId");
    methodType = 'PUT'
  }

  var raw = JSON.stringify(dataForSave);

  var requestOptions = {
    method: methodType,
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };

  fetch(apiUrl, requestOptions)
    .then(response => response.json())
    .then(result => {
      if (result.status == 200 && result.data != '') {
        showToast(result.message, 'success');
      }
      else {
        showToast("Expense not added", 'error');
        window.location.reload();
      }
      $('.card-body').loader('hide');
    })
    .catch(error => {
      showToast("Expense not added", 'error');
      $('.card-body').loader('hide');
    });
  $("#amount").val("")
  $("#description").val("")

}

function showOptions(element, operation) {
  $(element).siblings(".dropdown-menu").toggleClass(operation);
}
