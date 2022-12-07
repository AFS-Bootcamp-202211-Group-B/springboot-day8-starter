package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(Integer id) {
        return companyRepository.findById(id);
    }

    public List<Employee> getEmployees(Integer id) {
        return companyRepository.getEmployees(id);
    }
}
