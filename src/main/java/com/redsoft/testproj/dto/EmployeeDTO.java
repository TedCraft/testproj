package com.redsoft.testproj.dto;


import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class EmployeeDTO {

    private Integer empNo;
    private Integer deptNo;
    private String firstName;
    private String lastName;
    private Timestamp hireDate;
    private Float salary;
}
