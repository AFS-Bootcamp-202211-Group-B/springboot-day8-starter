package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employees_when_find_all_given_employees() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);

        when(employeeRepository.findAll()).thenReturn(employees); //stub
        //when
        List<Employee> result = employeeService.findAll();
        //then
        //1. verify data
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(employee));
        assertEquals(employees, result);
        //2.verify interaction
        verify(employeeRepository).findAll(); //spy
    }
    @Test
    void should_update_only_age_and_salary_when_update_all_given_employees() {
        //given
        int employeeId = 1;
        Employee employee = new Employee(employeeId, "Susan", 22, "Female", 100000);
        Employee toUpdateEmployee = new Employee(employeeId, "Tom", 23, "Male", 12000);
        //when
        when(employeeRepository.findById(employeeId)).thenReturn(employee);
        Employee updatedEmployee = employeeService.update(employeeId, toUpdateEmployee);
        //then
        verify(employeeRepository).findById(employeeId); //spy
        //verify data
        assertThat(updatedEmployee.getAge(), equalTo(23));
        assertThat(updatedEmployee.getSalary(), equalTo(12000));
        assertThat(updatedEmployee.getName(), equalTo("Susan"));
        assertThat(updatedEmployee.getGender(), equalTo("Female"));
    }

    @Test
    void should_find_employee_by_id_when_find_given_employees() {
        //given
        int employeeId = 2;
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee(10, "Susan", 22, "Female", 10000);
        Employee employee2 = new Employee(11, "Tom", 23, "Male", 12000);
        employees.add(employee1);
        employees.add(employee2);

        when(employeeRepository.findById(employeeId)).thenReturn(employee2); //stub
        //when
        Employee resultEmployee = employeeService.findById(employeeId);
        //then
        verify(employeeRepository).findById(employeeId); //spy
        //1. verify data
        assertThat(resultEmployee.getAge(), equalTo(23));
        assertThat(resultEmployee.getSalary(), equalTo(12000));
        assertThat(resultEmployee.getName(), equalTo("Tom"));
        assertThat(resultEmployee.getGender(), equalTo("Male"));

    }

}
