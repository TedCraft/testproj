package com.redsoft.testproj.controller;

import com.redsoft.testproj.dto.EmployeeDTO;
import com.redsoft.testproj.service.interfaces.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping(value = "/employees")
    public EmployeeDTO create(@RequestBody EmployeeDTO employee)
            throws ResponseStatusException {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping(value = "/employees")
    public List<EmployeeDTO> read() {
        return employeeService.findAllEmployees();
    }

    @GetMapping(value = "/employees/{id}")
    public List<EmployeeDTO> read(@PathVariable(name = "id") Integer empNo)
            throws ResponseStatusException {
        return employeeService.findEmployeesByEmpNo(empNo);
    }

    @PostMapping(value = "/employees/{id}")
    public EmployeeDTO update(
            @PathVariable(name = "id") Integer empNo,
            @RequestBody EmployeeDTO employee)
            throws ResponseStatusException {

        EmployeeDTO dbEmployee = employeeService.findEmployeesByEmpNo(empNo).get(0);
        BeanUtils.copyProperties(employee, dbEmployee, "empNo");
        return employeeService.saveEmployee(dbEmployee);
    }

    @DeleteMapping(value = "/employees/{id}")
    public void delete(@PathVariable(name = "id") Integer empNo) {
        EmployeeDTO employeeDTO = employeeService.findEmployeesByEmpNo(empNo).get(0);
        employeeService.deleteEmployee(employeeDTO);
    }

    @GetMapping(value = "/departments/{id}/employees")
    public List<EmployeeDTO> readByDept_no(@PathVariable(name = "id") Integer deptNo)
            throws ResponseStatusException {
        return employeeService.findEmployeesByDeptNo(deptNo);
    }

    @GetMapping(value = "/employees/")
    public List<EmployeeDTO> readByFirstAndLastName(
            @RequestParam String name,
            @RequestParam String lastname)
            throws ResponseStatusException {
        return employeeService.findEmployeesByFirstNameAndLastName(name, lastname);
    }
}