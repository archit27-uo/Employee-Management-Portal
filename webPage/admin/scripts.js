

document.addEventListener("DOMContentLoaded", function () {

    fetchEmployees('all');
});

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
       // console.error('API Error:', error);
        showMessage(`Failed due to: ${error.message}`, 'error');
        
    }
}

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
    document.getElementById('employee').style.display = 'block';
};

function openAssignProjectModal(employeeId) {
    document.getElementById('assignEmployeeId').value = employeeId;
    document.getElementById('assignProjectModal').style.display = 'block';
}

// Close the Assign Project Modal
function closeAssignProjectModal() {
    document.getElementById('assignProjectModal').style.display = 'none';
}
async function fetchEmployees(filter) {

    const {status, data} = await fetchData('http://localhost:8080/api/admin/employee', { method:'GET' });
    
    // fetch()
    //     .then(response => response.json())
    //     .then(data => {
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = '';  // Clear the list

            data.forEach(employee => {
                if (filter === 'all' || (filter === 'assigned' && employee.project) || (filter === 'unassigned' && !employee.project)) {
                    const employeeCard = document.createElement('div');
                    employeeCard.className = 'employee-card';
                    employeeDetails = ``;
                    if (filter === 'all') {
                        employeeDetails = `
                <div class="card">
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
                    </div>

                    <p>Skills : ${employee.skills.join(', ')}</p>
                </div>`;


                    } else if (filter === 'assigned') {
                        employeeDetails = `
                <div class="card">
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
                           <button onclick="unassignProject(${employee.employeeId})">Unassign</button> 
                          </div>

                        <p>Skills : ${employee.skills.join(', ')}</p>
                        </div>
                </div>        
                    `;
                    } else {
                        employeeDetails = `
                    
                <div class="card">    
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
                </div>
                    `;
                    }
                    employeeCard.innerHTML = employeeDetails;
                    employeeList.appendChild(employeeCard);
                }
            });
        // })
        // .catch(error => {
        //     console.error('Error fetching employee data:', error);
        //     showMessage('Failed to fetch employee data: ' + error.message, 'error');
        // });
        
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

async function unassignProject(employeeId) {

    const url = `http://localhost:8080/api/admin/employee/${employeeId}/unassignProject`;
    const {status, response} = await fetchData(url, {
        method: 'PUT'
    })
    // fetch(url, {
    //     method: 'PUT',
    //     headers: headers
    // })
    //     .then(response => {
        //     if (response.ok) {
        //         return response.text();
        //     } else {
        //         throw new Error('Failed to unassign project');
        //     }
        // })
    
        if(status==200){
            fetchEmployees('assigned');
            showMessage("Unassigned project successfully", "success");
        }
        
        // .catch(error => {
        //     console.error('Error unassigning project:', error);
        //     showMessage('Failed to unassign project: ' + error.message, 'error');
        // });
       
}
function showProjects() {

    fetchProjects();

    document.getElementById('project').style.display = 'block';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('request-list').style.display = 'none';

}

async function fetchProjects() {

    const {status, data} = await fetchData('http://localhost:8080/api/admin/projects','GET')
    // fetch('http://localhost:8080/api/admin/projects', {
    //     method: 'GET',
    //     headers: headers
    // })
    //     .then(response => response.json())
    //     .then(data => {
            if(status==200){
                const projectList = document.getElementById('project-list');
            projectList.innerHTML = '';
            data.forEach(project => {
                const projectCard = document.createElement('div');
                projectCard.classList.add('project-card');
                projectCard.setAttribute('data-project-id', project.projectId);

                let managerInfo = project.manager ?
                    `<p>Manager: ${project.manager.user.userName}</p>` :
                    `<p>No manager assigned</p>`;

                projectCard.innerHTML = `
            <div class="card">
            <div class="project-info">
                    <h3>${project.projectName}</h3>
                    ${managerInfo}
                </div>
                <button class="expand-btn" onclick="fetchEmployeesByProject(${project.projectId})">Expand</button>
            </div>`;
                projectList.appendChild(projectCard);
            });
            }
            
      
}

async function fetchEmployeesByProject(projectId) {

    const {status, data} = await fetchData(`http://localhost:8080/api/manager/employee/project/${projectId}`, {
        method: 'GET'
    });

    if(status==200){
        displayEmployees(projectId, data);
    }
    // fetch()
    //     .then(response => {
    //         if (!response.ok) {
    //             throw new Error('Failed to fetch employees');
    //         }
    //         return response.json();
    //     })
    //     .then(employees => {
            
    //     })
    //     .catch(error => {
    //         console.error('Error fetching employees:', error);
    //         showMessage('Error fetching employees: ' + error.message, 'error');
    //     });
}

// Function to display employees under the relevant project
function displayEmployees(projectId, employees) {
    const projectCard = document.querySelector(`[data-project-id="${projectId}"]`);
    let employeeList = projectCard.querySelector('.employee-list');

    // If the employee list doesn't exist, create it
    if (!employeeList) {
        employeeList = document.createElement('div');
        employeeList.classList.add('employee-list');
        projectCard.appendChild(employeeList);
    }

    // Clear previous employee list content
    employeeList.innerHTML = '';

    // Create a list of employees
    employees.forEach(employee => {
        const employeeCard = document.createElement('div');
        employeeCard.classList.add('employee-card');

        let skills = employee.skills.join(', ');

        employeeCard.innerHTML = `
            <p>Employee ID: ${employee.employeeId}</p>
            <p>Full Name: ${employee.fullName}</p>
            <p>Skills: ${skills}</p>
        `;

        employeeList.appendChild(employeeCard);
    });
}

function showProjects() {
    // Fetch and display projects here
    fetchProjects();
    // Show project list and hide other lists
    document.getElementById('project').style.display = 'block';
    document.getElementById('request-list').style.display = 'none';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
}


function showManagers() {
    document.getElementById('project').style.display = 'none';
    document.getElementById('request-list').style.display = 'none';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'block';
    fetchAllManagers();
}

async function fetchAllManagers() {

    const {status, data} = await fetchData('http://localhost:8080/api/admin/manager',{method:'GET'});
    // fetch(, { headers: headers })
    //     .then(response => response.json())
    //     .then(data => {
        if(status==200){
            const managerList = document.getElementById('manager-list');
            managerList.innerHTML = '<div class="gap"><h3>Manager List</h3></div>';  // Clear the list
            data.forEach(manager => {
                const managerCard = document.createElement('div');
                managerCard.className = 'manager-card';

                const managerDetails = `
            <div class="card">
                <div class="details">
                <h3>${manager.user.userName}</h3>
                <p>Email : ${manager.user.userEmail}</p>
            </div>
            </div>
                `;

                managerCard.innerHTML = managerDetails;
                managerList.appendChild(managerCard);
            });
        }
            
        // })
        // .catch(error => {
        //     console.error('Error fetching manager list:', error);
        //     showMessage('Failed to fetch mamnager list: ' + error.message, 'error');
        // });
}

function showRequests() {

    // Fetch and display requests here

    // Show request list and hide other lists
    document.getElementById('project').style.display = 'none';
    document.getElementById('request-list').style.display = 'block';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    fetchRequests();
}
pendingCounter = 0;
actionTakenCounter = 0;
async function fetchRequests() {

    const {status, data} = await fetchData('http://localhost:8080/api/admin/request', {
        method: 'GET'
    });
    // fetch()
    //     .then(response => response.json())
    //     .then(data => {
        if(status==200){
            const requestList = document.getElementById('request-list');
            requestList.innerHTML = '<h3>Request List</h3><div class="pending" id="pending"></div>';  // Clear the list

            data.forEach(request => {
                const requestCard = document.createElement('div');
                requestCard.className = 'request-card';

                const requesterName = request.requester.user.userName;
                

                if(request.status=="PENDING"){
                    if(pendingCounter==0){
                        const title = '<h4>Pending Request</h4>';
                        pending.append(title);
                        pendingCounter++;
                    }
                    const pending = document.getElementById('pending');
                    const requestDetails = `
                    <div class="card"><table>
                     <div class="details">
    
                    <tr>   
                            <td>Request ID: ${request.requestId}</td>
                            <td>Project ID: ${request.projectId}</td>
                    </tr>     
                     
                    <tr>
                            <td>Requester: ${requesterName}</td>
                            <td>Employee IDs: ${request.employeeIds.join(', ')}</td>  
                            <td>Status: ${request.status}</td>   
                    </tr>
    
                    <tr>
                            <td>Request Type: ${request.requestType}</td>
                            <td>Request Details: ${request.requestDetails}</td>
                            <td align=center>
                            <i class="fas fa-check" style="color: green; cursor: pointer;" onclick="handleRequest('approve', ${request.requestId})"></i>
                            &nbsp;&nbsp;
                            <i class="fas fa-times" style="color: red; cursor: pointer;" onclick="handleRequest('reject', ${request.requestId})"></i>
                            </td>
                    </tr>       
    
                         </div>
                       </table></div>
                `;
                requestCard.innerHTML = requestDetails;
                pending.appendChild(requestCard);
                }else{
                    
                    
                     if(actionTakenCounter==0){
                        const requestStatus = document.createElement('div');
                        requestStatus.className = 'action-taken';
                        requestStatus.id= 'action-taken';
                        const title =`<h4>Action Taken Request</h4>`;
                        requestStatus.innerHTML=title;
                        requestList.append(requestStatus);
                        actionTakenCounter++;
                    }
                    const actionTaken = document.getElementById('action-taken');
                    const requestDetails = `
                    <div class="card"><table>
                     <div class="details">
    
                    <tr>   
                            <td>Request ID: ${request.requestId}</td>
                            <td>Project ID: ${request.projectId}</td>
                    </tr>     
                     
                    <tr>
                            <td>Requester: ${requesterName}</td>
                            <td>Employee IDs: ${request.employeeIds.join(', ')}</td>  
                            <td>Status: ${request.status}</td>   
                    </tr>
    
                    <tr>
                            <td>Request Type: ${request.requestType}</td>
                            <td>Request Details: ${request.requestDetails}</td>
                           
                    </tr>       
    
                         </div>
                       </table></div>
                `;
                requestCard.innerHTML = requestDetails;
                actionTaken.appendChild(requestCard);
                }  
            });
        }
            
        // })
        // .catch(error => {
        //     console.error('Error in fetching requests:', error);
        //     showMessage('Failed to fetch request: ' + error.message, 'error');
        // });
}




function addEmployee() {
    // Function to trigger adding an employee
    
}

async function assignProject(event) {
    event.preventDefault();

    const employeeId = document.getElementById('assignEmployeeId').value;
    const projectId = document.getElementById('assignProjectId').value;
    const url = `http://localhost:8080/api/admin/employee/${employeeId}/assignProject/${projectId}`;
    const {status, data} = await fetchData(url, {
        method: 'PUT'
    });
    // fetch()
    //     .then(response => {
            if (status==200) {
                showMessage(`Assign project to employee with ID: ${employeeId}`,'success')
                closeAssignProjectModal();
                fetchEmployees('unassigned')
            } 
        // })
        // .then(message => {
        //     
        // })
        // .catch(error => {
        //     console.error('Error in assigning project:', error);
        //     showMessage('Failed to assign project: ' + error.message, 'error');
        // });
    
}

async function editEmployee(employeeId) {
    const {status, data} = await fetchData(`http://localhost:8080/api/admin/employee/${employeeId}`,{method:'GET'});
    // fetch(, { headers: headers })
    //     .then(response => response.json())
    if(status==200){
        openEditEmployeeModal(data);
    }
       
        // .then(employee => )
        // .catch(error => {
        //     console.error('Error fetching employee detail:', error);
        //     showMessage('Failed to fetch employee detail: ' + error.message, 'error');
        // });
}

async function submitEditEmployeeForm(event) {
    event.preventDefault();

    const employeeId = parseInt(document.getElementById('editEmployeeId').value);
    const fullName = document.getElementById('editFullName').value;
    const projectId = parseInt(document.getElementById('editProjectId').value);
    const managerId = parseInt(document.getElementById('editManagerId').value);
    const skills = document.getElementById('editSkills').value.split(',').map(skill => skill.trim());

    const employeeData = {
        employeeId: employeeId,
        fullName: fullName,
        projectId: projectId ?  projectId  : null,
        managerId: managerId ? managerId  : null,
        skills: skills
    };


    const {status, data} = await fetchData(`http://localhost:8080/api/admin/employee`,{
        method: 'PUT',
        body: JSON.stringify(employeeData)
    });
    // fetch(, {
    //     method: 'PUT',
    //     headers: headers,
    //     body: JSON.stringify(employeeData)
    // })
    //     .then(response => {
            if (status==200) {
                closeEditEmployeeModal();
                fetchEmployees('all')
;                return data.text();
            }
        //     }else{
        //         return response.json().then(errorResponse => {
        //             throw new Error(errorResponse.message || 'Failed to update employee');
        //         });
        //     }
        // })
        // .catch(error => {
        //     console.error('Error updating employee:', error);
        //     showMessage('Failed to update employee: ' + error.message, 'error');
        // });
}

async function deleteEmployee(employeeId) {
    // Function to delete employee
    try{
        const {status, data} = await fetchData(`http://localhost:8080/api/admin/employee/${employeeId}`, {
            method: 'DELETE'});
            if(status==200){
                showMessage('Employee successfully deleted.', 'success');
                fetchEmployees('all');
            }
            
    }catch(error){
        console.log(error);
    }
    

    // fetch()
    //     .then(response => response.text())
    //     .then(message => {
       
               
                // Optionally, refresh the employee list or remove the deleted employee's card from the DOM
               
               
           
            //  else {
            //     return response.json().then(errorResponse => {
            //         throw new Error(errorResponse.message || 'Failed to update employee');
            //     });
            // }
        // })
        // .catch(error => {
        //     console.error('Error in deleting employee:', error);
        //     showMessage('Failed to delete employee: ' + error.message, 'error');
        // });
   
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
                const manager = {
                    userId: data.userId
                }
                fetch('http://localhost:8080/api/admin/manager', {
                    method: 'POST',
                    headers: headers,
                    body: JSON.stringify(manager)
                })
                    .then(response => response.json())
                    .then(data => {
                        showMessage('Manager added successfully','Success');
                    })
                    .catch(error => {
                        console.error('Error adding manager:', error);
                        showMessage('Failed to add manager: ' + error.message, 'error');
                    });

                closeAddUserModal();
            }
        })
        .catch(error => {
            console.error('Error adding user:', error);
            showMessage('Failed to add user: ' + error.message, 'error');
        });
}

async function submitEmployeeForm(event) {
    event.preventDefault();
    

    const employee = {
        userId: document.getElementById('employeeUserId').value,
        fullName: document.getElementById('fullName').value,
        projectId: document.getElementById('projectId').value,
        managerId: document.getElementById('managerId').value,
        skills: document.getElementById('skills').value.split(',')
    };

    const {status,data} = await fetchData('http://localhost:8080/api/admin/employee', {
        method: 'POST',
        body: JSON.stringify(employee)
    });
    // fetch('http://localhost:8080/api/admin/employee', {
    //     method: 'POST',
    //     headers: headers,
    //     body: JSON.stringify(employee)
    // })
        // .then(response => response.json())
            //alert('Employee added successfully');
            if(status==201){
            fetchEmployees('all');
            showMessage('Employee added successfully', 'success');
            closeAddEmployeeModal();
            }
            
            
}

async function handleRequest(chooseAction, requestId) {
    const url = chooseAction === 'reject' ?
        `http://localhost:8080/api/admin/request/${requestId}/reject` :
        `http://localhost:8080/api/admin/request/${requestId}/approve`;

    if (chooseAction == 'reject') {
        const{status,data} = await fetchData(url, {
            method: 'PUT',
        });
        if(status==200){
            fetchRequests();
        }
        // fetch(url, {
        //     method: 'PUT',
        //     headers: headers,
        // })
    } else {
        const{status,data} = fetchData(url, {
            method: 'PUT',
        });
        if(status==200){
            fetchRequests();
        }
        // fetch(url, {
        //     method: 'PUT',
        //     headers: headers,
        // }).catch(error => {
        //     alert("Error in request cancel " + error)
        // })
    }
    // Implement logic to handle request action (accept/reject)
    console.log(`Request ID: ${requestId}, Action: ${chooseAction}`);
    // You can make API call to update request status here
}

function openAddProjectModal() {
    document.getElementById('addProjectModal').style.display = 'block';
}

// Function to close the Add Project modal
function closeAddProjectModal() {
    document.getElementById('addProjectModal').style.display = 'none';
}

// Function to handle the Add Project form submission
async function submitAddProjectForm(event) {
    event.preventDefault();

    const projectName = document.getElementById('projectName').value;
    const managerId = document.getElementById('projectManagerId').value;

    const newProject = {
        projectName: projectName,
        managerId: managerId
    };

    const {status, data} = await fetchData('http://localhost:8080/api/admin/project',{
        method: 'POST',
        body: JSON.stringify(newProject)
    })
    // fetch(, {
    //     method: 'POST',
    //     headers: headers,
    //     body: JSON.stringify(newProject)
    // })
        // .then(response => {
            if (status==201) {
                showMessage('Project added successfully', 'success');
                closeAddProjectModal();
                fetchProjects();
            } else {
                throw new Error('Failed to add project');
            }
        // })
        // .catch(error => {
        //     console.error('Error in adding project:', error);
        //     showMessage('Failed to add project: ' + error.message, 'error');
        // });
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
