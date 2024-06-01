document.addEventListener("DOMContentLoaded", function() {
    fetchEmployees('all');
});

function fetchEmployees(filter) {
    const username = 'arc@mail.com';  // Replace with your username
    const password = 'arc123';  // Replace with your password
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

    fetch('http://localhost:8080/api/admin/employee', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = '';  // Clear the list
            data.forEach(employee => {
                if (filter === 'all' || (filter === 'assigned' && employee.project) || (filter === 'unassigned' && !employee.project)) {
                    const employeeCard = document.createElement('div');
                    employeeCard.className = 'employee-card';

                    const employeeDetails = `
                        <div class="details">
                            <p>Name: ${employee.fullName}</p>
                            <p>Project: ${employee.project ? employee.project.name : 'None'}</p>
                            <p>Manager: ${employee.manager ? employee.manager.name : 'None'}</p>
                            <p>Skills: ${employee.skills.join(', ')}</p>
                        </div>
                        <div class="actions">
                            <button onclick="assignProject(${employee.employeeId})">Assign</button>
                            <i onclick="editEmployee(${employee.employeeId})" class="edit-icon">✏️</i>
                            <i onclick="deleteEmployee(${employee.employeeId})" class="delete-icon">🗑️</i>
                        </div>
                    `;

                    employeeCard.innerHTML = employeeDetails;
                    employeeList.appendChild(employeeCard);
                }
            });
        })
        .catch(error => {
            console.error('Error fetching employee data:', error);
        });
}

function showProjects() {
  
    fetchProjects();
    
    document.getElementById('project-list').style.display = 'block';
    document.getElementById('employee-list').style.display = 'none';
}

function fetchProjects() {
    const username = 'arc@mail.com';  // Replace with your username
    const password = 'arc123';  // Replace with your password
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
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
    document.getElementById('project-list').style.display = 'block';
    document.getElementById('request-list').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
}



function showRequests() {
    // Fetch and display requests here
    fetchRequests();
    // Show request list and hide other lists
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('request-list').style.display = 'block';
    document.getElementById('employee-list').style.display = 'none';
}

function fetchRequests() {
    const username = 'arc@mail.com';  // Replace with your username
    const password = 'arc123';  // Replace with your password
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    fetch('http://localhost:8080/api/admin/requests', {
        method: 'GET',
        headers:headers
    })
    .then(response => response.json())
    .then(data => {
        const requestList = document.getElementById('request-list');
        requestList.innerHTML = '';
        data.forEach(request => {
            const requestTile = document.createElement('div');
            requestTile.classList.add('request-tile');
            requestTile.innerHTML = `
                <div class="request-info">
                    <h3>${request.requestType}</h3>
                    <p>${request.requestDetails}</p>
                </div>
                <div class="action-buttons">
                    <button class="accept-btn" onclick="handleRequestAction(${request.requestId}, 'APPROVED')">Accept</button>
                    <button class="reject-btn" onclick="handleRequestAction(${request.requestId}, 'REJECT')">Reject</button>
                </div>
            `;
            requestList.appendChild(requestTile);
        });
    })
    .catch(error => console.error('Error:', error));
}

function handleRequestAction(requestId, action) {
    // Implement logic to handle request action (accept/reject)
    console.log(`Request ID: ${requestId}, Action: ${action}`);
    // You can make API call to update request status here
}


function addEmployee() {
    // Function to trigger adding an employee
    alert('Add Employee button clicked');
}

function assignProject(employeeId) {
    // Function to assign project to employee
    alert(`Assign project to employee with ID: ${employeeId}`);
}

function editEmployee(employeeId) {
    // Function to edit employee details
    alert(`Edit employee with ID: ${employeeId}`);
}

function deleteEmployee(employeeId) {
    // Function to delete employee
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

function submitEmployeeForm(event) {
    event.preventDefault();
    const form = document.getElementById('addEmployeeForm');
    const formData = new FormData(form);

    const employee = {
       
        fullName: formData.get('fullName'),
        projectId: formData.get('projectId') ? parseInt(formData.get('projectId')) : null,
        managerId: formData.get('managerId') ? parseInt(formData.get('managerId')) : null,
        skills: formData.get('skills').split(',').map(skill => skill.trim())
    };

    const username = 'arc@mail.com';  // Replace with your username
    const password = 'arc123';  // Replace with your password
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    headers.set('Content-Type', 'application/json');

    fetch('http://localhost:8080/api/admin/employee', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(employee)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to add employee');
        }
    })
    .then(data => {
        showMessage('Employee added successfully', 'success');
        form.reset();
        closeAddEmployeeModal();
        fetchEmployees('all');
    })
    .catch(error => {
        showMessage(error.message, 'error');
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