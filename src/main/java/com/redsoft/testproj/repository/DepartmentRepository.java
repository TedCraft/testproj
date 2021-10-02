package com.redsoft.testproj.repository;

import com.redsoft.testproj.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    List<Department> findOneByDeptNo(Integer deptNo);
}
