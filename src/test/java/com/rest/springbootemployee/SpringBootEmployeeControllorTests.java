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

@SpringBootTest
@AutoConfigureMockMvc

public class SpringBootEmployeeControllorTests {


    @Autowired
    MockMvc client;
    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void clean_repos(){
        employeeRepository.clearAll();}

    @Test
    void should_get_all_employees_when_perform() throws Exception{
        employeeRepository.create(new Employee(10,"Susan",22,"F",1000));


        client.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("F"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(1000));
    }

    @Test
    void should_get_employees_by_id_when_perform() throws Exception{
        Employee susan = employeeRepository.create(new Employee(10,"Susan",22,"F",1000));
        employeeRepository.create(new Employee(11,"Bob",22,"M",10));


        client.perform(MockMvcRequestBuilders.get("/employees/{id}",susan.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("F"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(1000));


    }



    @Test
    void should_get_employees_by_gender_salary_when_perform() throws Exception{
        Employee susan = employeeRepository.create(new Employee(10,"Susan",22,"F",1000));
        Employee bob = employeeRepository.create(new Employee(11,"Bob",22,"M",10));


        client.perform(MockMvcRequestBuilders.get("/employees?gender=F"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))

                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age").value(22))

                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary").value(1000));

    }

    @Test
    void should_get_employees_by_page_when_perform() throws Exception{
        Employee susan = employeeRepository.create(new Employee(10,"Susan",22,"F",1000));
        Employee bob = employeeRepository.create(new Employee(11,"Bob",22,"M",10));
        Employee leo = employeeRepository.create(new Employee(11,"Leo",22,"M",11));
        Employee bo = employeeRepository.create(new Employee(11,"Bo",22,"M",12));
        Employee b = employeeRepository.create(new Employee(11,"B",22,"M",13));
        Employee boob = employeeRepository.create(new Employee(11,"Boob",22,"M",14));



        client.perform(MockMvcRequestBuilders.get("/employees?page=1&pageSize=2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("F"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value("M"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].salary").value(10));

    }

    //test put employee
    @Test
    void should_update_employee_when_perform_put_given_employees() throws Exception{
        //given
        Employee bob=employeeRepository.create(new Employee(10,"bob",22,"Male",10000));
        //when
        client.perform(MockMvcRequestBuilders.put("/employees/{id}",bob.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bob)))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
        client.perform(MockMvcRequestBuilders.get("/employees/{id}",bob.getId()))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));

    }

    @Test
    void should_post_employees_by_gender_salary_when_perform() throws Exception{
        Employee susan = employeeRepository.create(new Employee(10,"Susan",22,"F",1000));
        Employee bob = employeeRepository.create(new Employee(11,"Bob",22,"M",10));


        client.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bob)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("M"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10));

        client.perform(MockMvcRequestBuilders.get("/employees/{id}",bob.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("M"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10));



    }

    @Test
    void should_delete_employees_by_gender_salary_when_perform() throws Exception{
        Employee susan = employeeRepository.create(new Employee(10,"Susan",22,"F",1000));


        client.perform(MockMvcRequestBuilders.delete("/employees/{id}",susan.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").doesNotExist());


        client.perform(MockMvcRequestBuilders.get("/employees"))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(0)));
    }
}
