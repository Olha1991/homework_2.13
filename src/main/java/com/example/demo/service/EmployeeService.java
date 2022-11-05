package com.example.demo.service;

import com.example.demo.exception.EmployeeAlreadyAddedException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.exception.StorageIsFullException;
import com.example.demo.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    private static final int LIMIT = 10;
    private final Map<String, Employee> employees = new HashMap<>();
    private  final ValidatorService validatorService;

    public EmployeeService(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    public String getKey(String firstName, String lastName){
        return firstName + " | " + lastName ;
    }

    public Employee add(String firstName, String lastName, double salary, int department){
        Employee employee = new Employee(validatorService.validateFirstName(firstName),
                validatorService.validateLastName(lastName),
                salary,
                department
        );
        String key = getKey(firstName, lastName);
        if(employees.containsKey(key)){
            throw new EmployeeAlreadyAddedException();
        }
        if(employees.size() < LIMIT){
            employees.put(key, employee);
            return employee;
        }
        throw new StorageIsFullException();
    }
    public Employee remove(String firstName, String lastName){
        String key = getKey(firstName, lastName);
        if(!employees.containsKey(key)){
            throw new EmployeeNotFoundException();
        }
        return employees.remove(key);
    }
    public Employee find(String firstName, String lastName) {
    String key = getKey(firstName, lastName);
    if(!employees.containsKey(key)){
        throw new EmployeeNotFoundException();
    }
    return employees.remove(key);
    }

    public List<Employee> getAll(){
        return new ArrayList<>(employees.values());
    }

}
