package com.redsoft.testproj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsoft.testproj.dto.EmployeeDTO;
import com.redsoft.testproj.service.interfaces.EmployeeService;
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

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EmployeeController.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeControllerTest {
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private EmployeeController employeeController;

    private static EmployeeDTO employeeDTO;

    @BeforeAll
    public static void setUp() {
        employeeDTO = createEmployeeDTO();
    }

    private static EmployeeDTO createEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmpNo(1);
        employeeDTO.setDeptNo(1);
        employeeDTO.setFirstName("Unit");
        employeeDTO.setLastName("Test");
        employeeDTO.setHireDate(new Timestamp(0));
        employeeDTO.setSalary(100000f);

        return employeeDTO;
    }

    @Test
    public void testCreate() throws Exception {
        String jsonEmployeeDTO = new ObjectMapper().writeValueAsString(employeeDTO);
        when(this.employeeService.saveEmployee(any(EmployeeDTO.class)))
                .thenReturn(employeeDTO);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEmployeeDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(jsonEmployeeDTO));
    }

    @Test
    public void testCreateValidationError() throws Exception {
        String jsonEmployeeDTO = new ObjectMapper().writeValueAsString(employeeDTO);
        when(this.employeeService.saveEmployee(any(EmployeeDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEmployeeDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testUpdate() throws Exception {
        String jsonEmployeeDTO = new ObjectMapper().writeValueAsString(employeeDTO);
        ArrayList<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(employeeDTO);
        when(this.employeeService.findEmployeesByEmpNo(any(Integer.class)))
                .thenReturn(employeeDTOList);
        when(this.employeeService.saveEmployee(any(EmployeeDTO.class)))
                .thenReturn(employeeDTO);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEmployeeDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(jsonEmployeeDTO));
    }

    @Test
    public void testUpdateValidationError() throws Exception {
        String jsonEmployeeDTO = new ObjectMapper().writeValueAsString(employeeDTO);
        ArrayList<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(employeeDTO);
        when(this.employeeService.findEmployeesByEmpNo(any(Integer.class)))
                .thenReturn(employeeDTOList);
        when(this.employeeService.saveEmployee(any(EmployeeDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEmployeeDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testUpdateNotFoundError() throws Exception {
        String jsonEmployeeDTO = new ObjectMapper().writeValueAsString(employeeDTO);
        when(this.employeeService.findEmployeesByEmpNo(any(Integer.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(this.employeeService.saveEmployee(any(EmployeeDTO.class)))
                .thenReturn(employeeDTO);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.post("/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEmployeeDTO);
        getResult.contentType(MediaType.APPLICATION_JSON);
        getResult.accept(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(404));
        verify(employeeService, times(0)).saveEmployee(any(EmployeeDTO.class));
    }

    @Test
    public void testDelete() throws Exception {
        ArrayList<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(employeeDTO);
        doNothing().when(this.employeeService).deleteEmployee(any(EmployeeDTO.class));
        when(this.employeeService.findEmployeesByEmpNo(any(Integer.class)))
                .thenReturn(employeeDTOList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.delete("/employees/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteNotFoundException() throws Exception {
        when(this.employeeService.findEmployeesByEmpNo(any(Integer.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.delete("/employees/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    public void testRead() throws Exception {
        when(this.employeeService.findAllEmployees())
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/employees");
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testReadById() throws Exception {
        ArrayList<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(employeeDTO);
        String jsonEmployeeDTOList = new ObjectMapper().writeValueAsString(employeeDTOList);
        when(this.employeeService.findEmployeesByEmpNo(any(Integer.class)))
                .thenReturn(employeeDTOList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/employees/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(jsonEmployeeDTOList));
    }

    @Test
    public void testReadByIdNotFoundException() throws Exception {
        when(this.employeeService.findEmployeesByEmpNo(any(Integer.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/employees/{id}", 1);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    public void testReadByDeptNo() throws Exception {
        ArrayList<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(employeeDTO);
        String jsonEmployeeDTOList = new ObjectMapper().writeValueAsString(employeeDTOList);
        when(this.employeeService.findEmployeesByDeptNo(any(Integer.class)))
                .thenReturn(employeeDTOList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/departments/{id}/employees", 1);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(jsonEmployeeDTOList));
    }

    @Test
    public void testReadByDeptNoNotFoundException() throws Exception {
        when(this.employeeService.findEmployeesByDeptNo(any(Integer.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/departments/{id}/employees", 1);
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    public void testReadByFirstAndLastName() throws Exception {
        ArrayList<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(employeeDTO);
        String jsonEmployeeDTOList = new ObjectMapper().writeValueAsString(employeeDTOList);
        when(this.employeeService.findEmployeesByFirstNameAndLastName(anyString(), anyString()))
                .thenReturn(employeeDTOList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/")
                .param("name", "Unit")
                .param("lastname", "Test");
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(jsonEmployeeDTOList));
    }

    @Test
    public void testReadByFirstAndLastNameValidationException() throws Exception {
        when(this.employeeService.findEmployeesByFirstNameAndLastName(anyString(), anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/")
                .param("name", "123")
                .param("lastname", "Test");
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testReadByFirstAndLastNameNotFoundException() throws Exception {
        when(this.employeeService.findEmployeesByFirstNameAndLastName(anyString(), anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/")
                .param("name", "Unit")
                .param("lastname", "Test");
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(404));
    }
}