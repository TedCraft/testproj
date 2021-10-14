package com.redsoft.testproj.service.implementations;

import com.redsoft.testproj.dto.EmployeeDTO;
import com.redsoft.testproj.entity.Employee;
import com.redsoft.testproj.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EmployeeServiceImpl.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeServiceImplTest {
    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    private static Employee employee;
    private static EmployeeDTO employeeDTO;

    @BeforeAll
    public static void setUp() {
        createEmployee();
        createEmployeeDTO();
    }

    private static EmployeeDTO createEmployeeDTO() {
        employeeDTO = new EmployeeDTO();
        employeeDTO.setEmpNo(1);
        employeeDTO.setDeptNo(1);
        employeeDTO.setFirstName("Unit");
        employeeDTO.setLastName("Test");
        employeeDTO.setHireDate(new Timestamp(0));
        employeeDTO.setSalary(10000f);

        return employeeDTO;
    }

    private static Employee createEmployee() {
        employee = new Employee();
        employee.setEmpNo(1);
        employee.setDeptNo(1);
        employee.setFirstName("Unit");
        employee.setLastName("Test");
        employee.setHireDate(new Timestamp(0));
        employee.setSalary(10000f);

        return employee;
    }

    @Test
    public void testSaveEmployee() throws ResponseStatusException {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        EmployeeDTO actualCreateResult = employeeServiceImpl.saveEmployee(employeeDTO);

        assertEquals(employeeDTO.getEmpNo(), actualCreateResult.getEmpNo().intValue());
        assertEquals(employeeDTO.getDeptNo(), actualCreateResult.getDeptNo().intValue());
        assertEquals(employeeDTO.getFirstName(), actualCreateResult.getFirstName());
        assertEquals(employeeDTO.getLastName(), actualCreateResult.getLastName());
        assertEquals(employeeDTO.getHireDate(), actualCreateResult.getHireDate());
        assertEquals(employeeDTO.getSalary(), actualCreateResult.getSalary().floatValue());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    public void testSaveEmployeeValidationException() throws ResponseStatusException {
        employeeDTO.setFirstName(null);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Throwable thrown = assertThrows(ResponseStatusException.class, () -> {
            EmployeeDTO actualCreateResult = employeeServiceImpl.saveEmployee(employeeDTO);
            assertEquals(employeeDTO.getEmpNo(), actualCreateResult.getEmpNo().intValue());
            assertEquals(employeeDTO.getDeptNo(), actualCreateResult.getDeptNo().intValue());
            assertEquals(employeeDTO.getFirstName(), actualCreateResult.getFirstName());
            assertEquals(employeeDTO.getLastName(), actualCreateResult.getLastName());
            assertEquals(employeeDTO.getHireDate(), actualCreateResult.getHireDate());
            assertEquals(employeeDTO.getSalary(), actualCreateResult.getSalary().floatValue());
        });

        assertNotNull(thrown);
        assertEquals("400 BAD_REQUEST \"Enter the First name!\"", thrown.getMessage());
        verify(employeeRepository, times(0)).save(any(Employee.class));

        employeeDTO.setFirstName("Unit");
    }

    @Test
    public void testDeleteEmployee() throws ResponseStatusException {
        doNothing().when(this.employeeRepository).deleteById(any(Integer.class));
        this.employeeServiceImpl.deleteEmployee(employeeDTO);
        verify(this.employeeRepository).deleteById(any(Integer.class));
    }

    @Test
    public void testFindEmployeesByDeptNo() throws ResponseStatusException {
        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(this.employeeRepository.findAllByDeptNo(any(Integer.class)))
                .thenReturn(employeeList);
        assertEquals(1, this.employeeServiceImpl.findEmployeesByDeptNo(1).size());
        verify(this.employeeRepository).findAllByDeptNo(any(Integer.class));
    }

    @Test
    public void testFindEmployeesByDeptNoNotFoundException() throws ResponseStatusException {
        when(this.employeeRepository.findAllByDeptNo(any(Integer.class)))
                .thenReturn(new ArrayList<>());
        Throwable thrown = assertThrows(ResponseStatusException.class, () ->
                this.employeeServiceImpl.findEmployeesByDeptNo(1));
        assertEquals("404 NOT_FOUND \"Employees not found!\"", thrown.getMessage());
        verify(this.employeeRepository).findAllByDeptNo(any(Integer.class));
    }

    @Test
    public void testFindAllEmployees() throws ResponseStatusException {
        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(this.employeeRepository.findAll())
                .thenReturn(employeeList);
        assertEquals(1, this.employeeServiceImpl.findAllEmployees().size());
        verify(this.employeeRepository).findAll();
    }

    @Test
    public void testFindEmployeesByEmpNo() throws ResponseStatusException {
        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(this.employeeRepository.findOneByEmpNo(any(Integer.class)))
                .thenReturn(employeeList);
        assertEquals(1, this.employeeServiceImpl.findEmployeesByEmpNo(1).size());
        verify(this.employeeRepository).findOneByEmpNo(any(Integer.class));
    }

    @Test
    public void testFindEmployeesByEmpNoNotFoundException() throws ResponseStatusException {
        when(this.employeeRepository.findOneByEmpNo(any(Integer.class)))
                .thenReturn(new ArrayList<>());
        Throwable thrown = assertThrows(ResponseStatusException.class, () ->
                this.employeeServiceImpl.findEmployeesByEmpNo(1));
        assertEquals("404 NOT_FOUND \"Employee by id 1 not found!\"", thrown.getMessage());
        verify(this.employeeRepository).findOneByEmpNo(any(Integer.class));
    }

    @Test
    public void testFindEmployeesByFirstNameAndLastNameNotFoundException() throws ResponseStatusException {
        when(this.employeeRepository.findAllByFirstNameAndLastNameAllIgnoreCase(anyString(), anyString()))
                .thenReturn(new ArrayList<>());
        Throwable thrown = assertThrows(ResponseStatusException.class, () ->
                this.employeeServiceImpl.findEmployeesByFirstNameAndLastName("Unit", "Test"));
        assertEquals("404 NOT_FOUND \"Employees not found!\"", thrown.getMessage());
        verify(this.employeeRepository).findAllByFirstNameAndLastNameAllIgnoreCase(anyString(), anyString());
    }

    @Test
    public void testFindEmployeesByFirstNameAndLastNameValidationException() throws ResponseStatusException {
        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(this.employeeRepository.findAllByFirstNameAndLastNameAllIgnoreCase(anyString(), anyString()))
                .thenReturn(employeeList);
        Throwable thrown = assertThrows(ResponseStatusException.class, () ->
                this.employeeServiceImpl.findEmployeesByFirstNameAndLastName("123", "Test"));
        assertEquals("400 BAD_REQUEST \"First name must contains only letters!\"", thrown.getMessage());
        verify(this.employeeRepository, times(0)).findAllByFirstNameAndLastNameAllIgnoreCase(anyString(), anyString());
    }

    @Test
    public void testFindEmployeesByFirstNameAndLastName() throws ResponseStatusException {
        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(this.employeeRepository.findAllByFirstNameAndLastNameAllIgnoreCase(anyString(), anyString()))
                .thenReturn(employeeList);
        assertEquals(1, this.employeeServiceImpl.findEmployeesByFirstNameAndLastName("Unit", "Test").size());
        verify(this.employeeRepository).findAllByFirstNameAndLastNameAllIgnoreCase(anyString(), anyString());
    }
}

