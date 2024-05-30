package com.assignment.employeeManagement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import com.assignment.employeeManagement.entity.Employee;
import com.assignment.employeeManagement.entity.Manager;
import com.assignment.employeeManagement.entity.Request;
import com.assignment.employeeManagement.entity.User;
import com.assignment.employeeManagement.repository.EmployeeRepository;
import com.assignment.employeeManagement.repository.ManagerRepository;
import com.assignment.employeeManagement.repository.ProjectRepository;
import com.assignment.employeeManagement.repository.RequestRepository;
import com.assignment.employeeManagement.repository.UserLoginRepo;
import com.assignment.employeeManagement.service.ManagerServiceIMPL;

public class ManagerServiceTest {
	@Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserLoginRepo userRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ManagerServiceIMPL managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employeeList = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employeeList);

        List<Employee> result = managerService.getAllEmployees();

        assertEquals(employeeList, result);
    }

    @Test
    void testFilterEmployeesBySkills() {
        List<String> skills = Arrays.asList("Java", "Spring");
        List<Employee> employeeList = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findBySkillsIn(skills)).thenReturn(employeeList);

        List<Employee> result = managerService.filterEmployeesBySkills(skills);

        assertEquals(employeeList, result);
    }

    @Test
    void testRequestEmployeesForProject() {
        String email = "manager@example.com";
        Long projectId = 1L;
        List<Long> employeeIds = Arrays.asList(1L, 2L);
        Manager manager = new Manager();
        Request request = new Request();

        when(userRepository.findByUserEmail(email)).thenReturn(new User());
        when(managerRepository.findByUser(any(User.class))).thenReturn(manager);
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        Request result = managerService.requestEmployeesForProject(email, projectId, employeeIds);

        verify(requestRepository, times(1)).save(any(Request.class));
    }
}
