package com.redsoft.testproj.service.interfaces;

import com.redsoft.testproj.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentDTO saveDepartment(DepartmentDTO employeeDTO);
    void deleteDepartment(DepartmentDTO departmentDTO);
    List<DepartmentDTO> findDepartmentByDeptNo(Integer deptNo);
    List<DepartmentDTO> findAllDepartments();
}
