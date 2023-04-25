
window.onload = function() {
  if(window.localStorage.getItem("token") == null){
      showData("none");
      var username = prompt("Please enter your email : ");
      if(username != null && username != ""){
        var password = prompt("Please enter your password : ");
        if(password != null && password != ""){
            var authStatus=authenticate(username,password);
            console.log(authStatus+""+window.localStorage.getItem("token"));
            if(authStatus == true && window.localStorage.getItem("token") != null){
                showData("block");
            }
            else{
              console.log("Error to generate token");
            }
        }
      }
  }else{
    showData("block");
  }
};
function showData(displayType){
var reqPath=window.location.href.split("/")[3];
    if(reqPath == ""){
        const myDiv = document.getElementById("user-table");
        myDiv.style.display = displayType;
        getUsers();
    }
    else if(reqPath =="delete-user"){
        const myDiv = document.getElementById("delete-form");
        myDiv.style.display = displayType;
    }
    else if(reqPath =="create-user"){
        const myDiv = document.getElementById("formId");
        myDiv.style.display = displayType;
    }
}

const apiUrl = 'http://localhost:8080';



function authenticate(email,password) {
  if(window.localStorage.getItem("token") != null){
    return true;
  }
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  var raw = JSON.stringify({
    "email": email,
    "password": password
  });
  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };
  
  fetch(apiUrl+"/authenticate", requestOptions)
    .then(response => response.json())
    .then(response => {
      window.localStorage.setItem("token",'Bearer '+response.jwt);
      alert("Token generated");
    }) 
    .catch(error => {
      alert(error);
    });
    return true;
}

function logout(){
  var localToken=window.localStorage.removeItem("token");
  alert("Logout Successfully...");
}

function getUsers() {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", window.localStorage.getItem("token"));
    
    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };
    const table = $('.table');
    fetch(apiUrl+"/user?projection=UserProjectionDTO", requestOptions)
      .then(result => result.json())
      .then(response => {
        if(response.statusCode == 200 && response.data != ''){
          // Iterate over the response data and add rows to the table
          response.data.forEach(user => {
            const row = `
              <tr>
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.enabled}</td>
                <td>${user.locked}</td>
              </tr>
            `;
            table.append(row);
          });
        }else{
          $('#msg').text(response.message);
          table.hide();
        }
      })
      .catch(error => {
        alert(error);
        console.log('error', error)
      });
}

function createUser() {
    var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", window.localStorage.getItem("token"));

    var raw = JSON.stringify({
    "email": document.getElementById("email").value,
    "fullName": document.getElementById("fullName").value,
    "password": document.getElementById("password").value,
    "enabled": true,
    "locked": false
    });
    console.log(raw)
    var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
    };

    fetch(apiUrl+"/user", requestOptions)
    .then(response => response.text())
    .then(result => console.log(result))
    .catch(error => console.log('error', error));

}

function takeConformation(){
  var result = confirm("Are you sure for delete user ?");
  if (!result) {
     event.preventDefault();
   }
   else{
     deleteUser();
   }
}

function deleteUser() {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", window.localStorage.getItem("token"));
    var requestOptions = {
      method: 'DELETE',
      headers: myHeaders,
      redirect: 'follow'
    };
    var userId=document.getElementById("userId").value;
    fetch(apiUrl+"/user/"+userId, requestOptions)
      .then(response => response.json())
      .then(result => {
        alert(result.message);
        window.location.reload();
      })
      .catch(error => alert("Usern not deleted"));

}
