package com.rest.springbootemployee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    MockMvc client;

    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    void cleanRepository(){
        companyRepository.clearAll();
    }

    @Test
    void should_get_all_companies_when_perform_get_given_companies() throws Exception{
        //given
        companyRepository.create(new Company(100, "spring", new ArrayList<>()));

        //when
        client.perform(MockMvcRequestBuilders.get("/companies"))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees",hasSize(0)));

    }

    @Test
    void should_get_companies_by_id_when_perform_get_by_id_given_companies() throws Exception{
        //given
        Company test1=companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        Company test2=companyRepository.create(new Company(101, "spring", new ArrayList<>()));
        //when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}",test1.getId()))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees",hasSize(0)));
    }
    @Test
    void should_get_employees_when_perform_get_employees_by_id_given_companies_and_employees() throws Exception{
        //given
        ArrayList<Employee> employeesOfCompany1 = new ArrayList<>();
        employeesOfCompany1.add(new Employee(1, "Carlos", 26, "Male", 70000));
        employeesOfCompany1.add(new Employee(2, "Nicole", 22, "Female", 80000));
        Company test1=companyRepository.create(new Company(100, "spring", employeesOfCompany1));

        //when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees",test1.getId()))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Carlos"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(26))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(70000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Nicole"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].salary").value(80000));

    }

    @Test
    void should_get_companies_by_page_when_perform_get_by_page_given_companies() throws Exception{
        //given
        Company test1=companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        Company test2=companyRepository.create(new Company(101, "spring2", new ArrayList<>()));
        Company test3=companyRepository.create(new Company(102, "spring3", new ArrayList<>()));

        //when
        client.perform(MockMvcRequestBuilders.get("/companies?page=1&pageSize=2"))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id",containsInAnyOrder(test1.getId(),test2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name",containsInAnyOrder(test1.getName(),test2.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].employees",containsInAnyOrder(test1.getEmployees(),test2.getEmployees())));

    }
}
