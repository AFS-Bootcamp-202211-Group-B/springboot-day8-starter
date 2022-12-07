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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    //SUT -> service , DOC -> repository(mocked)
    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_update_only_return_all_employees_when_find_all_given_em()
    {
        //List<Employee> employees = new ArrayList<>();
        int employeesId = 1;
        Employee employee = new Employee(10,"Susan",22,"Female",10000);
        //employees.add(employee);

        Employee employee2 = new Employee(10,"Tom",23,"Male",10001);


        when(employeeRepository.findById(employeesId)).thenReturn(employee); //stub

        Employee updatedEmployee = employeeService.update(employeesId,employee2);

        //1.verify data
        assertThat(updatedEmployee.getName(),equalTo("Susan"));;
        assertThat(updatedEmployee.getGender(),equalTo("Female"));;
        assertThat(updatedEmployee.getAge(),equalTo(23));
        assertThat(updatedEmployee.getSalary(),equalTo(10001));;

        verify(employeeRepository).findById(employeesId);
    }
}

//verify interaction
//when EmployeeServce findAll is called ,it will can employeeRepository.findAll()
//verify data
//return the data get from e.findall without change