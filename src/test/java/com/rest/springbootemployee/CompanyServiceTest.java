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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;


    @Test
    void should_return_all_companies_when_find_all_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));
        given(companyRepository.findAll())
                .willReturn(companies);
        //when
        List<Company> actual = companyService.findAll();
        //then
        assertEquals(companies, actual);
    }

    @Test
    void should_return_a_company_when_get_company_given_company_id() {
        //given
        List<Company> companies = new ArrayList<>();
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));



        when(companyRepository.findById(101)).thenReturn(summer); //stub

        //when
        Company result= companyService.findById(101);

        //then
        assertEquals(summer, result);
        verify(companyRepository).findById(101);//spy
    }

    @Test
    void should_return_employees_when_get_company_employee_given_company_id() {
        //given
        List<Employee> employeesOfCompany1 = new ArrayList<>();
        Employee employee1 = new Employee(1,"Susan",22,"Female",10000);
        Employee employee2 = new Employee(1,"Boa",22,"Male",10000);

        when(companyRepository.getEmployees(1)).thenReturn(employeesOfCompany1); //stub

        //when
        List<Employee> result= companyService.getEmployees(1);

        //then
        assertEquals(employeesOfCompany1, result);
        verify(companyRepository).getEmployees(1);//spy
    }

    @Test
    void should_return_company_by_page_and_pageSize_when_get_by_page_and_pageSize_given_company(){
        //given
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));
        Company spring = companyRepository.create(new Company(102, "spring", new ArrayList<>()));
        List<Company> company = new ArrayList<>();
        company.add(summer);
        company.add(spring);

        when(companyRepository.findByPage(1,2)).thenReturn(company); //stub

        //when
        List<Company> result= companyService.findByPage(1,2);

        //then
        assertEquals(company, result);
        verify(companyRepository).findByPage(1,2);//spy
    }

    @Test
    void should_return_company_when_create_given_company(){
        //given
        Company summer = companyRepository.create(new Company(101, "summer", new ArrayList<>()));

        when(companyRepository.create(summer)).thenReturn(summer); //stub

        //when


        //then
        Company result = companyService.create(summer);
        assertEquals(summer, result);
    }

    @Test
    void should_update_name_when_update_given_companies(){
        //given
        Company summer = new Company(1, "summer", new ArrayList<>());
        when(companyRepository.findById(1)).thenReturn(summer);
        Company spring = new Company(1, "spring", new ArrayList<>());

        //when
        Company result = companyService.update(1,spring);


        //then
        verify(companyRepository).findById(1);
        assertEquals("spring",result.getName());

    }

    @Test
    void should_return_null_and_delete_when_delete_by_gender_given_employees(){
        //given
        Company summer = new Company(101, "summer", new ArrayList<>());

        //when
        Company result = companyService.delete(1);

        //then
        assertNull(result);
        verify(companyRepository).delete(1);//spy
    }
}
