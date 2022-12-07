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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
public class CompanyServerTest {
    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_return_all_companies_when_find_all_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);
        Company company = new Company(1,"abc company",employees);
        companies.add(company);
        when(companyRepository.findAll()).thenReturn(companies); //stub

        //when
        List<Company> result = companyService.findAll();

        //then
        // 1. verify data

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(company));
        //assertEquals(employees, result);
        // 2. verify interaction
        verify(companyRepository).findAll();  // spy
    }
}
