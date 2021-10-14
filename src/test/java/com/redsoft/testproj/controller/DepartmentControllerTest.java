package com.redsoft.testproj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsoft.testproj.dto.DepartmentDTO;
import com.redsoft.testproj.service.interfaces.DepartmentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DepartmentController.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DepartmentControllerTest {
    @MockBean
    private DepartmentService DepartmentService;

    @Autowired
    private DepartmentController DepartmentController;

    private static DepartmentDTO DepartmentDTO;

    @BeforeAll
    public static void setUp() {
        DepartmentDTO = createDepartmentDTO();
    }

    private static DepartmentDTO createDepartmentDTO() {
        DepartmentDTO DepartmentDTO = new DepartmentDTO();
        DepartmentDTO.setDeptNo(1);
        DepartmentDTO.setName("Unit Test");
        DepartmentDTO.setBudget(100000f);

        return DepartmentDTO;
    }

    @Test
    public void testCreate() throws Exception {
        String jsonDepartmentDTO = new ObjectMapper().writeValueAsString(DepartmentDTO);
        when(this.DepartmentService.saveDepartment(any(DepartmentDTO.class)))
                .thenReturn(DepartmentDTO);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDepartmentDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(jsonDepartmentDTO));
    }

    @Test
    public void testCreateValidationError() throws Exception {
        String jsonDepartmentDTO = new ObjectMapper().writeValueAsString(DepartmentDTO);
        when(this.DepartmentService.saveDepartment(any(DepartmentDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDepartmentDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testUpdate() throws Exception {
        String jsonDepartmentDTO = new ObjectMapper().writeValueAsString(DepartmentDTO);
        ArrayList<DepartmentDTO> DepartmentDTOList = new ArrayList<>();
        DepartmentDTOList.add(DepartmentDTO);
        when(this.DepartmentService.findDepartmentByDeptNo(any(Integer.class)))
                .thenReturn(DepartmentDTOList);
        when(this.DepartmentService.saveDepartment(any(DepartmentDTO.class)))
                .thenReturn(DepartmentDTO);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/departments/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDepartmentDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(jsonDepartmentDTO));
    }

    @Test
    public void testUpdateValidationError() throws Exception {
        String jsonDepartmentDTO = new ObjectMapper().writeValueAsString(DepartmentDTO);
        ArrayList<DepartmentDTO> DepartmentDTOList = new ArrayList<>();
        DepartmentDTOList.add(DepartmentDTO);
        when(this.DepartmentService.findDepartmentByDeptNo(any(Integer.class)))
                .thenReturn(DepartmentDTOList);
        when(this.DepartmentService.saveDepartment(any(DepartmentDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/departments/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDepartmentDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testUpdateNotFoundError() throws Exception {
        String jsonDepartmentDTO = new ObjectMapper().writeValueAsString(DepartmentDTO);
        when(this.DepartmentService.findDepartmentByDeptNo(any(Integer.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(this.DepartmentService.saveDepartment(any(DepartmentDTO.class)))
                .thenReturn(DepartmentDTO);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/departments/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDepartmentDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(404));
        verify(DepartmentService, times(0)).saveDepartment(any(DepartmentDTO.class));
    }

    @Test
    public void testDelete() throws Exception {
        ArrayList<DepartmentDTO> DepartmentDTOList = new ArrayList<>();
        DepartmentDTOList.add(DepartmentDTO);
        doNothing().when(this.DepartmentService).deleteDepartment(any(DepartmentDTO.class));
        when(this.DepartmentService.findDepartmentByDeptNo(any(Integer.class)))
                .thenReturn(DepartmentDTOList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.delete("/departments/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteNotFoundException() throws Exception {
        when(this.DepartmentService.findDepartmentByDeptNo(any(Integer.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.delete("/departments/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    public void testRead() throws Exception {
        when(this.DepartmentService.findAllDepartments())
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/departments");
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testReadByDeptNo() throws Exception {
        ArrayList<DepartmentDTO> DepartmentDTOList = new ArrayList<>();
        DepartmentDTOList.add(DepartmentDTO);
        String jsonDepartmentDTOList = new ObjectMapper().writeValueAsString(DepartmentDTOList);
        when(this.DepartmentService.findDepartmentByDeptNo(any(Integer.class)))
                .thenReturn(DepartmentDTOList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/departments/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(jsonDepartmentDTOList));
    }

    @Test
    public void testReadByDeptNoNotFoundException() throws Exception {
        when(this.DepartmentService.findDepartmentByDeptNo(any(Integer.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/departments/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.DepartmentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(404));
    }
}