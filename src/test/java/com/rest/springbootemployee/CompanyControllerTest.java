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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    void should_get_all_companies_when_perform_get_given_companies() throws Exception {
        // given
        Company company = new Company(1, "spring", null);
        companyRepository.create(company);

        // when & then
        client.perform(MockMvcRequestBuilders.get("/companies"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("spring"));

    }

    @Test
    void should_get_company_by_id_when_perform_get_given_companies() throws Exception {
        // given
        Company company1 = new Company(1, "one", null);
        Company company2 = new Company(2, "two", null);
        companyRepository.create(company1);
        companyRepository.create(company2);

        // when & then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}",1))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("one"));

    }
    @Test
    void should_get_companies_when_get_company_by_page_given_companies() throws Exception {
        //given
        companyRepository.create(new Company(1, "one", null));
        companyRepository.create(new Company(2, "two", null));
        companyRepository.create(new Company(3, "three", null));
        companyRepository.create(new Company(4, "four", null));

        //when & then
        client.perform(MockMvcRequestBuilders.get("/companies?page={page}&pageSize={pageSize}", 1, 2))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("one"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("two"));
    }
    @Test
    void should_add_new_company_when_post_given_company() throws Exception {
        //given
        Company company = new Company(1, "one", null);
        ObjectMapper objectMapper = new ObjectMapper();

        //when & then
        client.perform(MockMvcRequestBuilders.post("/companies")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(company)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("one"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.employees").doesNotExist());

        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company addedCompany= companies.get(0);
        assertThat(addedCompany.getName(), equalTo("one"));
        assertNull(addedCompany.getEmployees());
    }
    @Test
    void should_update_company_when_put_given_company_and_id() throws Exception {
        //given
        Company company1 = new Company(1, "one", null);
        companyRepository.create(company1);
        Company company2 = new Company(2, "two", null);
        ObjectMapper objectMapper = new ObjectMapper();

        client.perform(MockMvcRequestBuilders.put("/companies/{id}", company1.getId())
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(company2)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("two"));

        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company updatedCompany= companies.get(0);
        assertThat(updatedCompany.getName(), equalTo("two"));
        assertNull(updatedCompany.getEmployees());

    }
    @Test
    void should_delete_company_when_delete_given_company_and_id() throws Exception {
        //given
        Company company = companyRepository.create(new Company(1, "one", null));

        //then
        client.perform(MockMvcRequestBuilders.delete("/companies/{id}", company.getId()))
            // 1. assert response status
            .andExpect(MockMvcResultMatchers.status().isNoContent());
        List<Company> companies = companyRepository.findAll();
        assertThat(companies,hasSize(0));
    }

}
