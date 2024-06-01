document.addEventListener("DOMContentLoaded", function() {
    showHomePage();
});

function showHomePage() {
    document.getElementById('home-page').style.display = 'block';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';

    fetchManagerInfo();
}

function showAllEmployees() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'flex';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';

    fetchAllEmployees();
}

function showAllManagers() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'flex';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'none';

    fetchAllManagers();
}

function showAllProjects() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'flex';
    document.getElementById('profile-page').style.display = 'none';

    fetchAllProjects();
}

function showProfile() {
    document.getElementById('home-page').style.display = 'none';
    document.getElementById('employee-list').style.display = 'none';
    document.getElementById('manager-list').style.display = 'none';
    document.getElementById('project-list').style.display = 'none';
    document.getElementById('profile-page').style.display = 'block';

    fetchManagerProfile();
}

function fetchManagerInfo() {
    const username = 'vishal@mail.com';
    const password = 'vishal123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

    fetch('http://localhost:8080/api/manager/info', { headers: headers })
        .then(response => response.json())
        .then(data => {
            const managerDetails = `
                <p>Name: ${data.fullName}</p>
                <p>Project: ${data.project ? data.project.name : 'None'}</p>
                <p>Team Members: ${data.team ? data.team.map(member => member.fullName).join(', ') : 'None'}</p>
            `;
            document.getElementById('manager-details').innerHTML = managerDetails;
        })
        .catch(error => {
            console.error('Error fetching manager info:', error);
        });
}

function fetchManagerProfile() {
    const username = 'vishal@mail.com';
    const password = 'vishal123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

    fetch('http://localhost:8080/api/manager/info', { headers: headers })
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
    const username = 'vishal@mail.com';
    const password = 'vishal123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

    fetch('http://localhost:8080/api/manager/employee', { headers: headers })
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
                        <button onclick="requestEmployee(${employee.employeeId})">Request</button>
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

function fetchAllManagers() {
    const username = 'vishal@mail.com';
    const password = 'vishal123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

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
                        <p>Name: ${manager.user.userName}</p>
                        <p>Email: ${manager.user.userEmail}</p>
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
    const username = 'vishal@mail.com';
    const password = 'vishal123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));

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

function requestEmployee(employeeId) {
    const username = 'vishal@mail.com';
    const password = 'vishal123'; 
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    headers.set('Content-Type', 'application/json');

    fetch('http://localhost:8080/api/employee/unassigned', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify({ employeeId })
    })
    .then(response => response.json())
    .then(data => {
        showMessage('Employee requested successfully!', 'success');
    })
    .catch(error => {
        console.error('Error requesting employee:', error);
        showMessage('Failed to request employee.', 'error');
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
