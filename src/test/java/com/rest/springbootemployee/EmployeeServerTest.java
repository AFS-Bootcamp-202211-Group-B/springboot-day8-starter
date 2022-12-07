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
    void should_return_by_id_when_find_by_id_given_employees() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);
        when(employeeRepository.findById(employee.getId())).thenReturn(employee); //stub
        //when
        Employee foundEmployee = employeeService.findById(employee.getId());
        //then
        assertThat(foundEmployee,equalTo(employee));
        verify(employeeRepository).findById(employee.getId());
    }

    @Test
    void should_return_by_gender_when_find_by_gender_given_employees() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);

        when(employeeRepository.findByGender(employee.getGender())).thenReturn(employees); //stub
        //when
        List<Employee> foundEmployees = employeeService.findByGender(employee.getGender());
        //then
        assertThat(foundEmployees,equalTo(employees));
        verify(employeeRepository).findByGender(employee.getGender());

    }

    @Test
    void should_return_new_employee_when_create_given_new_employee() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        //when
        when(employeeRepository.create(employee)).thenReturn(employee); //stub
        //then
        Employee addedEmployee = employeeService.create(employee);
        assertThat(addedEmployee,equalTo(employee));

        verify(employeeRepository).create(employee);
    }

    @Test
    void should_delete_employee_when_delete_given_delete_employee() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);
        //when

        //then
        List<Employee> resultEmployee = employeeService.findAll();
        employeeService.delete(employee.getId());
        assertThat(resultEmployee, hasSize(0));
        verify(employeeRepository).delete(employee.getId());
    }

    @Test
    void should_return_page_of_employees_when_find_by_pages_given_page_and_pageSize() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee susan = new Employee(10, "Susan", 22, "Female", 10000);
        Employee bob = new Employee(11, "Bob", 23, "Male", 10000);
        employees.add(susan);
        employees.add(bob);
        //when
        when(employeeRepository.findByPage(1,2)).thenReturn(employees); //stub
        //then
        List<Employee> foundEmployees = employeeService.findByPage(1,2);
        assertThat(foundEmployees,hasSize(2));
        assertThat(foundEmployees,equalTo(employees));

        verify(employeeRepository).findByPage(1,2);

    }

}
