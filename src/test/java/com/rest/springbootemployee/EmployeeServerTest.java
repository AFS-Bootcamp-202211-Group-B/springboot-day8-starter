package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServerTest {

    //SUT -> Service, DOC -> repository(mocked)
    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;


    // 1. verify interaction
    // when EmployeeService.findAll is called, it will call employeeRepository.findAll()
    //  2. verify data
    //return the data get from employeeRepository.findAll() without any change.
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
        // 1. verify data

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(employee));
        //assertEquals(employees, result);
        // 2. verify interaction
        verify(employeeRepository).findAll();  // spy
    }

    @Test
    void should_update_only_age_and_salary_when_update_all_give_employees() {
        //given
        //List<Employee> employees = new ArrayList<>();
        int employeeId = 1;
        Employee employee = new Employee(employeeId, "Susan", 22, "Female", 10000);
       // employees.add(employee);
        Employee toUpdateEmployee = new Employee(employeeId, "Tom", 23, "Male", 12000);
        when(employeeRepository.findById(employeeId)).thenReturn(employee); //stub

        //when
        Employee updatedEmployee = employeeService.update(employeeId, toUpdateEmployee);

        //then
        verify(employeeRepository).findById(employeeId);  // spy
        assertThat(updatedEmployee.getAge(),equalTo(23));
        assertThat(updatedEmployee.getSalary(),equalTo(12000));
//        assertEquals(updatedEmployee.getAge(), 23);
//        assertEquals(updatedEmployee.getSalary(), 12000);
    }

    @Test
    void should_return_employee_when_find_by_id_given_employee_id() {
        //given
        Employee employee = new Employee(10, "Bobby", 25, "Male", 8000);

        when(employeeRepository.findById(employee.getId())).thenReturn(employee); //stub

        //when
        Employee result = employeeService.findById(employee.getId());


        //then
        // 1. verify data
        assertThat(result.getName(),equalTo("Bobby"));
        assertThat(result.getAge(),equalTo(25));
        assertThat(result.getGender(),equalTo("Male"));
        assertThat(result.getSalary(),equalTo(8000));
        // 2. verify interaction
        verify(employeeRepository).findById(employee.getId());  // spy
    }



}
