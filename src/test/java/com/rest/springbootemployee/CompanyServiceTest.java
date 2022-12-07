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
    void should_get_by_id_companies_when_find_all_given_companies() {
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
}
