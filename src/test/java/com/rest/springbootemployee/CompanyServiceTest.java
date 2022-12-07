package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {
    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_return_all_company_when_find_all_given_companies(){
        //given
        List<Company> companies = new ArrayList<>();
        Company company = new Company(1,"web",new ArrayList<>());
        companies.add(company);

        when(companyRepository.findAll()).thenReturn(companies); //stub

        //when
        List<Company> result= companyService.findAll();

        //then
        assertEquals(companies, result);
        verify(companyRepository).findAll();//spy
    }
    @Test
    void should_return_companies_by_id_when_find_by_id_given_companies(){
        //given
        int companyId = 1;
        Company company = new Company(companyId,"web",new ArrayList<>());

        when(companyRepository.findById(companyId)).thenReturn(company); //stub

        //when
        Company result= companyService.findById(companyId);

        //then
        assertEquals(company, result);
        verify(companyRepository).findById(companyId);//spy
    }

    @Test
    void should_return_employee_by_company_id_when_get_employees_given_companies(){
        //given
        int companyId = 1;
        List<Employee> employeesOfCompany1 = new ArrayList<>();
        employeesOfCompany1.add(new Employee(1, "Carlos", 26, "Male", 70000));
        employeesOfCompany1.add(new Employee(2, "Nicole", 22, "Female", 80000));

        when(companyRepository.getEmployees(companyId)).thenReturn(employeesOfCompany1); //stub

        //when
        List<Employee> result= companyService.getEmployees(companyId);

        //then
        assertEquals(employeesOfCompany1, result);
        verify(companyRepository).getEmployees(companyId);//spy
    }
}
