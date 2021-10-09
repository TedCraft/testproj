package com.redsoft.testproj.service.interfaces;

import com.redsoft.testproj.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    void deleteEmployee(EmployeeDTO employeeDTO);
    List<EmployeeDTO> findEmployeesByDeptNo(Integer deptNo);
    List<EmployeeDTO> findAllEmployees();
    List<EmployeeDTO> findEmployeesByEmpNo(Integer empNo);
    List<EmployeeDTO> findEmployeesByFirstNameAndLastName(String firstName, String lastName);
}
