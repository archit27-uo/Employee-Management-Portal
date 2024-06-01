document.getElementById("loginForm").addEventListener("submit", function(event) {
    event.preventDefault();
    var email = document.getElementById("email").value
    var password = document.getElementById("password").value

    var login = {
        employeeEmail: email,
        employeePassword: password,
    }
    fetch("http://localhost:8080/api/employee/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
  
      body: JSON.stringify(login)
    })
    .then(response => response.json())
    .then(result => {
      document.getElementById("message").textContent = result.message;
      if (result.message === "Login Success") {
       
        if(result.role==="employee"){
            window.location.href = "../employee/employee.html";
        }
        else if(result.role==="Manager"){
          window.location.href = "../manager/manager.html";
        }
      }
    })
    .catch(error => console.error("Error:", error));
  });
  