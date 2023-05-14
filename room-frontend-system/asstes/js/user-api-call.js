$(document).ready(function () {
  getUserListing();
});

function getUserById(id) {
  var settings = {
    "url": backendServerUrl + "/user/"+id+"?projection=UserDTO",
    "method": "GET",
    "timeout": 0,
    "headers": {
      "Authorization": window.sessionStorage.getItem("token")
    },
  };

  return $.ajax(settings);
}

function deleteUser(userId) {
  var settings = {
    "url": backendServerUrl + "/user/"+userId,
    "method": "DELETE",
    "timeout": 0,
    "headers": {
      "Authorization": window.sessionStorage.getItem("token")
    },
  };

  return $.ajax(settings)
    .then(function(response) {
      showToast(response.message, 'error');
      return true;
    })
    .catch(function(error) {
      showToast(error, 'error');
      return false;
    });
}

function getUserListing() {
  $('body').loader('show');

  var settings = {
    "url": backendServerUrl + "/user",
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

          $.each(response.data, function (i, user) {
            var userImage="asstes/images/default-profile-image.png";
            if(user.profilePhoto != null && user.profilePhoto != ""){
              userImage=user.profilePhoto;
            }
            rows += `
            <div class="col-sm-6 col-md-4 col-lg-3">
              <div class="card">
                <img class="card-img-top" src="${userImage}" alt="Card image cap">
                <div class="card-body">
                  <div class="row">
                    <div class="col-12">
                      <h5 class="card-title">${user.firstName} ${user.lastName}</h5>
                      <p class="card-text">${user.email}</p>
                    </div>

                    <div class="col-8 profile-button">
                      <a href="/profile.html?userId=${user.id}" class="btn btn-primary" id="view-user-profile">View Profile</a>
                    </div>
                    <div class="col-4 profile-button">
                      <div class="dot ${user.enabled ? 'active' : 'inactive'}"></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          
        `;
          });
          $('#dynamic-user-listing').html(rows);
        }
        $('body').loader('hide');
      }
    })
    .fail(function (jqXHR, textStatus, errorThrown) {
      var errorResponse=jqXHR.responseJSON.message;
      showToast(errorResponse, 'error');
      $('#dynamic-user-listing').html("<div class='alert alert-danger' role='alert'>"+errorResponse+"</div>");
      $('body').loader('hide');
    });
}

// function loadUsername() {
//   $('.fullname').loader('show');
//   var myHeaders = new Headers();
//   myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

//   var requestOptions = {
//     method: 'GET',
//     headers: myHeaders,
//     redirect: 'follow'
//   };
//   const nameSelect = $('#fullname-dropdown');
//   fetch(backendServerUrl + "/user?projection=CreateUserPage", requestOptions)
//     .then(result => result.json())
//     .then(response => {

//       if (response.status == 200 && response.data != '') {
//         // Iterate over the response data and add rows to the table
//         response.data.forEach(user => {
//           const row = `
//                 <option value='${user.id}'>${user.firstName} ${user.lastName}</option>
//             `;
//           nameSelect.append(row);
//         });
//       }
//       $('.fullname').loader('hide');
//     })
//     .catch(error => {
//       $('.fullname').loader('hide');
//       showToast("Names are not setted in dropdown", 'error');
//     });
// }

// function createExpense() {
//   $('.card-body').loader('show');
//   var methodType = 'POST';
//   var apiUrl = backendServerUrl + "/user/" + $("#fullname-dropdown").val() + "/expense";
//   var myHeaders = new Headers();
//   myHeaders.append("Content-Type", "application/json");
//   myHeaders.append("Authorization", window.sessionStorage.getItem("token"));
//   var dataForSave = {
//     "paymentMode": $("#payment-mode").val(),
//     "amount": $("#amount").val(),
//     "description": $("#description").val()
//   };

//   var editCondition=new URLSearchParams(window.location.search).get("type") == "edit";

//   if (editCondition) {
//     apiUrl += "/" + new URLSearchParams(window.location.search).get("expenseId");
//     methodType = 'PUT'
//   }

//   var raw = JSON.stringify(dataForSave);

//   var requestOptions = {
//     method: methodType,
//     headers: myHeaders,
//     body: raw,
//     redirect: 'follow'
//   };

//   fetch(apiUrl, requestOptions)
//     .then(response => response.json())
//     .then(result => {
//       if (result.status == 200 && result.data != '') {
//         showToast(result.message, 'success');
//       }
//       else {
//         showToast("Expense not added", 'error');
//         window.location.reload();
//       }
//       $('.card-body').loader('hide');
//     })
//     .catch(error => {
//       showToast("Expense not added", 'error');
//       $('.card-body').loader('hide');
//     });
//   $("#amount").val("")
//   $("#description").val("")

//   if (editCondition) {
//     window.location.href = "/expense-listing.html"
//   }

// }
