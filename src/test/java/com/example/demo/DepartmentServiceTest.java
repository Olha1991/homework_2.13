package com.example.demo;


import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    public void beforeEach(){
        List<Employee> employees = List.of(
                new Employee("Yan", "Romanuk", 3000, 3),
                new Employee("Larisa","Matvienko", 1000, 2),
                new Employee("Egor","Tarasov", 4000, 3),
                new Employee("Olga","Shevchenko", 2000, 1),
                new Employee("Roman","Pechkin", 3000, 2),
                new Employee("Yana","Prokopenko", 4000, 1)
        );
        when(employeeService.getAll()).thenReturn(employees);
    }

    @ParameterizedTest
    @MethodSource("employeeMaxSalaryParams")
    public void employeeMaxSalaryPositiveTest(int department, Employee expected){
        assertThat(departmentService.getMaxSalary(department)).isEqualTo(expected);
    }

    @Test
    public void employeeMaxSalaryNegativeTest(){
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> departmentService.getMaxSalary(4));
    }

    @ParameterizedTest
    @MethodSource("employeeMinSalaryParams")
    public void employeeMinSalaryPositiveTest(int department, Employee expected){
        assertThat(departmentService.getMinSalary(department)).isEqualTo(expected);
    }

    @Test
    public void employeeMinSalaryNegativeTest(){
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> departmentService.getMinSalary(4));
    }

    @ParameterizedTest
    @MethodSource("employeesFromDepartmentParams")
    public void employeesFromDepartmentPositiveTest(int department, List<Employee> expected){
        assertThat(departmentService.employeesFromDepartment(department)).containsExactlyElementsOf(expected);
    }

    @Test
    public void allEmployeesFromDepartmentTest(){
        assertThat(departmentService.allEmployeesFromDepartment()).containsAllEntriesOf(
                Map.of(
                        1, List.of(new Employee("Olga","Shevchenko", 2000, 1),
                                new Employee("Yana","Prokopenko", 4000, 1)),
                        2, List.of(new Employee("Larisa","Matvienko", 1000, 2),
                                new Employee("Roman","Pechkin", 3000, 2)),
                        3, List.of(new Employee("Egor","Tarasov", 4000, 3),
                                new Employee("Yan","Romanuk", 3000, 3))


                )
        );
    }
    public static Stream<Arguments> employeeMaxSalaryParams(){
        return Stream.of(
                Arguments.of(1,  new Employee("Yana","Prokopenko", 4000, 1)),
                Arguments.of(2, new Employee("Roman","Pechkin", 3000, 2)),
                Arguments.of(3, new Employee("Egor","Tarasov", 4000, 3))
        );
    }

    public static Stream<Arguments> employeeMinSalaryParams(){
        return Stream.of(
                Arguments.of(1, new Employee("Olga","Shevchenko", 2000, 1)),
                Arguments.of(2, new Employee("Larisa","Matvienko", 1000, 2)),
                Arguments.of(3, new Employee("Yan", "Romanuk", 3000, 3))
        );
    }

    public static Stream<Arguments> employeesFromDepartmentParams() {
        return Stream.of(
                Arguments.of(1, List.of(new Employee("Olga", "Shevchenko", 2000, 1),
                        new Employee("Yana", "Prokopenko", 4000, 1))),
                Arguments.of(2, List.of(new Employee("Larisa", "Matvienko", 1000, 2),
                        new Employee("Roman", "Pechkin", 3000, 2))),
                Arguments.of(3, List.of(new Employee("Egor", "Tarasov", 4000, 3),
                        new Employee("Yan", "Romanuk", 3000, 3))),
                Arguments.of(4, Collections.emptyList())
        );
    }

}
