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
}
