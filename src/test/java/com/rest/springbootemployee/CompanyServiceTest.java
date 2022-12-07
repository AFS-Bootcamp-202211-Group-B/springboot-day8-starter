package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Test
    void should_return_company_by_page_and_pageSize_when_get_by_page_and_pageSize_given_companies(){
        //given
        Company company1 = new Company(1,"company1",new ArrayList<>());
        Company company2 = new Company(2,"company2",new ArrayList<>());
        List<Company> companies = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);
        int page=1;
        int pageSize=2;

        when(companyRepository.findByPage(page,pageSize)).thenReturn(companies); //stub

        //when
        List<Company> result= companyService.findByPage(page,pageSize);

        //then
        assertEquals(companies, result);
        verify(companyRepository).findByPage(page,pageSize);//spy
    }


    @Test
    void should_return_new_company_when_create_given_companies(){
        //given
        List<Employee> employeesOfCompany1 = new ArrayList<>();
        employeesOfCompany1.add(new Employee(1, "Carlos", 26, "Male", 70000));
        employeesOfCompany1.add(new Employee(2, "Nicole", 22, "Female", 80000));
        Company company1 = new Company(1,"company1",employeesOfCompany1);

        when(companyRepository.create(company1)).thenReturn(company1); //stub

        //when
        Company result= companyService.create(company1);

        //then
        assertEquals(company1, result);
        verify(companyRepository).create(company1);//spy
    }

    @Test
    void should_update_name_when_update_given_companies(){
        //given
        int companyId= 1;
        List<Employee> employeesOfCompany1 = new ArrayList<>();
        employeesOfCompany1.add(new Employee(1, "Carlos", 26, "Male", 70000));
        employeesOfCompany1.add(new Employee(2, "Nicole", 22, "Female", 80000));
        Company company = new Company(1,"oldCompanyName",employeesOfCompany1);
        when(companyRepository.findById(companyId)).thenReturn(company);
        Company updatedCompany = new Company(1,"newCompanyName",new ArrayList<>());

        //when
        Company result = companyService.update(companyId,updatedCompany);


        //then
        verify(companyRepository).findById(companyId);
        assertEquals("newCompanyName",result.getName());
        assertEquals(employeesOfCompany1,result.getEmployees());
    }
}
