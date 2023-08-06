function getUserById(id) {
  var settings = {
    "url": backendServerUrl + "/user/"+id+"?projection=UserProfileDTO",
    "method": "GET",
    "timeout": 0,
    "headers": {
      "Authorization": getJwtTokenFromLocalStrorage()
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
      "Authorization": getJwtTokenFromLocalStrorage()
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
    "url": backendServerUrl + "/user?projection=NormalWithProfile",
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
                      <h5 class="card-title">${user.fullName}</h5>
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
      $('body').loader('hide');
      errorMessageProcess(xhr);
      $('#dynamic-user-listing').html("<div class='alert alert-danger' role='alert'>"+errorResponse+"</div>");
    });
}