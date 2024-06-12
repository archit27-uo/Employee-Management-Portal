document.addEventListener("DOMContentLoaded", function () {
    showHomePage();
});

var managerId;
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


function getManagerId() {
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
    fetchEmployees('all');
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


function showRequests() {
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
            this.managerId = data.managerId;
            const managerDetails = `<div class="card">
            <h2>${data.fullName}</h2>
            </br>
            </br>
            <h3>Projects</h3>
            <ul>
                ${data.projectList.map(project => `<li><b>Project ID:</b> ${project.projectId}, <b>Name:</b> ${project.projectName}</li>`).join('')}
            </ul>
            </br>
            </br>
            <h3>Team Members</h3>
            <ul>
                ${data.employeeList.map(employee => `<li><b>Employee ID:</b> ${employee.employeeId}, <b>Name:</b> ${employee.fullName}</li>`).join('')}
            </ul>
             </div>`;
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
            const profileDetails = `<div class="card">
            <h2>${data.fullName}</h2>
            </br>
            </br>
            <h3>Projects</h3>
            <ul>
                ${data.projectList.map(project => `<li><b>Project ID:</b> ${project.projectId}, <b>Name:</b> ${project.projectName}</li>`).join('')}
            </ul>
            </br>
            </br>
            <h3>Team Members</h3>
            <ul>
                ${data.employeeList.map(employee => `<li><b>Employee ID:</b> ${employee.employeeId}, <b>Name:</b> ${employee.fullName}</li>`).join('')}
            </ul>
             </div>`;
            document.getElementById('profile-details').innerHTML = profileDetails;
        })
        .catch(error => {
            console.error('Error fetching profile info:', error);
        });
}

function filterEmployees(filter) {
    fetchEmployees(filter);
}


function fetchMyEmployees() {



    fetch('http://localhost:8080/api/manager/employee/team', { headers: headers })
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
                    <div class="btn">
                       <p><b>Manager:</b> ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                       <button onclick="requestForUnassign(${employee.employeeId},${employee.project.projectId})">Unassign</button>
                    </div>
                    <p><b>Skills:</b> ${employee.skills.join(', ')}</p>
                    
                </div> `;

                employeeCard.innerHTML = employeeDetails;
                employeeList.appendChild(employeeCard);

            });
        })
        .catch(error => {
            console.error('Error fetching employee data:', error);
        });
}

function requestForUnassign(employeeId, projectId){
    let arr = [employeeId];
    const employee = {
        employeeId : arr,
        projectId: projectId
    }
    requestAdmin(employee, "UNASSIGN_EMPLOYEE")
}


function fetchEmployees(filter) {


    fetch('http://localhost:8080/api/manager/employee', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = ''; 

            data.forEach(employee => {
                if (filter === 'all' || (filter === 'assigned' && employee.project) || (filter === 'unassigned' && !employee.project)) {
                    const employeeCard = document.createElement('div');
                    employeeCard.className = 'employee-card';

                    const employeeDetails = filter === 'unassigned' ? `
            
                    <div class="details card">
                    <div class="btn">
                    <h3>${employee.fullName}</h3>
                    <button onclick="openAssignProjectModal(${employee.employeeId})">Request</button>
                    </div>
                    
                    <p><b>EmployeeId:</b> ${employee.employeeId}</p>
                    <p><b>Project:</b> ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p><b>Manager:</b> ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    
                    <p><b>Skills:</b> ${employee.skills.join(', ')}</p>
                   
                </div> ` : `
                    <div class="details card">
                    <h3>${employee.fullName}</h3>
                    <p><b>EmployeeId:</b> ${employee.employeeId}</p>
                    <p><b>Project:</b> ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p><b>Manager:</b> ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p><b>Skills:</b> ${employee.skills.join(', ')}</p>     
                </div>`;
                    
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
            managerList.innerHTML = '<h3>Manager List</h3>'; 
            data.forEach(manager => {
                const managerCard = document.createElement('div');
                managerCard.className = 'manager-card';

                const managerDetails = `
                <div class="details card">
                <h3>${manager.user.userName}</h3>
                <p><b>Email:</b> ${manager.user.userEmail}</p>
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
            projectList.innerHTML = '<h3>Project List</h3>';  
            data.forEach(project => {
                const projectCard = document.createElement('div');
                projectCard.className = 'project-card';

                const managerName = project.manager ? project.manager.user.userName : 'None';
                const projectDetails = `
                    <div class="details card">
                        <p><b>Project ID:</b> ${project.projectId}</p>
                        <p><b>Project Name:</b> ${project.projectName}</p>
                        <p><b>Manager:</b> ${managerName}</p>
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

function openAssignProjectModal(employeeId) {
    document.getElementById('assignEmployeeId').value = employeeId;
    document.getElementById('assignProjectModal').style.display = 'block';
}

// Close the Assign Project Modal
function closeAssignProjectModal() {
    document.getElementById('assignProjectModal').style.display = 'none';
}
function requestEmployee(event){
    event.preventDefault();
    const employeeId = document.getElementById('assignEmployeeId').value;
    const projectId = document.getElementById('assignProjectId').value;
    let arr = [employeeId];

    const employee ={
        employeeId:arr,
        projectId:parseInt(projectId)
    }
    requestAdmin(employee, "ASSIGN_EMPLOYEE");
}


async function requestAdmin(employee, requestTypeName) {
    
  
    const requestData = {
        requesterId: parseInt(this.managerId),
        requestType: requestTypeName,
        projectId: employee.projectId,
        employeeIds: employee.employeeId,
        requestDetails: null
    };
    let apiUrl = 'http://localhost:8080/api/manager/request/employees';
    const {status, data} = await fetchData(apiUrl, {
        method: 'POST',
        body: JSON.stringify(requestData)
    })
    

   
            if (status==200 || status==201) {
                showMessage('Request submitted successfully!', 'success');
                closeAssignProjectModal();
               
            }

}


function openRequestModal() {
    document.getElementById('requestModal').style.display = 'block';
}

// Function to close the request modal
function closeRequestModal() {
    document.getElementById('requestModal').style.display = 'none';
}

// Function to submit the request form
async function submitRequestForm(event) {
    event.preventDefault();
    
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
    const {status, response} = await fetchData(apiUrl, {
        method: 'POST',
        body: JSON.stringify(requestData)
    })
    
            if (status==200) {
                showMessage('Request submitted successfully!', 'success');
                closeRequestModal();
                showRequests();
                return response.json();
            } else {
                throw new Error('Request failed');
            }
      
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
    
    employees.forEach(employee => {
        const employeeCard = document.createElement('div');
        employeeCard.className = 'employee-card';
      
        const employeeDetails = employee.project == null ? `<div class="details">
                    <div class="details card">
                    <div class="btn">
                    <h3>${employee.fullName}</h3>
                    <button onclick="openAssignProjectModal(${employee.employeeId})">Request</button>
                    </div>
                    
                    <p><b>Project:</b> ${employee.project ? employee.project.projectName : 'None'}</p>
                    
                    
                    <p><b>Manager:</b> ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p><b>Skills:</b> ${employee.skills.join(', ')}</p>
                 
                </div> ` : `
                    <div class="details card">
                    <div class="details">
                
                    <h3>${employee.fullName}</h3>
                 
                    <p><b>Project:</b> ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p><b>Manager:</b> ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p><b>Skills:</b> ${employee.skills.join(', ')}</p>
                    
                </div>           
                    `;
        employeeCard.innerHTML = employeeDetails;
        employeeList.appendChild(employeeCard);

    });
}

function fetchAllRequest(managerId) {


    fetch(`http://localhost:8080/api/manager/request/manager/${managerId}`, {
        method: 'GET',
        headers: headers,

    })
        .then(response => response.json())
        .then(data => {
            pendingCounter = 0;
            actionTakenCounter = 0;
            const requestList = document.getElementById('request-list');
            const actionTaken = document.getElementById('action-taken');
            actionTaken.innerHTML='';
            const pending = document.getElementById('pending');
            pending.innerHTML='';
            
          
            data.forEach(request => {
                const requestAccordion = document.createElement('div');
                requestAccordion.className = 'accordion-item';
                
                if(request.status=='PENDING'){
                   
                    if(pendingCounter==0){
                        const requestStatus = document.createElement('div');
                       
                        const title = '<h4>Pending Request</h4>';
                        requestStatus.innerHTML=title;

                        pending.appendChild(requestStatus);
                        pendingCounter++;
                    }
                    
                    const requestDetails = `
               <button class="accordion"> Request Id : ${request.requestId} </button>
               <div class="panel card">
                        <p><b>Request Type:</b> ${request.requestType}</p>
                        <p><b>Project ID:</b> ${request.projectId}</p>
                      <p><b>Employee IDs:</b> ${
    Array.isArray(request.employeeIds) 
    ? (request.employeeIds.length > 0 
        ? request.employeeIds.join(', ') 
        : 'No Employee IDs') 
    : (request.employeeIds || 'No Employee IDs')
}</p>
                        <p><b>Request Details:</b> ${request.requestDetails}</p>
                        <p class="sts"><b>Status:</b> ${request.status}</p>
                </div>`;
                requestAccordion.innerHTML = requestDetails;
                pending.appendChild(requestAccordion);
           
                }else{
                    
                    if(actionTakenCounter==0){
                        const requestStatus = document.createElement('div');
                        
                        const title =`<h4>Action Taken Request</h4>`;
                        requestStatus.innerHTML=title;
                        actionTaken.appendChild(requestStatus);
                        actionTakenCounter++;
                    }
                    
                    const requestDetails = `
               <button class="accordion"> <b>Request Id:</b> ${request.requestId} </button>
               <div class="panel card">
                        <p><b>Request Type:</b> ${request.requestType}</p>
                        <p><b>Project ID:</b> ${request.projectId}</p>
                      <p><b>Employee IDs:</b> ${
    Array.isArray(request.employeeIds) 
    ? (request.employeeIds.length > 0 
        ? request.employeeIds.join(', ') 
        : 'No Employee IDs') 
    : (request.employeeIds || 'No Employee IDs')
}</p>
                        <p><b>Request Details:</b> ${request.requestDetails}</p>
                        <p class="sts"><b>Status:</b> ${request.status}</p>
                </div>`;
                requestAccordion.innerHTML = requestDetails;
                actionTaken.appendChild(requestAccordion);
             
                }
                

                
                
            });

            //  Accordion Panel for Request 
            var acc = document.getElementsByClassName("accordion");
            var i;
            
            for (i = 0; i < acc.length; i++) {
              acc[i].addEventListener("click", function() {
                this.classList.toggle("active");
                var panel = this.nextElementSibling;
                if (panel.style.maxHeight) {
                  panel.style.maxHeight = null;
                } else {
                  panel.style.maxHeight = panel.scrollHeight + "px";
                } 
              });
            }
            // ---------------End!-----------------

        })
        .catch(error => {
            console.error('Error fetching employee data:', error);
        });
}

function logout() {
   
    localStorage.removeItem('authToken');
    window.location.href = '../login/login.html'; 
}


// Function to display messages
function showMessage(message, type) {
    const messageElement = document.createElement('div');
    messageElement.textContent = message;
    messageElement.className = type === 'success' ? 'success-message' : 'error-message';
    
    
    if (type === 'success') {
        messageElement.style.backgroundColor = 'green';
        messageElement.style.color = 'white';
    } else if (type === 'error') {
        messageElement.style.backgroundColor = 'red';
        messageElement.style.color = 'white';
    }
    

    messageElement.style.position = 'fixed';
    messageElement.style.top = '20px';
    messageElement.style.left = '50%';
    messageElement.style.transform = 'translateX(-50%)';
    messageElement.style.padding = '10px';
    messageElement.style.borderRadius = '5px';
    messageElement.style.zIndex = '1000';
    messageElement.style.boxShadow = '0 2px 10px rgba(0,0,0,0.1)';

    document.body.appendChild(messageElement);

    setTimeout(() => {
        document.body.removeChild(messageElement);
    }, 3000);
}
