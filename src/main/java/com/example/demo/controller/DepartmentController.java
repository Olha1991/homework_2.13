package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.DepartmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/max-salary")
    public Employee employeeMaxSalary(@RequestParam("department") int department){
        return departmentService.getMaxSalary(department);
    }

    @GetMapping("/min-salary")
    public Employee employeeMinSalary(@RequestParam("department") int department){
        return departmentService.getMinSalary(department);
    }

    @GetMapping(value = "/all", params = "department")
    public List<Employee> employeesFromDepartment(@RequestParam int department){
        return departmentService.employeesFromDepartment(department);
    }

    @GetMapping("/all")
    public Map<Integer, List<Employee>> findEmployees(){
        return departmentService.allEmployeesFromDepartment();
    }
}
