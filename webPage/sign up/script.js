document.getElementById("registrationForm").addEventListener("submit", function(event) {
    event.preventDefault();
    var name = document.getElementById("name").value;
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;
    var role = document.getElementById("role").value;

    var userData = {
        employeeName: encodeURIComponent(name),
        employeeEmail: email,
        employeePassword: password,
        employeeRole: role
    };

    fetch('http://localhost:8080/api/employee/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("message").innerHTML = "User registered successfully!";
    })
    .catch((error) => {
        console.error('Error:', error);
        document.getElementById("message").innerHTML = "An error occurred while registering user.";
    });
});
