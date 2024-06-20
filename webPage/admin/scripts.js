document.addEventListener("DOMContentLoaded", function () {

    fetchEmployees('all');
});

var token = localStorage.getItem("adminAuthToken");
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

function openAddUserModal() {
    document.getElementById('addUserModal').style.display = 'block';
}

function closeAddUserModal() {
    document.getElementById('addUserModal').style.display = 'none';
}

function logout() {
    localStorage.removeItem('adminAuthToken');
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
    
    
            const employeeList = document.getElementById('employee-list');
            employeeList.innerHTML = ''; 
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
                        <i onclick="editEmployee(${employee.employeeId})" class="fas fa-pencil-alt edit-icon"></i>
                        <i onclick="deleteEmployee(${employee.employeeId})" class="fas fa-trash delete-icon"></i>
                    </div>
                    </div>
                
                    <div class="actions">
                     <div class="lst">
                     <div>
                      <p><b>Project</b> : ${employee.project ? employee.project.projectName : 'None'}</p>
                      <p><b>Manager</b> : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                      </div> 
                     </div> 
                    </div>

                    <p><b>Skills</b> : ${employee.skills.join(', ')}</p>
                </div>`;


                    } else if (filter === 'assigned') {
                        employeeDetails = `
                <div class="card">
                        <div class="details">
                         <p id="empname">${employee.fullName}</p>
                          <div>
                            <i onclick="editEmployee(${employee.employeeId})" class="fas fa-pencil-alt edit-icon"></i>
                            <i onclick="deleteEmployee(${employee.employeeId})" class="fas fa-trash delete-icon"></i>
                        </div>
                        </div>

                        <div class="actions">
                        
                        <div class="lst">
                          <div>
                           <p><b>Project</b> : ${employee.project ? employee.project.projectName : 'None'}</p>
                           <p><b>Manager</b> : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                          </div>  
                           <button onclick="unassignProject(${employee.employeeId})">Unassign</button> 
                          </div>

                        <p><b>Skills</b> : ${employee.skills.join(', ')}</p>
                        </div>
                </div>        
                    `;
                    } else {
                        employeeDetails = `
                    
                <div class="card">    
                        <div class="details">
                    <p id="empname">${employee.fullName}</p>
                     <div>
                        <i onclick="editEmployee(${employee.employeeId})" class="fas fa-pencil-alt edit-icon"></i>
                        <i onclick="deleteEmployee(${employee.employeeId})" class="fas fa-trash delete-icon"></i>
                    </div>
                 </div>
                <div class="actions">
                <div class="lst">
                <div>
                <p><b>Project</b> : ${employee.project ? employee.project.projectName : 'None'}</p>
                <p><b>Manager</b> : ${employee.manager ? employee.manager.user.userName : 'None'}</p>
                </div>  
                <button onclick="openAssignProjectModal(${employee.employeeId})">Assign</button> 
                </div>
                <p><b>Skills</b> : ${employee.skills.join(', ')}</p>
                </div>
                </div>
                    `;
                    }
                    employeeCard.innerHTML = employeeDetails;
                    employeeList.appendChild(employeeCard);
                }
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

async function unassignProject(employeeId) {

    const url = `http://localhost:8080/api/admin/employee/${employeeId}/unassignProject`;
    const {status, response} = await fetchData(url, {
        method: 'PUT'
    })
        if(status==200){
            fetchEmployees('assigned');
            showMessage("Unassigned project successfully", "success");
        }      
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

            if(status==200){
                const projectList = document.getElementById('project-list');
            projectList.innerHTML = '';
            data.forEach(project => {
                const projectAccordion = document.createElement('div');
                projectAccordion.className = 'accordion-item';
                projectAccordion.setAttribute('data-project-id', project.projectId);
     

                let managerInfo = project.manager ?
                    `<span><b>Manager</b>: ${project.manager.user.userName}</span>` :
                    `<span>No manager assigned</span>`;

            projectAccordion.innerHTML = `<button class="accordion"  onclick="fetchEmployeesByProject(${project.projectId})"> <b>Project Id</b> : ${project.projectId}   | ${managerInfo}</button>
            <div class="panel card" data-panel-id="${project.projectId}"></div>`;
                   //     const projectCard = document.createElement('div');
            //     projectCard.classList.add('project-card');
            //     projectCard.setAttribute('data-project-id', project.projectId);
            // <div class="card">
            // <div class="project-info">
            //         <h3>${project.projectName}</h3>
            //         ${managerInfo}
            //     </div>
            //     <button class="expand-btn" onclick="fetchEmployeesByProject(${project.projectId})">Expand</button>
            // </div>`;
                projectList.appendChild(projectAccordion);
            });
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
            }
            
      
}

async function fetchEmployeesByProject(projectId) {

    const {status, data} = await fetchData(`http://localhost:8080/api/manager/employee/project/${projectId}`, {
        method: 'GET'
    });

    if(status==200){
        displayEmployees(projectId, data);
    }
   
}

// Function to display employees 
function displayEmployees(projectId, employees) {
    // const container = document.getElementById('employee-cards-container');
    // container.innerHTML = ''; // Clear any existing cards

    // employees.forEach(employee => {
    //     const employeeCard = document.createElement('div');
    //     employeeCard.className = 'employee-small-card';

    //     const employeeDetails = `
    //         <tr class="employee-id">Employee ID: ${employee.employeeId}</tr>
    //         <tr class="employee-name">Full Name: ${employee.fullName}</tr>
    //         <tr class="employee-skills">Skills: ${employee.skills.join(', ')}</tr>
    //     `;

    //     employeeCard.innerHTML = employeeDetails;
    //     container.appendChild(employeeCard);
    // });
    const projectCard = document.querySelector(`[data-panel-id="${projectId}"]`);
    let employeeList = projectCard.querySelector('.employee-small-list');

    if (!employeeList) {
        employeeList = document.createElement('div');
        employeeList.classList.add('employee-small-list');
        projectCard.appendChild(employeeList);
    }

    employeeList.innerHTML = ''; // Clear any existing cards

    employees.forEach(employee => {
        const employeeCard = document.createElement('div');
        employeeCard.className = 'employee-small-card';

        let skills = employee.skills.join(', ');

        employeeCard.innerHTML = `
            <div class="employee-id">Employee ID: ${employee.employeeId}</div>
                    <div class="employee-details">
                        <div class="detail-item">
                            <span class="detail-title">Name:</span> ${employee.fullName}
                        </div>
                        <div class="detail-item">
                            <span class="detail-title">Skills:</span> ${skills}
                        </div>
                    </div>
        `;

        employeeList.appendChild(employeeCard);
    });
}

function showProjects() {
    fetchProjects();
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
                <p><b>ManagerId</b> : ${manager.managerId} </p>
                <p><b>Email</b> : ${manager.user.userEmail}</p>
            </div>
            </div>
                `;

                managerCard.innerHTML = managerDetails;
                managerList.appendChild(managerCard);
            });
        }
}

async function showRequests() {

    document.getElementById('project').style.display = 'none';
    document.getElementById('request-list').style.display = 'block';
    document.getElementById('employee').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    await fetchRequests();
}



async function fetchRequests() {

    const {status, data} = await fetchData('http://localhost:8080/api/admin/request', {
        method: 'GET'
    });
    
        if(status==200){
            pendingCounter = 0;
            actionTakenCounter = 0;
            const requestList = document.getElementById('request-list');
            
            const pending = document.getElementById('pending');
            pending.innerHTML='';
            const actionTaken = document.getElementById('action-taken');
            actionTaken.innerHTML='';
            data.forEach(request => {
                const requestCard = document.createElement('div');
                requestCard.className = 'request-card';

                const requesterName = request.requester.user.userName;
                

                if(request.status=="PENDING"){
                    if(pendingCounter==0){
                        const requestStatus = document.createElement('div');
                       
                        const title = '<h3>Pending Request List</h3>';
                        requestStatus.innerHTML=title;

                        pending.appendChild(requestStatus);
                        pendingCounter++;
                    }
                    
                    const requestDetails = `
                    <div class="card"><table>
                     <div class="details">
    
                    <tr>   
                            <td><b>Request ID:</b> ${request.requestId}</td>
                            <td><b>Project ID:</b> ${request.projectId}</td>
                    </tr>     
                     
                    <tr>
                            <td><b>Requester:</b> ${requesterName}</td>
                            <td><b>Employee IDs:</b> ${Array.isArray(request.employeeIds) && request.employeeIds.length > 0 ? request.employeeIds.join(', ') : (request.employeeIds || 'No Employee IDs')}</td>  
                            <td><b>Status:</b> ${request.status}</td>   
                    </tr>
    
                    <tr>
                            <td><b>Request Type:</b> ${request.requestType}</td>
                            <td><b>Request Details:</b> ${request.requestDetails}</td>
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
                        const title =`<h3>Action Taken Request</h3>`;
                        requestStatus.innerHTML=title;
                        actionTaken.appendChild(requestStatus);
                        actionTakenCounter++;
                    }
                   
                    const requestDetails = `
                    <div class="card"><table>
                     <div class="details">
    
                    <tr>   
                            <td><b>Request ID:</b> ${request.requestId}</td>
                            <td><b>Project ID:</b> ${request.projectId}</td>
                    </tr>     
                     
                    <tr>
                            <td><b>Requester:</b> ${requesterName}</td>
                             <td><b>Employee IDs:</b> ${Array.isArray(request.employeeIds) && request.employeeIds.length > 0 ? request.employeeIds.join(', ') : (request.employeeIds || 'No Employee IDs')}</td>    
                            <td><b>Status:</b> ${request.status}</td>   
                    </tr>
    
                    <tr>
                            <td><b>Request Type:</b> ${request.requestType}</td>
                            <td><b>Request Details:</b> ${request.requestDetails}</td>
                           
                    </tr>       
    
                         </div>
                       </table></div>
                `;
                requestCard.innerHTML = requestDetails;
                actionTaken.appendChild(requestCard);
                }  
            });
        }
 
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

            if (status==200) {
                showMessage(`Assign project to employee with ID: ${employeeId}`,'success')
                closeAssignProjectModal();
                fetchEmployees('unassigned')
            } 
      
    
}

async function editEmployee(employeeId) {
    const {status, data} = await fetchData(`http://localhost:8080/api/admin/employee/${employeeId}`,{method:'GET'});
    
    if(status==200){
        openEditEmployeeModal(data);
    }
      
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
            if (status==200) {
                document.getElementById('editEmployeeId').value='';
                document.getElementById('editFullName').value='';
                document.getElementById('editProjectId').value='';
                document.getElementById('editManagerId').value='';
                document.getElementById('editSkills').value='';
                closeEditEmployeeModal();
                fetchEmployees('all');
                return data.text();
            }

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
}

function filterEmployees(filter) {
    const filterBar = document.getElementById('filter-bar');
    const buttons = filterBar.getElementsByTagName('button');

    for (let button of buttons) {
        button.classList.remove('active');
    }

    switch (filter) {
        case 'all':
            buttons[0].classList.add('active');
            break;
        case 'assigned':
            buttons[1].classList.add('active');
            break;
        case 'unassigned':
            buttons[2].classList.add('active');
            break;
    }
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
        userEmail : document.getElementById('userEmail').value + '@nucleusteq.com',
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
                document.getElementById('userName').value='';
                document.getElementById('userEmail').value='';
                document.getElementById('userPassword').value='';
                document.getElementById('userRole').value='';
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
                    document.getElementById('userName').value='';
                    document.getElementById('userEmail').value='';
                    document.getElementById('userPassword').value='';
                    document.getElementById('userRole').value='';
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
            if(status==201){
            fetchEmployees('all');
            showMessage('Employee added successfully', 'success');
            document.getElementById('employeeUserId').value='';
            document.getElementById('fullName').value='';
            document.getElementById('projectId').value='';
            document.getElementById('managerId').value='';
            document.getElementById('skills').value='';
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
       
    } else {
        const{status,data} = fetchData(url, {
            method: 'PUT',
        });
       
    }
    await fetchRequests();
    
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
 
            if (status==201) {
                showMessage('Project added successfully', 'success');
                document.getElementById('projectName').value='';
                document.getElementById('projectManagerId').value='';
                closeAddProjectModal();
                fetchProjects();
            } else {
                throw new Error('Failed to add project');
            }
      
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
