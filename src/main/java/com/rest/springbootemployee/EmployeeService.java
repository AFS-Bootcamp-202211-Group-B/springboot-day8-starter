package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeService { //SUT

    private EmployeeRepository employeeRepository; // DOC

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    // 1. verify interaction
    // when EmployeeService.findAll is called, it will call employeeRepository.findAll()
    //  2. verify data
    //return the data get from employeeRepository.findAll() without any change.
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    // 1. verify interaction
    // when EmployeeService.update is called, it will call employeeRepository.findById(id)
    //  2. verify data
    // when input an employee, only the age and salary will be changed, name and gender will not change.
    public Employee findById(Integer id){
        return employeeRepository.findById(id);
    }


    public Employee update(Integer id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id);
        if (employee.getAge() != null) {
            existingEmployee.setAge(employee.getAge());
        }
        if (employee.getSalary() != null) {
            existingEmployee.setSalary(employee.getSalary());
        }
        return existingEmployee;
    }


    public List<Employee> findByGender(String gender) {
        return employeeRepository.findByGender(gender);
    }

    public Employee create(Employee employee) {
        return employeeRepository.create(employee);
    }

    public void delete(Integer id) {
        employeeRepository.delete(id);
    }

    public List<Employee> findByPage(int page, int pageSize) {
        return employeeRepository.findByPage(page, pageSize);
    }
}
