package com.example.demo;

import com.example.demo.exception.EmployeeAlreadyAddedException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.exception.IncorrectFirstNameException;
import com.example.demo.exception.StorageIsFullException;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.ValidatorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class EmployeeServiceTest {
    private final EmployeeService employeeService = new EmployeeService(new ValidatorService());

    @AfterEach
    public void afterEach(){
        employeeService.getAll().forEach(employee -> employeeService.remove(employee.getFirstName(), employee.getLastName()));
    }
    @ParameterizedTest
    @MethodSource("params")
    public void addNegativeTest1(String firstName, String lastName, double salary, int department){

        Employee expected = new Employee(firstName, lastName, salary, department);

        assertThat(employeeService.getAll()).isEmpty();
        employeeService.add(firstName, lastName,salary,department);
        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);
        assertThat(employeeService.find(expected.getFirstName(), expected.getLastName()))
                .isNotNull()
                .isEqualTo(expected);

        assertThatExceptionOfType(EmployeeAlreadyAddedException.class)
                .isThrownBy(() -> employeeService.add(firstName, lastName, salary, department));
    }
    @ParameterizedTest
    @MethodSource("params")
    public void addNegativeTest2(String firstName, String lastName, double salary, int department){
        List<Employee> employees = generateEmployees(10);
        employees.forEach(employee ->
                assertThat(employeeService.add(employee.getFirstName(), employee.getLastName(), employee.getSalary(), employee.getDepartment())).isEqualTo(employee)
    );
        assertThat(employeeService.getAll()).hasSize(10);
        assertThatExceptionOfType(StorageIsFullException.class)
                .isThrownBy(() -> employeeService.add(firstName, lastName, salary, department));
    }

    @Test
    public void addNegativeTest3(){
        assertThatExceptionOfType(IncorrectFirstNameException.class)
                .isThrownBy(() -> employeeService.add("Ivan", "Ivanov",1000, 1));

        assertThatExceptionOfType(IncorrectFirstNameException.class)
                .isThrownBy(() -> employeeService.add("Vera", "!Belkina", 2000, 1));
        assertThatExceptionOfType(IncorrectFirstNameException.class)
                .isThrownBy(() -> employeeService.add(null, "Petrovova", 3000, 2));
    }

    @ParameterizedTest
    @MethodSource("params")
    public void removeNegativeTest(String firstName, String lastName, double salary, int department){
        assertThat(employeeService.getAll()).isEmpty();
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.remove("test", "test"));

        Employee expected = new Employee(firstName, lastName, salary, department);
        employeeService.add(firstName, lastName, salary, department);
        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.remove("test", "test"));
    }

    @ParameterizedTest
    @MethodSource("params")
    public void removePositiveTest(String firstName, String lastName, double salary, int department){
        assertThat(employeeService.getAll()).isEmpty();
        Employee expected = new Employee(firstName, lastName, salary, department);
        employeeService.add(firstName, lastName, salary, department);
        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);
        assertThat(employeeService.remove(firstName,lastName)).isEqualTo(expected);
        assertThat(employeeService.getAll()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("params")
    public void findPositiveTest(String firstName, String lastName, double salary, int department){
        assertThat(employeeService.getAll()).isEmpty();
        Employee expected = new Employee(firstName, lastName, salary, department);
        employeeService.add(firstName, lastName, salary, department);
        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);
        assertThat(employeeService.find(firstName, lastName)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("params")
    public void findNegativeTest(String firstName, String lastName, double salary, int department){
        assertThat(employeeService.getAll()).isEmpty();
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.find("test", "test"));

        Employee expected = new Employee(firstName, lastName, salary, department);
        employeeService.add(firstName, lastName, salary, department);
        assertThat(employeeService.getAll())
                .hasSize(1)
                .containsExactly(expected);
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.find("test", "test"));
    }

    private List<Employee> generateEmployees(int size){
        return Stream.iterate(1, i -> i + 1)
                .limit(size)
                .map(i -> new Employee("FirstName" + (char) ((int) 'a' + 1), "lLastName" + (char) ((int) 'a' + 1), i, 1000 + i))
                .collect(Collectors.toList());
    }
    public static Stream<Arguments> params(){
        return Stream.of(
                Arguments.of("Ivan", "Ivanov",1000, 1),
                Arguments.of("Vera", "Belkina", 2000, 1),
                Arguments.of("Mariya", "Petrovova", 3000, 2)
        );
    }

}