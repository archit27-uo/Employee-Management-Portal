document.addEventListener("DOMContentLoaded", function() {
   
    fetchEmployees('all');
});

var token = localStorage.getItem("authToken");
const headers = new Headers();
headers.set('Authorization', 'Basic ' + token);
headers.set('Content-Type', 'application/json');

function openAddUserModal() {
    document.getElementById('addUserModal').style.display = 'block';
}

function closeAddUserModal() {
    document.getElementById('addUserModal').style.display = 'none';
}

function logout() {
    // Clear the auth token from local storage
    localStorage.removeItem('authToken');

    window.location.href = '../login/login.html';
}

function showEmployees() {
    fetchEmployees('all');
    document.getElementById('project').style.display = 'none';
    document.getElementById('request-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('employee-list').style.display = 'block';
};

function openAssignProjectModal(employeeId) {
    document.getElementById('assignEmployeeId').value = employeeId;
    document.getElementById('assignProjectModal').style.display = 'block';
}

// Close the Assign Project Modal
function closeAssignProjectModal() {
    document.getElementById('assignProjectModal').style.display = 'none';
}
function fetchEmployees(filter) {
    
    fetch('http://localhost:8080/api/admin/employee', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = '';  // Clear the list
           
            data.forEach(employee => {
                if (filter === 'all' || (filter === 'assigned' && employee.project) || (filter === 'unassigned' && !employee.project)) {
                    const employeeCard = document.createElement('div');
                    employeeCard.className = 'employee-card';
                    employeeDetails = ``;
                    if(filter==='all'){
                        employeeDetails =  `
                    <div class="details">
                    <p id="empname">${employee.fullName}</p>
                    <div>
                    <i onclick="editEmployee(${employee.employeeId})" class="edit-icon" >📝</i>
                    <i onclick="deleteEmployee(${employee.employeeId})" class="delete-icon">🗑️</i>
                    </div>
                 </div>
                <div class="actions">
                <div class="lst">
                <div>
                <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                </div>  
               
                </div>
                <p>Skills : ${employee.skills.join(', ')}</p>
                </div>
                    `;
                    }else if(filter==='assigned'){
                        employeeDetails =  `
                    <div class="details">
                    <p id="empname">${employee.fullName}</p>
                    <div>
                    <i onclick="editEmployee(${employee.employeeId})" class="edit-icon" >📝</i>
                    <i onclick="deleteEmployee(${employee.employeeId})" class="delete-icon">🗑️</i>
                    </div>
                 </div>
                <div class="actions">
                <div class="lst">
                <div>
                <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                </div>  
                <button onclick="unassignProject(${employee.employeeId})">unassigned</button> 
                </div>
                <p>Skills : ${employee.skills.join(', ')}</p>
                </div>
                    `;
                    }else{
                    employeeDetails =  `
                    <div class="details">
                    <p id="empname">${employee.fullName}</p>
                    <div>
                    <i onclick="editEmployee(${employee.employeeId})" class="edit-icon" >📝</i>
                    <i onclick="deleteEmployee(${employee.employeeId})" class="delete-icon">🗑️</i>
                    </div>
                 </div>
                <div class="actions">
                <div class="lst">
                <div>
                <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                </div>  
                <button onclick="openAssignProjectModal(${employee.employeeId})">Assign</button> 
                </div>
                <p>Skills : ${employee.skills.join(', ')}</p>
                </div>
                    `;
                }
                    employeeCard.innerHTML = employeeDetails;
                    employeeList.appendChild(employeeCard);
                }
            });
        })
        .catch(error => {
            console.error('Error fetching employee data:', error);
        });
}

function openEditEmployeeModal(employee) {
    document.getElementById('editEmployeeId').value = employee.employeeId;
    document.getElementById('editFullName').value = employee.fullName;
    document.getElementById('editProjectId').value = employee.project ? employee.project.projectId : '';
    document.getElementById('editManagerId').value = employee.manager ? employee.manager.managerId : '';
    document.getElementById('editSkills').value = employee.skills.join(', ');

    document.getElementById('editEmployeeModal').style.display = 'block';
}

function closeEditEmployeeModal() {
    document.getElementById('editEmployeeModal').style.display = 'none';
}

function unassignProject(employeeId){

    const url = `http://localhost:8080/api/admin/employee/${employeeId}/unassignProject`;

    fetch(url, {
        method: 'PUT',
        headers: headers
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Failed to unassign project');
        }
    })
    .then(message => {
        alert(message);
        closeAssignProjectModal();
      
    })
    .catch(error => {
        console.error('Error auassigning project:', error);
        alert('Failed to unnassign project: ' + error.message);
    });
}
function showProjects() {
  
    fetchProjects();
    
    document.getElementById('project').style.display = 'block';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('request-list').style.display = 'none';

}

function fetchProjects() {
   
    fetch('http://localhost:8080/api/admin/projects', {
        method: 'GET',
        headers: headers
    })
    .then(response => response.json())
    .then(data => {
        const projectList = document.getElementById('project-list');
        projectList.innerHTML = '';
        data.forEach(project => {
            const projectCard = document.createElement('div');
            projectCard.classList.add('project-card');
            
            let managerInfo = project.manager ? 
                `<p>Manager: ${project.manager.user.userName}</p>` :
                `<p>No manager assigned</p>`;
                
            projectCard.innerHTML = `
                <div class="project-info">
                    <h3>${project.projectName}</h3>
                    ${managerInfo}
                </div>
                <button class="expand-btn" onclick="fetchEmployees(${project.projectId})">Expand</button>
            `;
            projectList.appendChild(projectCard);
        });
    })
    .catch(error => console.error('Error:', error));
}

function showProjects() {
    // Fetch and display projects here
    fetchProjects();
    // Show project list and hide other lists
    document.getElementById('project').style.display = 'block';
    document.getElementById('request-list').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
}


function showManagers(){
    document.getElementById('project').style.display = 'none';
    document.getElementById('request-list').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'block';
    fetchAllManagers();
}

function fetchAllManagers() {
   
    fetch('http://localhost:8080/api/admin/manager', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const managerList = document.getElementById('manager-list');
            managerList.innerHTML = '';  // Clear the list
            data.forEach(manager => {
                const managerCard = document.createElement('div');
                managerCard.className = 'manager-card';

                const managerDetails = `
                <div class="details">
                <h3>${manager.user.userName}</h3>
                <p>Email : ${manager.user.userEmail}</p>
            </div>
                `;

                managerCard.innerHTML = managerDetails;
                managerList.appendChild(managerCard);
            });
        })
        .catch(error => {
            console.error('Error fetching manager list:', error);
        });
}

function showRequests() {
   
    // Fetch and display requests here
    
    // Show request list and hide other lists
    document.getElementById('project').style.display = 'none';
    document.getElementById('request-list').style.display = 'block';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    fetchRequests();
}

function fetchRequests() {
    
    fetch('http://localhost:8080/api/admin/request', {
        method: 'GET',
        headers:headers
    })
    .then(response => response.json())
        .then(data => {
            const requestList = document.getElementById('request-list');
            requestList.innerHTML = '';  // Clear the list

            data.forEach(request => {
                const requestCard = document.createElement('div');
                requestCard.className = 'request-card';

                const requesterName = request.requester.user.userName;
                const requestDetails = `
                    <div class="details">
                        <p>Request ID: ${request.requestId}</p>
                        <p>Requester: ${requesterName}</p>
                        <p>Request Type: ${request.requestType}</p>
                        
                        <div class="lst">
                        <p>Project ID: ${request.projectId}</p>
                        <div> <i class="fas fa-times" style="color: red; cursor: pointer;" onclick="handleRequest('reject', ${request.requestId})"></i>
                        &nbsp  &nbsp
                        <i class="fas fa-check" style="color: green; cursor: pointer;" onclick="handleRequest('approve', ${request.requestId})"></i></div>
                       
                    </div>
                        <p>Employee IDs: ${request.employeeIds.join(', ')}</p>
                        <p>Request Details: ${request.requestDetails}</p>
                        <p>Status: ${request.status}</p>
                    </div>
                `;

                requestCard.innerHTML = requestDetails;
                requestList.appendChild(requestCard);
            });
        })
        .catch(error => {
            console.error('Error fetching request list:', error);
        });
}




function addEmployee() {
    // Function to trigger adding an employee
    alert('Add Employee button clicked');
}

function assignProject(event) {
    event.preventDefault();
    
    const employeeId = document.getElementById('assignEmployeeId').value;
    const projectId = document.getElementById('assignProjectId').value;
    const url = `http://localhost:8080/api/admin/employee/${employeeId}/assignProject/${projectId}`;

    fetch(url, {
        method: 'PUT',
        headers: headers
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Failed to assign project');
        }
    })
    .then(message => {
        alert(message);
        closeAssignProjectModal();
      
    })
    .catch(error => {
        console.error('Error assigning project:', error);
        alert('Failed to assign project: ' + error.message);
    });
    alert(`Assign project to employee with ID: ${employeeId}`);
}

function editEmployee(employeeId) {
    fetch(`http://localhost:8080/api/admin/employee/${employeeId}`, { headers: headers })
    .then(response => response.json())
    .then(employee => openEditEmployeeModal(employee))
    .catch(error => console.error('Error fetching employee details:', error));
}

function submitEditEmployeeForm(event) {
    event.preventDefault();

    const employeeId = document.getElementById('editEmployeeId').value;
    const fullName = document.getElementById('editFullName').value;
    const projectId = document.getElementById('editProjectId').value;
    const managerId = document.getElementById('editManagerId').value;
    const skills = document.getElementById('editSkills').value.split(',').map(skill => skill.trim());

    const employeeData = {
        employeeId: employeeId,
        fullName: fullName,
        project: projectId ? { projectId: projectId } : null,
        manager: managerId ? { managerId: managerId } : null,
        skills: skills
    };

    fetch(`http://localhost:8080/api/admin/employee/${employeeId}`, {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(employeeData)
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Failed to update employee');
        }
    })
    .then(message => {
        alert(message);
        closeEditEmployeeModal();
        // Optionally refresh the employee list or update the UI
    })
    .catch(error => {
        console.error('Error updating employee:', error);
        alert('Failed to update employee: ' + error.message);
    });
}

function deleteEmployee(employeeId) {
    // Function to delete employee
   
    fetch(`http://localhost:8080/api/admin/employee/${employeeId}`, {
        method: 'DELETE',
        headers: headers
    })
    .then(response => response.text())
    .then(message => {
        if (response.body === "Successfully Deleted") {
            showMessage('Employee successfully deleted.', 'success');
            // Optionally, refresh the employee list or remove the deleted employee's card from the DOM
            document.getElementById(`employee-card-${employeeId}`).remove();
        } else {
            print(message);
            print(response.body)
            showMessage('Error deleting employee.', 'error');
        }
    })
    .catch(error => {
        console.error('Error deleting employee:', error);
        showMessage('Error deleting employee.', 'error');
    });
    alert(`Delete employee with ID: ${employeeId}`);
}

function filterEmployees(filter) {
    fetchEmployees(filter);
}
function openAddEmployeeModal() {
    document.getElementById('addEmployeeModal').style.display = 'block';
}

function closeAddEmployeeModal() {
    document.getElementById('addEmployeeModal').style.display = 'none';
}

function submitUserForm(event) {
    event.preventDefault();
    const username = 'arc@mail.com';  // Replace with your username
    const password = 'arc123';  // Replace with your password
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    headers.set('Content-Type', 'application/json');

    const user = {
        userName: document.getElementById('userName').value,
        userEmail: document.getElementById('userEmail').value,
        userPassword: document.getElementById('userPassword').value,
        userRole: document.getElementById('userRole').value
    };

    fetch('http://localhost:8080/api/admin/user', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(user)
    })
    .then(response => response.json())
    .then(data => {
        if (user.userRole === 'EMPLOYEE') {
            document.getElementById('employeeUserId').value = data.userId;
            closeAddUserModal();
            openAddEmployeeModal();
        } else {
            const manager ={
                userId:data.userId
            }
            fetch('http://localhost:8080/api/admin/manager',{
                method:'POST',
                headers:headers,
                body:JSON.stringify(manager)
            })
            .then(response => response.json())
            .then(data =>{
                alert('Manager added successfully');
            })
            .catch(error=>{
                alert('Error in adding manager: '+error);
            })
           
            closeAddUserModal();
        }
    })
    .catch(error => {
        alert('Error adding user: ' + error);
    });
}

function submitEmployeeForm(event) {
    event.preventDefault();
    const username = 'arc@mail.com';  // Replace with your username
    const password = 'arc123';  // Replace with your password
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    headers.set('Content-Type', 'application/json');

    const employee = {
        userId: document.getElementById('employeeUserId').value,
        fullName: document.getElementById('fullName').value,
        projectId: document.getElementById('projectId').value,
        managerId: document.getElementById('managerId').value,
        skills: document.getElementById('skills').value.split(',')
    };

    fetch('http://localhost:8080/api/admin/employee', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(employee)
    })
    .then(response => response.json())
    .then(data => {
        alert('Employee added successfully');
        closeAddEmployeeModal();
    })
    .catch(error => {
        alert('Error adding employee: ' + error);
    });
}

function handleRequest(chooseAction, requestId) {
    const url = chooseAction === 'reject' ? 
        `http://localhost:8080/api/admin/request/${requestId}/reject` : 
        `http://localhost:8080/api/admin/request/${requestId}/approve`;

    if(chooseAction=='reject'){
        fetch(url,{
            method: 'PUT',
            headers: headers,
        }).catch(error=>{
            alert("Error in request cancel "+error)
        })
    }else{
        fetch(url,{
            method: 'PUT',
            headers: headers,
        }).catch(error=>{
            alert("Error in request cancel "+error)
        })
    }
    // Implement logic to handle request action (accept/reject)
    console.log(`Request ID: ${requestId}, Action: ${chooseAction}`);
    // You can make API call to update request status here
}
// function submitEmployeeForm(event) {
//     event.preventDefault();
//     const form = document.getElementById('addEmployeeForm');
//     const formData = new FormData(form);

//     const employee = {
       
//         fullName: formData.get('fullName'),
//         projectId: formData.get('projectId') ? parseInt(formData.get('projectId')) : null,
//         managerId: formData.get('managerId') ? parseInt(formData.get('managerId')) : null,
//         skills: formData.get('skills').split(',').map(skill => skill.trim())
//     };

//     const username = 'arc@mail.com';  // Replace with your username
//     const password = 'arc123';  // Replace with your password
//     const headers = new Headers();
//     headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
//     headers.set('Content-Type', 'application/json');

//     fetch('http://localhost:8080/api/admin/employee', {
//         method: 'POST',
//         headers: headers,
//         body: JSON.stringify(employee)
//     })
//     .then(response => {
//         if (response.ok) {
//             return response.json();
//         } else {
//             throw new Error('Failed to add employee');
//         }
//     })
//     .then(data => {
//         showMessage('Employee added successfully', 'success');
//         form.reset();
//         closeAddEmployeeModal();
//         fetchEmployees('all');
//     })
//     .catch(error => {
//         showMessage(error.message, 'error');
//     });
// }
function openAddProjectModal() {
    document.getElementById('addProjectModal').style.display = 'block';
}

// Function to close the Add Project modal
function closeAddProjectModal() {
    document.getElementById('addProjectModal').style.display = 'none';
}

// Function to handle the Add Project form submission
function submitAddProjectForm(event) {
    event.preventDefault();

    const projectName = document.getElementById('projectName').value;
    const managerId = document.getElementById('projectManagerId').value;

    const newProject = {
        projectName: projectName,
        managerId: managerId
    };

    fetch('http://localhost:8080/api/admin/project', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(newProject)
    })
    .then(response => {
        if (response.ok) {
            showMessage('Project added successfully', 'success');
            closeAddProjectModal();
        } else {
            throw new Error('Failed to add project');
        }
    })
    .catch(error => {
        showMessage('Error adding project: ' + error.message, 'error');
    });
}

// Function to display messages
function showMessage(message, type) {
    const messageElement = document.createElement('div');
    messageElement.textContent = message;
    messageElement.className = type === 'success' ? 'success-message' : 'error-message';
    document.body.appendChild(messageElement);
    setTimeout(() => {
        document.body.removeChild(messageElement);
    }, 3000);
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