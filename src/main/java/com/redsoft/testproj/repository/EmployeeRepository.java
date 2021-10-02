package com.redsoft.testproj.repository;

import com.redsoft.testproj.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findAllByDeptNo(Integer deptNo);
    List<Employee> findOneByEmpNo(Integer empNo);
    List<Employee> findAllByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName);
}