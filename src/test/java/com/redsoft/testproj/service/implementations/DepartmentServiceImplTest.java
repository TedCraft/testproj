package com.redsoft.testproj.service.implementations;

import com.redsoft.testproj.dto.DepartmentDTO;
import com.redsoft.testproj.entity.Department;
import com.redsoft.testproj.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DepartmentServiceImpl.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DepartmentServiceImplTest {
    @MockBean
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;

    private static Department department;
    private static DepartmentDTO departmentDTO;

    @BeforeAll
    public static void setUp() {
        createDepartment();
        createDepartmentDTO();
    }

    private static void createDepartmentDTO() {
        departmentDTO = new DepartmentDTO();
        departmentDTO.setDeptNo(1);
        departmentDTO.setName("Unit Test");
        departmentDTO.setBudget(10000f);
    }

    private static void createDepartment() {
        department = new Department();
        department.setDeptNo(1);
        department.setName("Unit Test");
        department.setBudget(10000f);
    }

    @Test
    public void testSaveDepartment() throws ResponseStatusException {
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(department);
        DepartmentDTO actualCreateResult = departmentServiceImpl.saveDepartment(departmentDTO);

        assertEquals(departmentDTO.getDeptNo(), actualCreateResult.getDeptNo().intValue());
        assertEquals(departmentDTO.getName(), actualCreateResult.getName());
        assertEquals(departmentDTO.getBudget(), actualCreateResult.getBudget().floatValue());

        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    public void testSaveDepartmentValidationException() throws ResponseStatusException {
        departmentDTO.setName(null);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        Throwable thrown = assertThrows(ResponseStatusException.class, () -> {
            DepartmentDTO actualCreateResult = departmentServiceImpl.saveDepartment(departmentDTO);
            assertEquals(departmentDTO.getDeptNo(), actualCreateResult.getDeptNo().intValue());
            assertEquals(departmentDTO.getName(), actualCreateResult.getName());
            assertEquals(departmentDTO.getBudget(), actualCreateResult.getBudget().floatValue());
        });

        assertNotNull(thrown);
        assertEquals("400 BAD_REQUEST \"Enter the name of department!\"", thrown.getMessage());
        verify(departmentRepository, times(0)).save(any(Department.class));

        departmentDTO.setName("Unit Test");
    }

    @Test
    public void testDeleteDepartment() throws ResponseStatusException {
        doNothing().when(this.departmentRepository).deleteById(any(Integer.class));
        this.departmentServiceImpl.deleteDepartment(departmentDTO);
        verify(this.departmentRepository).deleteById(any(Integer.class));
    }

    @Test
    public void testFindDepartmentsByDeptNo() throws ResponseStatusException {
        ArrayList<Department> departmentList = new ArrayList<>();
        departmentList.add(department);
        when(this.departmentRepository.findOneByDeptNo(any(Integer.class)))
                .thenReturn(departmentList);
        assertEquals(1, this.departmentServiceImpl.findDepartmentByDeptNo(1).size());
        verify(this.departmentRepository).findOneByDeptNo(any(Integer.class));
    }

    @Test
    public void testFindDepartmentsByDeptNoNotFoundException() throws ResponseStatusException {
        when(this.departmentRepository.findOneByDeptNo(any(Integer.class)))
                .thenReturn(new ArrayList<>());
        Throwable thrown = assertThrows(ResponseStatusException.class, () ->
                this.departmentServiceImpl.findDepartmentByDeptNo(1));
        assertEquals("404 NOT_FOUND \"Departments not found!\"", thrown.getMessage());
        verify(this.departmentRepository).findOneByDeptNo(any(Integer.class));
    }

    @Test
    public void testFindAllDepartments() throws ResponseStatusException {
        ArrayList<Department> departmentList = new ArrayList<>();
        departmentList.add(department);
        when(this.departmentRepository.findAll())
                .thenReturn(departmentList);
        assertEquals(1, this.departmentServiceImpl.findAllDepartments().size());
        verify(this.departmentRepository).findAll();
    }
}