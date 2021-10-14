package com.redsoft.testproj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Integer empNo;
    private Integer deptNo;
    private String firstName;
    private String lastName;
    private Timestamp hireDate;
    private Float salary;
}
