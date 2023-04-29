
function operations(type,id,element){ 
  if(type == "edit"){

    }
    if(type == "delete"){
        var myHeaders = new Headers();
        myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

        var requestOptions = {
          method: 'DELETE',
          headers: myHeaders,
        };

        fetch(backendServerUrl+"/user/expense/"+id, requestOptions)
          .then(response => response.json())
          .then(result => {
              if(result.status == 200 && result.data != ''){
                showToast(result.message, 'success');
                $('#delete'+id+'row').parents().eq(4).remove();
                if($('tbody tr').length == 0){
                  $('.table').html("<div class='alert alert-danger' role='alert'>Expenses are not available in system.</div>");
                }
              }
              else{
                showToast("Expense not deleted", 'error');
              }
          })
          .catch(error => showToast("Expense not deleted", 'error'));

    }

    $(".dropdown-menu.show").removeClass("show");
}

function getUsers() {
$('.card-body').loader('show');
var settings = {
  "url": backendServerUrl+"/user/expense",
  "method": "GET",
  "timeout": 0,
  "headers": {
    "Authorization": window.sessionStorage.getItem("token")
  },
};

$.ajax(settings)
.done(function(response) {
  if (response.status == 200 && response.data != '') {
    
    if(response.data.length != 0){
      var rows="";
      $.each(response.data, function(i, expense) {
        var date=new Date(expense.createdAt).toLocaleDateString('en-IN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
        rows += `
          <tr>
            <td class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center">${i+1}</td>
            <td>${expense.user.firstName} ${expense.user.lastName}</td>
            <td>${expense.paymentMode}</td>
            <td>${expense.amount}</td>
            <td>${expense.description}</td>
            <td>${date}</td>
            <td class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center">
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
.fail(function(jqXHR, textStatus, errorThrown) {
  showToast(jqXHR.responseJSON.message, 'error');
  $('.table').html("<div class='alert alert-danger' role='alert'>Expenses are not available in system.</div>");
  $('.card-body').loader('hide');
});

//     var myHeaders = new Headers();
//     myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

//     var requestOptions = {
//       method: 'GET',
//       headers: myHeaders,
//       redirect: 'follow'
//     };
//     const table = $('.table tbody');
//     fetch(backendServerUrl+"/user/expense", requestOptions)
//       .then(result => result.json())
//       .then(response => {

//         if(response.status == 200 && response.data != ''){
// console.log(response.data);
//           // Iterate over the response data and add rows to the table
//           response.data.forEach(function (expense, i) {
//             const row = `
//               <tr>
//                 <td>${i+1}</td>
//                 <td>${expense.user.firstName} ${expense.user.lastName}</td>
//                 <td>${expense.paymentMode}</td>
//                 <td>${expense.amount}</td>
//                 <td>${expense.description}</td>
//                 <td>${expense.createdAt}</td>
//                 <td>
//                 <div class="dropdown">
//                 <img src="asstes/images/three-dots-vertical.svg" onclick="showOptions(this)" class="dropdown-toggle">
//                 <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
//                   <li><a class="dropdown-item edit" onclick="operations('view',${expense.id},this)" >View</a></li>
//                   <li><a class="dropdown-item update" onclick="operations('edit',${expense.id},this)">Update</a></li>
//                   <li><a class="dropdown-item delete" onclick="operations('delete',${expense.id},this)">Delete</a></li>
//                 </ul>
//               </div>
//                 </td>
//               </tr>
//             `;
//             table.append(row);
//           });
//         }
//       })
//       .catch(error => {
//         showToast("Error getting expense data.", 'error');
//         console.log('error', error)
//       });
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
    fetch(backendServerUrl+"/user?projection=CreateUserPage", requestOptions)
      .then(result => result.json())
      .then(response => {

        if(response.status == 200 && response.data != ''){
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

function createExpense(){
  $('.card-body').loader('show');
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

    var raw = JSON.stringify({
      "paymentMode": $("#payment-mode").val(),
      "amount": $("#amount").val(),
      "description": $("#description").val()
    });

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

    fetch(backendServerUrl+"/user/"+$("#fullname-dropdown").val()+"/expense", requestOptions)
      .then(response => response.json())
      .then(result => {
          if(result.status == 200 && result.data != ''){
            showToast(result.message, 'success');
          }
          else{
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

function showOptions(element,operation){
  $(element).siblings(".dropdown-menu").toggleClass(operation);
}
