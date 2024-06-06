document.addEventListener("DOMContentLoaded", function() {
    showHomePage();
});

var managerId;
var token = localStorage.getItem("authToken");
const headers = new Headers();
headers.set('Authorization', 'Basic ' + token);
headers.set('Content-Type', 'application/json');
function getManagerId(){
    return this.managerId;
}

function showHomePage() {
    document.getElementById('home-page').style.display = 'block';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';
    document.getElementById('request').style.display = 'none';
    fetchManagerInfo();
}

function showAllEmployees() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee').style.display = 'flex';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';
    document.getElementById('request').style.display = 'none';
    fetchAllEmployees();
}

function showAllManagers() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'flex';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';
    document.getElementById('request').style.display = 'none';
    fetchAllManagers();
}

function showAllProjects() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'flex';
    document.getElementById('profile-page').style.display = 'none';
    document.getElementById('request').style.display = 'none';
    fetchAllProjects();
}

function showProfile() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'block';
    document.getElementById('request').style.display = 'none';
    fetchManagerProfile();
}


function showRequests(){
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';
    document.getElementById('request').style.display = 'block';
    fetchAllRequest(managerId);
}
function fetchManagerInfo() {
  
    fetch('http://localhost:8080/api/manager/info', { headers: headers })
        .then(response => response.json())
        .then(data => {
            this.managerId=data.managerId;
            const managerDetails = `
            <h3>${data.fullName}</h3>
            <h4>Projects</h4>
            <ul>
                ${data.projectList.map(project => `<li>Project ID: ${project.projectId}, Name: ${project.projectName}</li>`).join('')}
            </ul>
            <h4>Team Members</h4>
            <ul>
                ${data.employeeList.map(employee => `<li>Employee ID: ${employee.employeeId}, Name: ${employee.fullName}</li>`).join('')}
            </ul>
            `;
            document.getElementById('manager-details').innerHTML = managerDetails;
        })
        .catch(error => {
            console.error('Error fetching manager info:', error);
        });
}

function fetchManagerProfile() {

    fetch('http://localhost:8080/api/manager/info', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const profileDetails = `
            <h3>${data.fullName}</h3>
            <h4>Projects</h4>
            <ul>
                ${data.projectList.map(project => `<li>Project ID: ${project.projectId}, Name: ${project.projectName}</li>`).join('')}
            </ul>
            <h4>Team Members</h4>
            <ul>
                ${data.employeeList.map(employee => `<li>Employee ID: ${employee.employeeId}, Name: ${employee.fullName}</li>`).join('')}
            </ul>
            `;
            document.getElementById('profile-details').innerHTML = profileDetails;
        })
        .catch(error => {
            console.error('Error fetching profile info:', error);
        });
}
function filterEmployees(filter) {
    fetchEmployees(filter);
}
function fetchMyEmployees(){
    fetch('http://localhost:8080/api/manager/employee/team', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = ''; 
           
            data.forEach(employee => {
                
                    const employeeCard = document.createElement('div');
                    employeeCard.className = 'employee-card';

                    const employeeDetails = `<div class="details">
                    <div class="details">
                    <h3>${employee.fullName}</h3>
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p>Skills : ${employee.skills.join(', ')}</p>
                    <button onclick="requestAdmin(${employee}, "UNASSIGN_EMPLOYEE")">Unassign</button>
                </div> `;
                    
                    employeeCard.innerHTML = employeeDetails;
                    employeeList.appendChild(employeeCard);
          
            });
        })
        .catch(error => {
            console.error('Error fetching employee data:', error);
        });
}
function fetchAllEmployees() {
   
        //     const employeeList = document.getElementById('employee-list');
        //     employeeList.innerHTML = '';  // Clear the list
        //     filterBar = document.createElement('div');
        //         filterBar.className = 'filter-bar';
        //     const employeeFilter = `
        //     <button onclick="filterEmployees('all')">ALL</button>
        //     <button onclick="filterEmployees('assigned')">ASSIGNED</button>
        //     <button onclick="filterEmployees('unassigned')">UNASSIGNED</button>
        // `;
        // filterBar.innerHTML = employeeFilter;
        // employeeList.appendChild(filterBar);
      
        fetchEmployees("all");
     
        //     data.forEach(employee => {
        //         const employeeCard = document.createElement('div');
        //         employeeCard.className = 'employee-card';

        //         const employeeDetails = `
        //         <div class="details">
        //         <h3>${employee.fullName}</h3>
        //         <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
        //         <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
        //         <p>Skills : ${employee.skills.join(', ')}</p>
        //         <button onclick="requestEmployee(${employee.employeeId})">Request</button>
        //     </div>
        //         `;

        //         employeeCard.innerHTML = employeeDetails;
        //         employeeList.appendChild(employeeCard);
        //     });
        // })
        // .catch(error => {
        //     console.error('Error fetching employee list:', error);
        // });

        }
function fetchEmployees(filter) {
    console.log("fetch all")

    fetch('http://localhost:8080/api/manager/employee', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = '';  // Clear the list
           
            data.forEach(employee => {
                if (filter === 'all' || (filter === 'assigned' && employee.project) || (filter === 'unassigned' && !employee.project)) {
                    const employeeCard = document.createElement('div');
                    employeeCard.className = 'employee-card';

                    const employeeDetails = filter==='unassigned'?`
            
                    <div class="details">
                    <h3>${employee.fullName}</h3>
                    <p>EmployeeId : ${employee.employeeId}</p>
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p>Skills : ${employee.skills.join(', ')}</p>
                   
                </div> ` :`
                    
            
                    <div class="details">
                    <h3>${employee.fullName}</h3>
                    <p>EmployeeId : ${employee.employeeId}</p>
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p>Skills : ${employee.skills.join(', ')}</p>
                    
                         
                </div>    `;
                    // <button onclick="requestEmployee(${employee.employeeId})">Request</button>
                    employeeCard.innerHTML = employeeDetails;
                    employeeList.appendChild(employeeCard);
                }
            });
        })
        .catch(error => {
            console.error('Error fetching employee data:', error);
        });
}

function fetchAllManagers() {
    

    fetch('http://localhost:8080/api/manager/manager', { headers: headers })
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

function fetchAllProjects() {


    fetch('http://localhost:8080/api/manager/project', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const projectList = document.getElementById('project-list');
            projectList.innerHTML = '';  // Clear the list
            data.forEach(project => {
                const projectCard = document.createElement('div');
                projectCard.className = 'project-card';

                const managerName = project.manager ? project.manager.user.userName : 'None';
                const projectDetails = `
                    <div class="details">
                        <p>Project ID: ${project.projectId}</p>
                        <p>Project Name: ${project.projectName}</p>
                        <p>Manager: ${managerName}</p>
                    </div>
                `;

                projectCard.innerHTML = projectDetails;
                projectList.appendChild(projectCard)
            });
        })
        .catch(error => {
            console.error('Error fetching project list:', error);
        });
}

function requestAdmin(employee, requestTypeName) {
 

    const requestData = {
        requesterId: parseInt(this.managerId),
        requestType: requestTypeName,
        projectId: parseInt(employee.projectId),
        employeeIds:parseInt(employee.employeeId),
        requestDetails: null
    };

    let apiUrl = 'http://localhost:8080/api/manager/request/employees';

    fetch(apiUrl, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(requestData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Request failed');
        }
    })
    .then(data => {
        showMessage('Request submitted successfully!', 'success');
        closeRequestModal();
    })
    .catch(error => {
        showMessage('Error submitting request: ' + error.message, 'error');
    });
}


function openRequestModal() {
    document.getElementById('requestModal').style.display = 'block';
}

// Function to close the request modal
function closeRequestModal() {
    document.getElementById('requestModal').style.display = 'none';
}

// Function to submit the request form
function submitRequestForm(event) {
    event.preventDefault();
    console.log(this.getManagerId);
    const requesterId = this.managerId;
    const requestType = document.getElementById('requestType').value;
    const projectId = document.getElementById('projectId').value;
    const employeeIds = document.getElementById('employeeIds').value.split(',').map(id => id.trim());
    const requestDetails = document.getElementById('requestDetails').value;

    const requestData = {
        requesterId: parseInt(requesterId),
        requestType: requestType,
        projectId: parseInt(projectId),
        employeeIds: employeeIds.map(id => parseInt(id)),
        requestDetails: requestDetails
    };

    let apiUrl = '';

    if (requestType === 'ASSIGN_EMPLOYEE' || requestType === 'UNASSIGN_EMPLOYEE') {
        apiUrl = 'http://localhost:8080/api/manager/request/employees';
    } else if (requestType === 'PROJECT_RESOURCE') {
        apiUrl = 'http://localhost:8080/api/manager/request/projectResource';
    }

    fetch(apiUrl, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(requestData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Request failed');
        }
    })
    .then(data => {
        showMessage('Request submitted successfully!', 'success');
        closeRequestModal();
    })
    .catch(error => {
        showMessage('Error submitting request: ' + error.message, 'error');
    });
}


// Function to filter employees by skills
function filterEmployees() {
    const skills = document.getElementById('skills').value.split(',').map(skill => skill.trim());

    fetch(`http://localhost:8080/api/manager/employee/filter?skills=${skills.join(',')}`, {
        method: 'GET',
        headers: headers,
    })
    .then(response => {
        
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to fetch employees');
        }
    })
    .then(data => {
        displayFilteredEmployees(data);
    })
    .catch(error => {
        showMessage('Error fetching employees: ' + error.message, 'error');
    });
}


// Function to display filtered employees
function displayFilteredEmployees(employees) {
    
    const employeeList = document.getElementById('employee-list');
    employeeList.innerHTML = '';

    if (employees.length === 0) {
        employeeList.innerHTML = '<p>No employees found with the specified skills.</p>';
        return;
    }
    console.log("archit");
    employees.forEach(employee => {
        const employeeCard = document.createElement('div');
        employeeCard.className = 'employee-card';
        console.log(employee)
        const employeeDetails = employee.project==null?`<div class="details">
                    <div class="details">
                    <h3>${employee.fullName}</h3>
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p>Skills : ${employee.skills.join(', ')}</p>
                    <button onclick="requestEmployee(${employee.employeeId})">Request</button>
                </div> ` :`
                    <div class="details">
                    <div class="details">
                    <h3>${employee.fullName}</h3>
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p>Skills : ${employee.skills.join(', ')}</p>
                    
                </div>           
                    `;
        employeeCard.innerHTML = employeeDetails;
        employeeList.appendChild(employeeCard);

    });
}

function fetchAllRequest(managerId){
    
   
    fetch(`http://localhost:8080/api/manager/request/manager/${managerId}`, {
        method: 'GET',
        headers: headers,
        
    })
    .then(response => response.json())
    .then(data => {
        const requestList = document.getElementById('request-list');
        requestList.innerHTML = '';  // Clear the list
       
        data.forEach(request => {
          
                const requestAccordion = document.createElement('div');
                requestAccordion.className = 'accordion-item';

               const requestDetails = `
               <div class = "accordion">
               <h2 class="accordion-header">
                <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                  Request Id : ${request.requestId}
                </button>
              </h2>
              <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                <div class="lst">
                <table>
                <tr>
                <td>Request Type: ${request.requestType}</td>
                        
                       
                        <td>Project ID: ${request.projectId}</td>
                     
                        <td>Employee IDs: ${request.employeeIds.join(', ')}</td>
                        <td>Request Details: ${request.requestDetails}</td>
                        <td>Status: ${request.status}</td>
                        </tr>
                  </table>      
                        
                 </div> 
                </div>
              </div>
              </div>`;

                
                
              requestAccordion.innerHTML =requestDetails;
                requestList.appendChild(requestAccordion);
            
        });
    })
    .catch(error => {
        console.error('Error fetching employee data:', error);
    });
}

function logout() {
    // Clear the auth token from local storage
    localStorage.removeItem('authToken');

    // Redirect to the login page
    window.location.href = '../login/login.html'; // Adjust this to the path of your login page
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
