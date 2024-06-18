document.addEventListener("DOMContentLoaded", function () {
    showHomePage();
});
var token = localStorage.getItem("authToken");
const headers = new Headers();
headers.set('Authorization', 'Basic ' + token);
headers.set('Content-Type', 'application/json');

async function fetchData(url, options={}) {
    try {
        const response = await fetch(url, { headers, ...options });
        
        if (response.status!=200 && response.status!=201){ 
            const errorData = await response.json();
            const errorMessage = errorData.message || 'Failed to update employee';
            throw new Error(errorMessage);

    }       
    const data = await response.json();
    return {status : response.status, data };
        
    } catch (error) {
        showMessage(`Failed due to: ${error.message}`, 'error');
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const sidebarLinks = document.querySelectorAll(".sidebar ul li");
    sidebarLinks.forEach(link => {
        link.addEventListener("click", function() {
            sidebarLinks.forEach(link => link.classList.remove("selected"));
            this.classList.add("selected");
        });
    });
});

function showHomePage() {
    document.getElementById('home-page').style.display = 'block';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';

    fetchEmployeeInfo();
}

function showAllEmployees() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'flex';
    document.getElementById('profile-page').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';

    fetchAllEmployees();
}

function showProfile() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'block';
    document.getElementById('manager-list').style.display = 'none';

    fetchEmployeeProfile();
}

function fetchEmployeeInfo() {


    fetch('http://localhost:8080/api/employee/info', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeDetails = `
            <div class="card">
            <h3>Welcome ! ${data.fullName}</h3>
            <p><b>Project: </b> ${data.project ? data.project.projectName : 'None'}</p>
            <p><b>Manager: </b> ${data.manager ? data.manager.user.userName : 'None'}</p>
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
            <p><b>ID :</b> ${data.employeeId} </p>
            <p><b>Email :</b> ${data.user.userEmail}</p>
            <p><b>Project :</b> ${data.project ? data.project.projectName : 'None'}</p>
                <p><b>Manager :</b> ${data.manager ? data.manager.user.userName : 'None'}</p>
            <p><b>Skills :</b> ${data.skills.join(', ')}</p>
            <div class="card-footer">
              <button onclick="openModal('${data.user.userEmail}')">Change Password</button>
              </div>
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
                <p><b>Project:</b> ${employee.project ? employee.project.projectName : 'None'}</p>
                <p><b>Manager: </b>${employee.manager ? employee.manager.user.userName : 'None'}</p>
                <p><b>Skills: </b>${employee.skills.join(', ')}</p>
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


function showAllManagers() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';
    document.getElementById('manager-list').style.display = 'block';
    fetchAllManagers();
}

async function fetchAllManagers() {

    const {status, data} = await fetchData('http://localhost:8080/api/employee/manager',{method:'GET'});

        if(status==200){
            const managerList = document.getElementById('manager-list');
            managerList.innerHTML = '<div class="gap"><h3>Manager List</h3></div>'; 
            data.forEach(manager => {
                const managerCard = document.createElement('div');
                managerCard.className = 'manager-card';

                const managerDetails = `
            <div class="card">
                <div class="details">
                <h3>${manager.user.userName}</h3>
                <p><b>Email</b> : ${manager.user.userEmail}</p>
            </div>
            </div>
                `;

                managerCard.innerHTML = managerDetails;
                managerList.appendChild(managerCard);
            });
        }
}



function openModal(userEmail) {
    document.getElementById('changePasswordModal').style.display = 'block';
    document.getElementById('userEmail').value = userEmail; // Populate with the user's email
}

function closeModal() {
    document.getElementById('changePasswordModal').style.display = 'none';
    document.getElementById('userPassword').value = '';
    document.getElementById('alertMessage').style.display = 'none';
}

async function changePassword() {
    const email = document.getElementById('userEmail').value;
    const password = document.getElementById('userPassword').value;
    const change={
        userName:email,
        userPassword:password
    }
    const response = await fetch('http://localhost:8080/api/employee/changePassword', {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(change)
    });

    const result = await response.json();
    const alertMessage = document.getElementById('alertMessage');
    
    if (response.ok) {
        
        alertMessage.textContent = result.message;
        alertMessage.className = 'alert success';
        alertMessage.style.display = 'block';
        closeModal();
        logout();
    } else {
        alertMessage.textContent = result.message;
        alertMessage.className = 'alert error';
    }

   
}

// Close the modal when the user clicks outside of it
window.onclick = function(event) {
    const modal = document.getElementById('changePasswordModal');
    if (event.target == modal) {
        closeModal();
    }
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
