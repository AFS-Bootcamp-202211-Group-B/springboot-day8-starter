package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CompanyServerTest {

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_return_all_companies_when_findAll_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        Company company1 = new Company(1, "Company1", new ArrayList<>());
        Company company2 = new Company(2, "Company2", new ArrayList<>());

        companies.add(company1);
        companies.add(company2);


        when(companyRepository.findAll()).thenReturn(companies);

        //when
        List<Company> result = companyService.findAll();

        //then
        //1. verify data
        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalTo(company1));
        assertThat(result.get(1), equalTo(company2));

        //2. verify interaction
        //spy
        verify(companyRepository).findAll();
    }

    @Test
    void should_return_company_with_id_when_find_by_id_given_companies() {
        //given
        int id = 1;
        Company company1 = new Company(id, "Company1", new ArrayList<>());

        when(companyRepository.findById(id)).thenReturn(company1);

        //when
        Company expectedCompany = companyService.findById(id);

        //then
        //1. verify data age, salary
        assertThat(expectedCompany.getId(), equalTo(id));
        assertThat(expectedCompany.getName(), equalTo("Company1"));

        //2. verify interaction
        //spy
        verify(companyRepository).findById(id);
    }

    @Test
    void should_get_employees_when_get_employees_by_company_id_given_companies_and_id() {
        //given
        ArrayList<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(1, "Susan", 22, "Female", 10000);
        employees.add(employee);
        employees.add(new Employee(2, "gogo", 23, "Female", 3000));

        int id = 1;
        Company company = new Company(1, "Company", employees);

        when(companyRepository.getEmployees(id)).thenReturn(company.getEmployees());

        //when
        List<Employee> returnedEmployees = companyService.getEmployees(id);

        //then
        //1. verify data
        assertThat(returnedEmployees, hasSize(2));
        assertThat(returnedEmployees.get(0).getName(), equalTo("Susan"));
        assertThat(returnedEmployees.get(0).getAge(), equalTo(22));
        assertThat(returnedEmployees.get(0).getGender(), equalTo("Female"));
        assertThat(returnedEmployees.get(0).getSalary(), equalTo(10000));
        assertThat(returnedEmployees.get(1).getName(), equalTo("gogo"));
        assertThat(returnedEmployees.get(1).getAge(), equalTo(23));
        assertThat(returnedEmployees.get(1).getGender(), equalTo("Female"));
        assertThat(returnedEmployees.get(1).getSalary(), equalTo(3000));

        //2. verify interaction
        //spy
        verify(companyRepository).getEmployees(id);
    }

    @Test
    void should_get_companies_when_get_given_companies_pageSize_and_page() {
        //given
        int page = 1;
        int pageSize = 2;
        List<Company> companies = new ArrayList<>();
        Company companyA = new Company(1, "Company1", new ArrayList<>());
        Company companyB = new Company(2, "Company2", new ArrayList<>());
        companies.add(companyA);
        companies.add(companyB);

        when(companyRepository.findByPage(page, pageSize)).thenReturn(companies);

        //when
        List<Company> returnedCompanies = companyService.findByPage(page, pageSize);

        //then
        //1. verify data
        assertThat(returnedCompanies.size(), equalTo(2));
        assertThat(returnedCompanies.get(0).getId(), equalTo(1));
        assertThat(returnedCompanies.get(1).getId(), equalTo(2));
        assertThat(returnedCompanies.get(0).getName(), equalTo("Company1"));
        assertThat(returnedCompanies.get(1).getName(), equalTo("Company2"));

        //2. verify interaction
        //spy
        verify(companyRepository).findByPage(page, pageSize);
    }

    @Test
    void should_get_companies_when_create_given_company() {
        //given
        Company company = new Company(1, "Company1", new ArrayList<>());
        when(companyRepository.create(company)).thenReturn(company);

        //when
        Company createdCompany = companyService.create(company);

        //then
        assertThat(createdCompany.getId(), equalTo(company.getId()));
        assertThat(createdCompany.getName(), equalTo("Company1"));
        assertThat(createdCompany.getEmployees(), empty());

        //spy
        verify(companyRepository).create(company);
    }

    @Test
    void should_get_company_when_update_given_updated_company_and_id() {
        //given
        int id = 1;
        Company company = new Company(1, "Company1", new ArrayList<>());

        when(companyRepository.findById(id)).thenReturn(company);

        //when
        Company companyUpdatedReturned = companyService.update(id, company);

        //then
        //spy
        verify(companyRepository).findById(id);
        assertThat(companyUpdatedReturned.getId(), equalTo(id));
        assertThat(companyUpdatedReturned.getName(), equalTo("Company1"));
        assertThat(companyUpdatedReturned.getEmployees(), empty());
    }


    @Test
    void should_delete_company_when_delete_given_company_id() {
        //given
        List<Company> companies = new ArrayList<>();
        Company companyA = new Company(1, "Company1", new ArrayList<>());
        companies.add(companyA);

        when(companyRepository.findAll()).thenReturn(companies).thenReturn(new ArrayList<>());

        //when
        List<Company> result = companyService.findAll();
        companyService.delete(1);
        List<Company> deletedResult = companyService.findAll();

        //then
        //1. verify data
        assertThat(result, hasSize(1));
        assertThat(deletedResult, hasSize(0));

        //2. verify interaction
        //spy
        verify(companyRepository).delete(1);
    }

}