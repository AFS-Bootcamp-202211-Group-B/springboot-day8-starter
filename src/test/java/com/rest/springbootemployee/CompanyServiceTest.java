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
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void should_get_companies_by_page_query_when_get_given_companies_and_page_and_page_size() {
        // given
        List<Company> companies = new ArrayList<>();
        Company company1 = new Company(1, "one", null);
        Company company2 = new Company(2, "two", null);
        companies.add(company1);
        companies.add(company2);

        int page = 1;
        int pageSize = 2;
        when(companyRepository.findByPage(page, pageSize)).thenReturn(companies);
        // when
        List<Company> returnedCompanies = companyService.findByPage(page, pageSize);

        // then
        assertEquals(2, returnedCompanies.size());
        assertEquals(1, returnedCompanies.get(0).getId());
        assertEquals(2, returnedCompanies.get(1).getId());
        assertEquals("one", returnedCompanies.get(0).getName());
        assertEquals("two", returnedCompanies.get(1).getName());
        verify(companyRepository).findByPage(page, pageSize);
    }
    @Test
    void should_add_company_when_post_given_company() {
        // given
        Company company = new Company(1, "one", null);
        when(companyRepository.create(company)).thenReturn(company);
        // when
        Company postedCompany = companyService.create(company);

        // then
        assertEquals(1, postedCompany.getId());
        assertEquals("one", postedCompany.getName());
        assertNull(postedCompany.getEmployees());
        verify(companyRepository).create(company);
    }
    @Test
    void should_update_company_name_when_put_given_company_and_id() {
        // given
        int companyId = 1;
        Company company = new Company(companyId, "one", null);
        Company toUpdateCompany = new Company(companyId, "two", null);
        when(companyRepository.findById(companyId)).thenReturn(company);
        // when
        Company updatedCompany = companyService.update(companyId, toUpdateCompany);

        // then
        assertEquals(companyId, updatedCompany.getId());
        assertEquals("two", updatedCompany.getName());
        assertNull(updatedCompany.getEmployees());
        verify(companyRepository).findById(companyId);
    }
    @Test
    void should_delete_company_when_delete_given_company_id() {
        // given
        Company company1 = new Company(1, "one", null);

        when(companyRepository.findById(1)).thenReturn(company1);
        // when
        companyService.delete(1);
        List<Company> actualCompanyList = companyService.findAll();

        // then
        assertThat(actualCompanyList, hasSize(0));
        verify(companyRepository).delete(1);
    }

}
