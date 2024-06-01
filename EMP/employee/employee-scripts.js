document.addEventListener("DOMContentLoaded", function() {
    showHomePage();
});

function showHomePage() {
    document.getElementById('home-page').style.display = 'block';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';

    fetchEmployeeInfo();
}

function showAllEmployees() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'flex';
    document.getElementById('profile-page').style.display = 'none';

    fetchAllEmployees();
}

function showProfile() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'block';

    fetchEmployeeProfile();
}

function fetchEmployeeInfo() {
    const username = 'harsh@mail.com';
    const password = 'harsh123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

    fetch('http://localhost:8080/api/employee/info', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeDetails = `
                <p>Name: ${data.fullName}</p>
                <p>Project: ${data.project ? data.project.name : 'None'}</p>
                <p>Manager: ${data.manager ? data.manager.name : 'None'}</p>
            `;
            document.getElementById('employee-details').innerHTML = employeeDetails;
        })
        .catch(error => {
            console.error('Error fetching employee info:', error);
        });
}

function fetchEmployeeProfile() {
    const username = 'harsh@mail.com';
    const password = 'harsh123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

    fetch('http://localhost:8080/api/employee/info', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const profileDetails = `
                <p>User ID: ${data.user.userId}</p>
                <p>Name: ${data.fullName}</p>
                <p>Email: ${data.user.userEmail}</p>
                <p>Skills: ${data.skills.join(', ')}</p>
            `;
            document.getElementById('profile-details').innerHTML = profileDetails;
        })
        .catch(error => {
            console.error('Error fetching profile info:', error);
        });
}

function fetchAllEmployees() {
    const username = 'harsh@mail.com';
    const password = 'harsh123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

    fetch('http://localhost:8080/api/employee/all', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = '';  // Clear the list
            data.forEach(employee => {
                const employeeCard = document.createElement('div');
                employeeCard.className = 'employee-card';

                const employeeDetails = `
                    <div class="details">
                        <p>Name: ${employee.fullName}</p>
                        <p>Project: ${employee.project ? employee.project.name : 'None'}</p>
                        <p>Manager: ${employee.manager ? employee.manager.name : 'None'}</p>
                        <p>Skills: ${employee.skills.join(', ')}</p>
                    </div>
                `;

                employeeCard.innerHTML = employeeDetails;
                employeeList.appendChild(employeeCard);
            });
        })
        .catch(error => {
            console.error('Error fetching employee list:', error);
        });
}

function showMessage(message, type) {
    const messageDiv = document.getElementById('message');
    if (messageDiv) {
        messageDiv.textContent = message;
        messageDiv.className = type === 'success' ? 'message success' : 'message error';
        setTimeout(() => {
            messageDiv.textContent = '';
            messageDiv.className = 'message';
        }, 3000);
    } else {
        console.error('Message element not found');
    }
}
