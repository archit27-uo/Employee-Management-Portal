document.addEventListener("DOMContentLoaded", function () {
    showHomePage();
});
var token = localStorage.getItem("authToken");
const headers = new Headers();
headers.set('Authorization', 'Basic ' + token);
headers.set('Content-Type', 'application/json');
headers.set("ngrok-skip-browser-warning", "69420")

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


    fetch('http://localhost:8080/api/employee/info', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeDetails = `
            <div class="card">
            <h3>Welcome ! ${data.fullName}</h3>
            <p>Project : ${data.project ? data.project.projectName : 'None'}</p>
            <p>Manager : ${data.manager ? data.manager.user.userName : 'None'}</p>
            </div>`;
            document.getElementById('employee-details').innerHTML = employeeDetails;
        })
        .catch(error => {
            console.error('Error fetching employee info:', error);
        });
}


function fetchEmployeeProfile() {
    

    fetch('http://localhost:8080/api/employee/info', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const profileDetails = `
            <div class="card">
            <h3>${data.fullName} <i onclick="editProfile(${data.user.userId})" class="fas fa-edit" style="cursor: pointer;"></i></h3>
            <p>ID : ${data.employeeId} </p>
            <p>Email : ${data.user.userEmail}</p>
            <p>Project : ${data.project.projectName}</p>
            <p>Manager: ${data.manager.user.userName}</p>
            <p>Skills : ${data.skills.join(', ')}</p>
            </div>`;
            document.getElementById('profile-details').innerHTML = profileDetails;
        })
        .catch(error => {
            console.error('Error fetching profile info:', error);
        });
}



function editProfile(userId) {
   
    
    fetch(`http://localhost:8080/api/employee/info`, {
        method: 'GET',
        headers: headers,
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error(response.json);
            }
        })
        .then(data => {
           
            document.getElementById('editProfileModal').style.display = 'block';
            document.getElementById('editSkills').value = data.skills.join(', ');
        })
        .catch(error => {
            showMessage('Error fetching profile details for editing: ' + error.message, 'error');
        });
}

// Function to submit the edited profile details
function submitEditProfileForm(event) {
    event.preventDefault();
    const skills = document.getElementById('editSkills').value.split(',').map(skill => skill.trim());

   
    fetch(`http://localhost:8080/api/employee/skills`, {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(skills)
    }).then(response => {
            if (response.status==200) {
            
                showMessage('Profile updated successfully', 'success');
                document.getElementById('editProfileModal').style.display = 'none';
                showProfile();
            } else {
                throw new Error('Failed to update profile');
            }
        })
        .catch(error => {
            showMessage('Error updating profile: ' + error.message, 'error');
        });
}


function fetchAllEmployees() {


    fetch('http://localhost:8080/api/employee/all', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = ''; 
            data.forEach(employee => {
                const employeeCard = document.createElement('div');
                employeeCard.className = 'employee-card';

                const employeeDetails = `
                <div class="details card">
                <h3>${employee.fullName}</h3>
                <p>Project: ${employee.project ? employee.project.projectName : 'None'}</p>
                <p>Manager: ${employee.manager ? employee.manager.user.userName : 'None'}</p>
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

function logout() {
    localStorage.removeItem('authToken');
    window.location.href = '../login/login.html';
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
