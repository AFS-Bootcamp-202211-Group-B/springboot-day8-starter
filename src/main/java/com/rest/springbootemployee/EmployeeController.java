package com.rest.springbootemployee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeservice;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeService employeeservice) {
        this.employeeservice= employeeservice;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getAll() {
        return employeeservice.findAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Integer id) {
        return employeeRepository.findById(id);
    }

    @GetMapping(params = {"gender"})
    public List<Employee> getByGender(@RequestParam String gender) {
        return employeeRepository.findByGender(gender);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee add(@RequestBody Employee employee) {
        return employeeRepository.create(employee);
    }
    @PutMapping("/{id}")
    public Employee update(@PathVariable Integer id, @RequestBody Employee employee) {
        //return employeeRepository.update(id, employee);
        return employeeservice.update(id,employee);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        employeeRepository.delete(id);
    }


    @GetMapping(params = {"page", "pageSize"})
    public List<Employee> getByPage(int page, int pageSize) {
        return employeeRepository.findByPage(page, pageSize);
    }

}
