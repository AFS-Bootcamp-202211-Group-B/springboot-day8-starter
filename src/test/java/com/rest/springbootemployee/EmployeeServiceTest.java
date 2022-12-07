package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    //when EmployeeService.findall -> employeeRepository.findAll()
    //    return  without any change

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employees_when_find_all_given_employees(){
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(1,"Susan",22,"Female",10000);
        employees.add(employee);

        when(employeeRepository.findAll()).thenReturn(employees); //stub

        //when
        List<Employee> result= employeeService.findAll();

        //then
        assertEquals(employees, result);
        verify(employeeRepository).findAll();//spy
    }
}
