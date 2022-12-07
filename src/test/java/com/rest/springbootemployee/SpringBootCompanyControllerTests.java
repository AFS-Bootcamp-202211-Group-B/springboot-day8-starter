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

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootCompanyControllerTests {

    @Autowired
    MockMvc client;
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void clean_repos(){
        companyRepository.clearAll();}


    @Test
    void should_get_all_company_when_perform() throws Exception{
        companyRepository.create(new Company(100, "spring", new ArrayList<>()));


        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees",hasSize(0)));


    }


    @Test
    void should_get_company_by_id_when_perform() throws Exception{
        Company spring = companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));


        client.perform(MockMvcRequestBuilders.get("/companies/{id}",spring.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees",hasSize(0)));

    }


    @Test
    void should_get_employees_by_company_id_when_perform() throws Exception{
        Company spring = companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        employeeRepository.create(new Employee(spring.getId(),"Kam",23,"male",9999));
        employeeRepository.create(new Employee(spring.getId(),"Pang",24,"female",12312412));
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));


        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees",spring.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(0)));

    }


    @Test
    void should_get_all_company_by_page_when_perform() throws Exception{

        Company spring = companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));



        client.perform(MockMvcRequestBuilders.get("/companies?page=1&pageSize=2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", containsInAnyOrder(spring.getId(), summer.getId())));


    }

    @Test
    void should_post_company_by_given_company() throws Exception{
        Company OOcL = companyRepository.create(new Company(100, "OOCL", new ArrayList<>()));



        client.perform(MockMvcRequestBuilders.post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(OOcL)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("OOCL"));


        client.perform(MockMvcRequestBuilders.get("/companies/{id}",OOcL.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("OOCL"));
    }
    @Test
    void should_return_changed_company_when_perform_put_given_company_id() throws Exception {
        //given
        Company spring = companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        //when
        //then
        client.perform(MockMvcRequestBuilders.put("/companies/{id}",spring.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(spring)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spring"));

    }

    @Test
    void should_delete_company_by_given_id_company_when_perform() throws Exception{
        Company spring = companyRepository.create(new Company(100, "spring", new ArrayList<>()));


        client.perform(MockMvcRequestBuilders.delete("/companies/{id}",spring.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").doesNotExist());



        client.perform(MockMvcRequestBuilders.get("/companies"))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(0)));
    }

    @Test
    void should_return_notfound_when_perform_get_given_company_and_falseId() throws Exception {
        //given
        Company spring = companyRepository.create(new Company(100, "spring", new ArrayList<>()));
        companyRepository.delete(spring.getId());
        //when
        //then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}",spring.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
