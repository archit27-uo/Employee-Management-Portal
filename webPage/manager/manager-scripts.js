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
        console.log(response.status);
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
            <h3>${data.fullName}</h3>
            <h4>Projects</h4>
            <ul>
                ${data.projectList.map(project => `<li>Project ID: ${project.projectId}, Name: ${project.projectName}</li>`).join('')}
            </ul>
            <h4>Team Members</h4>
            <ul>
                ${data.employeeList.map(employee => `<li>Employee ID: ${employee.employeeId}, Name: ${employee.fullName}</li>`).join('')}
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
            <h3>${data.fullName}</h3>
            <h4>Projects</h4>
            <ul>
                ${data.projectList.map(project => `<li>Project ID: ${project.projectId}, Name: ${project.projectName}</li>`).join('')}
            </ul>
            <h4>Team Members</h4>
            <ul>
                ${data.employeeList.map(employee => `<li>Employee ID: ${employee.employeeId}, Name: ${employee.fullName}</li>`).join('')}
            </ul></div>
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
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    <div class="btn">
                       <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                       <button onclick="requestForUnassign(${employee.employeeId},${employee.project.projectId})">Unassign</button>
                    </div>
                    <p>Skills : ${employee.skills.join(', ')}</p>
                    
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

// function fetchAllEmployees() {

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

    // fetchEmployees("all");

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

// }
function fetchEmployees(filter) {


    fetch('http://localhost:8080/api/manager/employee', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = '';  // Clear the list

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
                    
                    <p>EmployeeId : ${employee.employeeId}</p>
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    
                    <p>Skills : ${employee.skills.join(', ')}</p>
                   
                </div> ` : `
                    <div class="details card">
                    <h3>${employee.fullName}</h3>
                    <p>EmployeeId : ${employee.employeeId}</p>
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p>Skills : ${employee.skills.join(', ')}</p>     
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
            managerList.innerHTML = '<h3>Manager List</h3>';  // Clear the list
            data.forEach(manager => {
                const managerCard = document.createElement('div');
                managerCard.className = 'manager-card';

                const managerDetails = `
                <div class="details card">
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
            projectList.innerHTML = '<h3>Project List</h3>';  // Clear the list
            data.forEach(project => {
                const projectCard = document.createElement('div');
                projectCard.className = 'project-card';

                const managerName = project.manager ? project.manager.user.userName : 'None';
                const projectDetails = `
                    <div class="details card">
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
    
    console.log("inside request admin")
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
    

    // fetch(apiUrl, {
    //     method: 'POST',
    //     headers: headers,
    //     body: JSON.stringify(requestData)
    // })
    //     .then(response => {
            if (status==200 || status==201) {
                showMessage('Request submitted successfully!', 'success');
                closeAssignProjectModal();
                return data.json();
            }
        // })
        // .then(data => {
        //     showMessage('Request submitted successfully!', 'success');
        //     closeAssignProjectModal();
        // })
        // .catch(error => {
        //     showMessage('Error submitting request: ' + error.message, 'error');
        // });
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
    // fetch(apiUrl, {
    //     method: 'POST',
    //     headers: headers,
    //     body: JSON.stringify(requestData)
    // })
    //     .then(response => {
            if (status==200) {
                showMessage('Request submitted successfully!', 'success');
                closeRequestModal();
                showRequests();
                return response.json();
            } else {
                throw new Error('Request failed');
            }
        // })
        // .then(data => {
        //     showMessage('Request submitted successfully!', 'success');
        //     closeRequestModal();
        // })
        // .catch(error => {
        //     showMessage('Error submitting request: ' + error.message, 'error');
        // });
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
        console.log(employee)
        const employeeDetails = employee.project == null ? `<div class="details">
                    <div class="details card">
                    <div class="btn">
                    <h3>${employee.fullName}</h3>
                    <button onclick="requestEmployee(${employee.employeeId})">Request</button>
                    </div>
                    
                    <p>Project : ${employee.project ? employee.project.projectName : 'None'}</p>
                    
                    
                    <p>Manager : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                    <p>Skills : ${employee.skills.join(', ')}</p>
                 
                </div> ` : `
                    <div class="details card">
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
            
           // requestList.innerHTML = '';  // Clear the list

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
                        <p>Request Type: ${request.requestType}</p>
                        <p>Project ID: ${request.projectId}</p>
                      <p>Employee IDs: ${
    Array.isArray(request.employeeIds) 
    ? (request.employeeIds.length > 0 
        ? request.employeeIds.join(', ') 
        : 'No Employee IDs') 
    : (request.employeeIds || 'No Employee IDs')
}</p>
                        <p>Request Details: ${request.requestDetails}</p>
                        <p class="sts">Status: ${request.status}</p>
                </div>`;
                requestAccordion.innerHTML = requestDetails;
                pending.appendChild(requestAccordion);
              //  requestList.appendChild(requestAccordion);
                }else{
                    
                    if(actionTakenCounter==0){
                        const requestStatus = document.createElement('div');
                        
                        const title =`<h4>Action Taken Request</h4>`;
                        requestStatus.innerHTML=title;
                        actionTaken.appendChild(requestStatus);
                        actionTakenCounter++;
                    }
                    
                    const requestDetails = `
               <button class="accordion"> Request Id : ${request.requestId} </button>
               <div class="panel card">
                        <p>Request Type: ${request.requestType}</p>
                        <p>Project ID: ${request.projectId}</p>
                      <p>Employee IDs: ${
    Array.isArray(request.employeeIds) 
    ? (request.employeeIds.length > 0 
        ? request.employeeIds.join(', ') 
        : 'No Employee IDs') 
    : (request.employeeIds || 'No Employee IDs')
}</p>
                        <p>Request Details: ${request.requestDetails}</p>
                        <p class="sts">Status: ${request.status}</p>
                </div>`;
                requestAccordion.innerHTML = requestDetails;
                actionTaken.appendChild(requestAccordion);
               // requestList.appendChild(requestAccordion);
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
    // Clear the auth token from local storage
    localStorage.removeItem('authToken');

    // Redirect to the login page
    window.location.href = '../login/login.html'; // Adjust this to the path of your login page
}

// Function to display messages
function showMessage(message, type) {
    const messageElement = document.createElement('div');
    messageElement.textContent = message;
    messageElement.className = type === 'success' ? 'success-message' : 'error-message';
    
    // Styling for success and error messages
    if (type === 'success') {
        messageElement.style.backgroundColor = 'green';
        messageElement.style.color = 'white';
    } else if (type === 'error') {
        messageElement.style.backgroundColor = 'red';
        messageElement.style.color = 'white';
    }
    

    // Common styles for the message element
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