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
  myHeaders.append("Authorization", getJwtTokenFromLocalStrorage());

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
        $("#datetime").val(new Date(apiData.createdAt).toLocaleString('sv-SE', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(' ', 'T'));
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
      $("#datetime").attr('disabled', 'disabled');
      $('.card-body').loader('hide');
    })
    .catch(error => showToast("Expense not fetched", 'error'));
}

function getExpenseAndSetUserSession(expenseId) {

  var settings = {
    "url": backendServerUrl + "/user/expense/" + expenseId,
    "method": "GET",
    "headers": {
      "Authorization": getJwtTokenFromLocalStrorage(),
    },
  };

  $.ajax(settings).done(function (response) {
    var sessionUser = result.data;
    if (result.status == 200 && result.data != '') {
      localStorage.setItem('session_user', JSON.stringify(sessionUser));
    }
  });
}

function operations(type, id) {
  if (type == "view") {
    window.location.href = "create-expense.html?expenseId=" + id + "&type=" + type;
  }
  else if (type == "edit") {
    window.location.href = "create-expense.html?expenseId=" + id + "&type=" + type;
  }
  else if (type == "delete") {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", getJwtTokenFromLocalStrorage());

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
  var apiUrl = backendServerUrl + "/user";
  var tokenId = decodeJwt(getJwtTokenFromLocalStrorage()).id;
  var operationType = new URLSearchParams(window.location.search).get('type');
  if (operationType == "my") {
    apiUrl += "/" + tokenId + "/expense";
    $(".card-title").text("My Expenses")
  }
  else {
    apiUrl += "/expense";
  }

  var settings = {
    "url": apiUrl,
    "method": "GET",
    "timeout": 0,
    "headers": {
      "Authorization": getJwtTokenFromLocalStrorage()
    },
  };

  $.ajax(settings)
    .done(function (response) {
      if (response.status == 200 && response.data != '') {
        if (response.data.length != 0) {
          $('#no-more-tables').show();
          $('#loading-message').remove();
          var rows = "";
          $.each(response.data, function (i, expense) {
            var date = new Date(expense.createdAt).toLocaleDateString('en-IN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
            rows += `
          <tr>
            <td data-title="SR NO"class="col-xs-1 col-sm-1 col-md-1 col-lg-1  ">${i + 1}</td>
            <td data-title="Full Name">${expense.user.fullName}</td>
            <td data-title="Payment Mode">${expense.paymentMode}</td>
            <td data-title="Amount">${expense.amount}</td>
            <td data-title="Description">${expense.description}</td>
            <td data-title="Created At">${date}</td>
            <td data-title="Actions" class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center">
              <div class="dropdown">
                <img src="asstes/images/three-dots-vertical.svg" onclick="showOptions(this,'show')" class="dropdown-toggle">
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                  <li><a class="dropdown-item" id="edit${expense.id}row" onclick="operations('view',${expense.id})" >View</a></li>
                  <li><a class="dropdown-item" id="update${expense.id}row" onclick="operations('edit',${expense.id})">Update</a></li>
                  <li><a class="dropdown-item" id="delete${expense.id}row" onclick="operations('delete',${expense.id})">Delete</a></li>
                </ul>
              </div>
            </td>
          </tr>
        `;
          });
          $('.table#user-table tbody').html(rows);
        }
        $('#user-table').DataTable({
          paging: true,
          searching: true
        });
        $('.card-body').loader('hide');
      }
    })
    .fail(function (jqXHR, textStatus, errorThrown) {
      var errorResponse = jqXHR.responseJSON.message;
      $('#no-more-tables').show();
      $('#loading-message').remove();
      showToast(errorResponse, 'error');
      $('.table').html("<div class='alert alert-danger' role='alert'>" + errorResponse + "</div>");
      $('.card-body').loader('hide');
    });
}

function loadUsername() {

  var loadUsers = window.localStorage.getItem("load-users");

  if (loadUsers !== null) {
    setDataDrop(JSON.parse(loadUsers));
    return;
  }

  $('.fullname').loader('show');

  var myHeaders = new Headers();
  myHeaders.append("Authorization", getJwtTokenFromLocalStrorage());

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  fetch(backendServerUrl + "/user?projection=Normal", requestOptions)
    .then(result => result.json())
    .then(response => {

      if (response.status == 200 && response.data != '') {
        setDataDrop(response.data);
        localStorage.setItem('load-users', JSON.stringify(response.data));
      }
      $('.fullname').loader('hide');
    })
    .catch(error => {
      $('.fullname').loader('hide');
      showToast("Names are not setted in dropdown", 'error');
    });
}

function setDataDrop(data) {
  const nameSelect = $('#fullname-dropdown');
  var current_user = JSON.parse(localStorage.getItem("session_user"));
  data.forEach(user => {
    if (user.id == current_user.id) {
      const row = `
      <option value='${user.id}' selected>${user.fullName}</option> `;
      nameSelect.append(row);
    }
    else {
      const row = `
            <option value='${user.id}'>${user.fullName}</option>
        `;
      nameSelect.append(row);
    }
  });
}

function createExpense() {
  if (!addressVlid($('#description').val(), $('#description-err')) ||
    !paymentAmountVlid($('#amount').val(), $('#amount-err'))) {
    return false;
  }

  $('.card-body').loader('show');
  var methodType = 'POST';
  var apiUrl = backendServerUrl + "/user/" + $("#fullname-dropdown").val() + "/expense";
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", getJwtTokenFromLocalStrorage());
  var userDate = $("#datetime").val();
  if (userDate !== "") {
    userDate = new Date(userDate).toUTCString();
  }

  var dataForSave = {
    "paymentMode": $("#payment-mode").val(),
    "amount": $("#amount").val(),
    "description": $("#description").val(),
    "createdAt": userDate
  };

  var editCondition = new URLSearchParams(window.location.search).get("type") == "edit";

  if (editCondition) {
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
        window.location.href = "expense-listing.html"
      }
      else {
        showToast("Expense not added", 'error');
        // window.location.reload();
      }
      $('.card-body').loader('hide');
    })
    .catch(error => {
      showToast("Expense not added", 'error');
      $('.card-body').loader('hide');
    });
  $("#amount").val("")
  $("#description").val("")
  $("#datetime").val("")

  if (editCondition) {
    window.location.href = "/expense-listing.html"
  }

}

function calcualteExpense() {
  $(".error-message").text("");
  // Validate Persons
  if ($("#persons").val() <= 0) {
    $("#persons-error").text("Please enter a valid number of persons.");
    return;
  }

  // Validate From Date
  if ($("#from-datetime").val() === "") {
    $("#from-datetime-error").text("Please select a valid From Date.");
    return;
  }

  // Validate To Date
  if ($("#to-datetime").val() === "") {
    $("#to-datetime-error").text("Please select a valid To Date.");
    return;
  }

  $('.card-body').loader('show');
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", getJwtTokenFromLocalStrorage());


  var requestOptions = {
    method: "POST",
    headers: myHeaders,
    body: JSON.stringify({
      "fixedMonthlyExpenses": {
        "safai vala massi": $("#safai-vala-massi").val(),
        "Room Rent": $("#room-rent").val(),
        "Electricity Bill": $("#electricity-bill").val(),
        "Other": $("#fixedOther").val()
      },
      "variableMonthlyExpenses": {
        "Water Bill": $("#water-bill").val(),
        "Other": $("#VariableOther").val()
      },
      "fullPersons": fullPersonSelected = fullPersonSelected.map(function (item) { return item.id; }),
      "halfPersons": halfPersonSelected = halfPersonSelected.map(function (item) { return item.id; }),
      "onVacationPersons": onVacationPersonSelected = onVacationPersonSelected.map(function (item) { return item.id; }),
      "from": new Date($("#from-datetime").val()).toUTCString(),
      "to": new Date($("#to-datetime").val()).toUTCString()
    }),
    redirect: 'follow'
  };
  halfperson.map(function (item) {
    return item.id;
  })
  fetch(backendServerUrl + "/user/expense/calculator/calculate?isStore=false", requestOptions)
    .then(response => response.json())
    .then(result => {
      console.log(result.status == 200 && result.data != '');
      if (result.status == 200 && result.data != '') {
        showToast(result.message, 'success');
        localStorage.setItem('result', JSON.stringify(result));
        window.location.href = "view-calculated-expenses.html";
      }
      else {
        showToast(result.message, 'error');
      }
      $('.card-body').loader('hide');
    })
    .catch(error => {
      showToast("Expense calculation is not done", 'error');
      $('.card-body').loader('hide');
    });
}

$("#fullPersonOptions").slideUp();
$("#halfPersonOptions").slideUp();
$("#onVacationPersonOptions").slideUp();
var getAllUser = window.localStorage.getItem('load-users');
if (getAllUser == "" || getAllUser == null) {
  loadUsername();
  for (; getAllUser == null || getAllUser == "";) {
    if (getAllUser != null || getAllUser != "") {
      getAllUser = window.localStorage.getItem('load-users');
    }
  }

}
var obj = JSON.parse(getAllUser);
var allPerson = obj.map(function (person) {
  return { id: person.id, fullName: person.fullName };
});

fullPerson = halfPerson = onVacationPerson = allPerson;
var fullPersonSelected, halfPersonSelected, onVacationPersonSelected;
$("#selectedfullPerson").click(fullPersonLoad);
function fullPersonLoad() {
  $("#fullPersonOptions").empty();
  fullPerson.forEach((e) => {
    addOption($("#fullPersonOptions"), e)
  });
  $("#fullPersonOptions").slideDown();
  $("#onVacationPersonOptions").slideUp();
}

$("#selectedhalfPerson").click(halfPersonLoad);
function halfPersonLoad() {
  fullPersonSelected = createSelectedPersonArray("#fullPersonOptions");
  halfPerson = RemoveDuplicateElement(fullPersonSelected, allPerson);
  $("#fullPersonOptions").slideUp();
  $("#halfPersonOptions").empty();
  halfPerson.forEach((e) => {
    addOption($("#halfPersonOptions"), e)
  });
  $("#halfPersonOptions").slideDown();
}
$("#selectedonVacationPerson").click(onVacationPersonLoad);
function onVacationPersonLoad() {
  halfPersonSelected = createSelectedPersonArray("#halfPersonOptions");
  onVacationPerson = RemoveDuplicateElement(mergeArrays(fullPersonSelected, halfPersonSelected), allPerson);
  $("#halfPersonOptions").slideUp();
  $("#onVacationPersonOptions").empty();
  onVacationPerson.forEach((e) => {
    addOption($("#onVacationPersonOptions"), e)
  });
  $("#onVacationPersonOptions").slideDown();
}
$("#from-datetime").click(() => {
  $("#onVacationPersonOptions").slideUp();
  onVacationPersonSelected = createSelectedPersonArray("#onVacationPersonOptions");
});
function RemoveDuplicateElement(fullPersonSelected, halfperson) {
  var idsToRemove = fullPersonSelected.map(function (item) {
    return parseInt(item.id, 10);
  });
  var filteredHalfPerson = halfperson.filter(function (item) {
    return !idsToRemove.includes(item.id);
  });
  return filteredHalfPerson;
}

function createSelectedPersonArray(part) {
  var selectedPersonSelected = [];
  $(`${part} input[type="checkbox"]:checked`).each(function () {
    var inputId = $(this).val();
    var labelText = $(this).parent().text().trim();
    var dataObject = { id: inputId, fullName: labelText };
    selectedPersonSelected.push(dataObject);
  });
  return selectedPersonSelected;
}

function mergeArrays(arr1, arr2) {
  const idMap = new Map();
  arr1.forEach(item => idMap.set(item.id, item));
  arr2.forEach(item => {
    if (!idMap.has(item.id)) {
      arr1.push(item);
    }
  });
  return arr1;
}

function addOption(PersonOptionsDiv, data) {
  const label = $('<label>');
  const input = $('<input>').attr('type', 'checkbox').val(data.id);
  const labelText = document.createTextNode(data.fullName);

  label.append(input);
  label.append(labelText);

  PersonOptionsDiv.append(label);
  PersonOptionsDiv.append('<br>');
}
