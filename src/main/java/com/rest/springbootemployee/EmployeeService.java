package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }
    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }

    public Employee update(Integer employeeId, Employee toUpdateEmployee) {
        Employee existingEmployee = employeeRepository.findById(employeeId);
        if (toUpdateEmployee.getAge() != null) {
            existingEmployee.setAge(toUpdateEmployee.getAge());
        }
        if (toUpdateEmployee.getSalary() != null) {
            existingEmployee.setSalary(toUpdateEmployee.getSalary());
        }
        return existingEmployee;
    }
}
