package com.rest.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    MockMvc client;
    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void cleanRepository() {
        employeeRepository.cleanAll();
    }
    @Test
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
        //given
        employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Bob", 23, "Male", 10000));
        //when & then
        client.perform(MockMvcRequestBuilders.get("/employees"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));
    }

    @Test
    void should_get_employees_by_id_when_perform_get_by_id_given_employees() throws Exception {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(10, "Bob", 23, "Male", 9000));

        //when
        client.perform(MockMvcRequestBuilders.get("/employees/{id}", susan.getId()))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response date
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));



        //then
    }

    @Test
    void should_get_employees_by_gender_when_perform_get_by_gender_given_employees() throws Exception {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        //employeeRepository.create(new Employee(10, "Bob", 23, "Male", 9000));

        //when
        client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", "Female"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response date
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));

        //then
    }

    @Test
    void should_get_employees_by_page_when_perform_get_by_page_given_employees() throws Exception {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        //employeeRepository.create(new Employee(10, "Bob", 23, "Male", 9000));

        //when
        client.perform(MockMvcRequestBuilders.get("/employees?page={page}&pageSize={pageSize}", 1,1))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response date
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));

        //then
    }

    @Test
    void should_create_add_new_employee_when_perform_post_given_new_employees() throws Exception {
        //given
        Employee newEmployee = new Employee(1, "Jin", 22, "Male", 10000);
        //when
        String newEmployeeJson = new ObjectMapper().writeValueAsString(newEmployee);
        //then
        client.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newEmployeeJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees,hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getName(),equalTo("Jin"));
        assertEquals("Jin",employee.getName());
        assertThat(employee.getAge(), equalTo(22));
        assertThat(employee.getSalary(),equalTo(10000));
        assertThat(employee.getGender(),equalTo("Male"));

    }

}