document.getElementById("loginForm").addEventListener("submit", function(event) {
    event.preventDefault();
    var email = document.getElementById("email").value
    var password = document.getElementById("password").value

    var login = {
        userName: email,
        userPassword: password,
    }
    fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "ngrok-skip-browser-warning": "69420"
      },
  
      body: JSON.stringify(login)
    })
    .then(response => response.json())
    .then(result => {
      console.log(result);
      document.getElementById("message").textContent = result.message;
      if (result.message === "Login Success") {
        var token = btoa(email + ":" + password);
        localStorage.setItem("authToken", token);
        if(result.userRole==="EMPLOYEE"){
            window.location.href = "../employee/employee.html";
        }
        else if(result.userRole==="MANAGER"){
          window.location.href = "../manager/manager.html";
        }else if(result.userRole=="ADMIN"){
          window.location.href="../admin/admin.html"
        }
      }
    })
    .catch(error => console.error("Error:", error));
  });
  