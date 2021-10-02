package com.redsoft.testproj.controller;

import com.redsoft.testproj.dto.DepartmentDTO;
import com.redsoft.testproj.service.interfaces.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping(value = "/departments")
    public DepartmentDTO create(@RequestBody DepartmentDTO department)
            throws ResponseStatusException {
        return departmentService.saveDepartment(department);
    }

    @GetMapping(value = "/departments")
    public List<DepartmentDTO> read()
            throws ResponseStatusException {
        return departmentService.findAllDepartments();
    }

    @GetMapping(value = "/departments/{id}")
    public List<DepartmentDTO> read(@PathVariable(name = "id") Integer deptNo)
            throws ResponseStatusException {
        return departmentService.findDepartmentByDeptNo(deptNo);
    }

    @PostMapping(value = "/departments/{id}")
    public DepartmentDTO update(
            @PathVariable(name = "id") Integer deptNo,
            @RequestBody DepartmentDTO department)
            throws ResponseStatusException {

        DepartmentDTO dbDepartment = departmentService.findDepartmentByDeptNo(deptNo).get(0);
        BeanUtils.copyProperties(department, dbDepartment, "deptNo");
        return departmentService.saveDepartment(dbDepartment);
    }

    @DeleteMapping(value = "/departments/{id}")
    public void delete(@PathVariable(name = "id") Integer deptNo) {
        DepartmentDTO departmentDTO = departmentService.findDepartmentByDeptNo(deptNo).get(0);
        departmentService.deleteDepartment(departmentDTO);
    }
}