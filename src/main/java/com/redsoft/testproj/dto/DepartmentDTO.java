package com.redsoft.testproj.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentDTO {

    private Integer deptNo;
    private String name;
    private Float budget;
}
