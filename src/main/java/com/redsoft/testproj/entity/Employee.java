package com.redsoft.testproj.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "EMPLOYEE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @Column(name = "EMP_NO", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empNo;

    @Column(name = "DEPT_NO", nullable = false)
    private Integer deptNo;

    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "HIRE_DATE")
    private Timestamp hireDate;
    @Column(name = "SALARY")
    private Float salary;
}