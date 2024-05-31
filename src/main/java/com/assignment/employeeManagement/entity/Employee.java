package com.assignment.employeeManagement.entity;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
	 	
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "employee_id")
	    private Long employeeId;

	    @OneToOne
	    @JoinColumn(name = "user_id",  nullable = false)
	     private User user;

	    @Column(name = "full_name", nullable = false	)
	    private String fullName;

	    @ManyToOne
	    @JoinColumn(name = "project_id", nullable = true)
	    private Project project;

	    @ManyToOne
	    @JoinColumn(name = "manager_id", nullable = true)
	    private Manager manager;
	    
	    @ElementCollection
	    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "employee_id"))
	    @Column(name = "skill")
	    private Set<String> skills;

		public Employee(Long employeeId, User user, String fullName, Project project, Manager manager,
				Set<String> skills) {
			super();
			this.employeeId = employeeId;
			this.user = user;
			this.fullName = fullName;
			this.project = project;
			this.manager = manager;
			this.skills = skills;
		}

		public Employee() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Long getEmployeeId() {
			return employeeId;
		}

		public void setEmployeeId(Long employeeId) {
			this.employeeId = employeeId;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public Project getProject() {
			return project;
		}

		public void setProject(Project project) {
			this.project = project;
		}

		public Manager getManager() {
			return manager;
		}

		public void setManager(Manager manager) {
			this.manager = manager;
		}

		public Set<String> getSkills() {
			return skills;
		}

		public void setSkills(Set<String> skills) {
			this.skills = skills;
		}

		@Override
		public String toString() {
			return "Employee [employeeId=" + employeeId + ", user=" + user + ", fullName=" + fullName + ", project="
					+ project + ", manager=" + manager + ", skills=" + skills + "]";
		}

		
		

		
		
	    
	    
}
