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

    @Test
    void should_return_company_by_id_when_get_by_id_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);
        Company company = new Company(1,"abc company",employees);
        companies.add(company);
        when(companyRepository.findById(company.getId())).thenReturn(company); //stub
        //when
        Company foundCompany = companyService.findById(company.getId());
        //then
        assertThat(foundCompany,equalTo(company));
        verify(companyRepository).findById(company.getId());
        //then

    }

    @Test
    void should_employees_when_get_by_company_id_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);
        Company company = new Company(1,"abc company",employees);
        companies.add(company);
        when(companyRepository.getEmployees(company.getId())).thenReturn(employees); //stub
        List<Employee> foundEmployees = companyService.getEmployees(company.getId());
        //when
        assertThat(foundEmployees,equalTo(employees));

        //then
        verify(companyRepository).getEmployees(company.getId());
    }
    @Test
    void should_return_by_page_when_get_by_page_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);
        Company company = new Company(1,"abc company",employees);
        Company defCompany = new Company(2,"def company",employees);
        companies.add(company);
        companies.add(defCompany);
        int page = 1;
        int pageSize = 2;
        when(companyRepository.findByPage(page,pageSize)).thenReturn(companies); //stub
        List<Company> foundCompanies = companyService.findByPage(page,pageSize);
        //when
        assertThat(foundCompanies,equalTo(companies));

        //then
        verify(companyRepository).findByPage(page,pageSize);
    }

    @Test
    void should_return_new_company_when_create_given_new_company() {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);
        Company company = new Company(1,"abc company",employees);
        //when
        when(companyRepository.create(company)).thenReturn(company); //stub
        //then
        Company addedCompany = companyService.create(company);
        assertThat(addedCompany,equalTo(company));

        verify(companyRepository).create(company);
    }

    @Test
    void should_update_only_age_and_salary_when_update_all_give_employees() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        Company company = new Company(1,"def company",employees);
        employees.add(employee);

        Company toUpdateCompany = new Company(1,"abc company",employees);


        when(companyRepository.findById(company.getId())).thenReturn(toUpdateCompany); //stub

        //when
        Company updatedCompany = companyService.update(company.getId(), toUpdateCompany);
        //then
        verify(companyRepository).findById(company.getId());  // spy
        assertThat(updatedCompany.getName(),equalTo("abc company"));

    }
}
