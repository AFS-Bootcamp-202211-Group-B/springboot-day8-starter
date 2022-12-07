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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CompanyServiceTest {
    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_get_all_companies_when_find_all_given_companies() {
        // given
        List<Company> companies = new ArrayList<>();
        Company company = new Company(1, "spring", null);
        companies.add(company);
        when(companyRepository.findAll()).thenReturn(companies);

        // when
        List<Company> returnedCompanies = companyService.findAll();

        // then
        assertThat(returnedCompanies, hasSize(1));
        assertEquals(company, returnedCompanies.get(0));
        verify(companyRepository).findAll();
    }

    @Test
    void should_get_company_by_id_when_find_all_given_companies() {
        // given
        List<Company> companies = new ArrayList<>();
        Company company1 = new Company(1, "one", null);
        Company company2 = new Company(2, "two", null);
        companies.add(company1);
        companies.add(company2);
        when(companyRepository.findById(1)).thenReturn(company1);

        // when
        Company returnedCompany = companyService.findById(1);

        // then
        assertEquals(company1, returnedCompany);
        verify(companyRepository).findById(1);
    }

    @Test
    void should_get_employees_when_find_employees_given_companies() {
        // given
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee(10,"one",22,"female",10000);
        Employee employee2 = new Employee(10,"two",21,"male",11000);
        employees.add(employee1);
        employees.add(employee2);
        List<Company> companies = new ArrayList<>();
        Company company1 = new Company(1, "one", employees);
        Company company2 = new Company(2, "two", null);
        companies.add(company1);
        companies.add(company2);
        when(companyRepository.getEmployees(1)).thenReturn(employees);

        // when
        List<Employee> resultList = companyService.getEmployees(1);

        // then
        assertEquals(employees,resultList);
        verify(companyRepository).getEmployees(1);
    }

    @Test
    void should_get_companies_by_page_when_find_by_page_given_companies() {
        // given
        Company company1 = new Company(1, "one", null);
        Company company2 = new Company(2, "two", null);
        Company company3 = new Company(3, "three", null);
        Company company4 = new Company(4, "four", null);
        companyRepository.create(company1);
        companyRepository.create(company2);
        companyRepository.create(company3);
        companyRepository.create(company4);
        List<Company> companies = new ArrayList<>();
        companies.add(company3);
        companies.add(company4);
        when(companyRepository.findByPage(2,2)).thenReturn(companies);

        // when
        List<Company> resultList = companyService.findByPage(2,2);

        // then
        assertThat(resultList,hasSize(2));
        assertEquals(companies,resultList);
        verify(companyRepository).findByPage(2,2);
    }
}
