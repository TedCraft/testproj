package com.redsoft.testproj.service.implementations;

import com.redsoft.testproj.dto.EmployeeDTO;
import com.redsoft.testproj.entity.Employee;
import com.redsoft.testproj.repository.EmployeeRepository;
import com.redsoft.testproj.service.interfaces.DepartmentService;
import com.redsoft.testproj.service.interfaces.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private void checkIsNull(EmployeeDTO employeeDTO)
            throws ResponseStatusException {
        if (isNull(employeeDTO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Table cannot be null!");
        }
        if (isNull(employeeDTO.getDeptNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter the DeptNo!");
        }
        if (isNull(employeeDTO.getFirstName()) || employeeDTO.getFirstName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter the First name!");
        }
        if (isNull(employeeDTO.getLastName()) || employeeDTO.getLastName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter the Last name!");
        }
        if (isNull(employeeDTO.getHireDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter the Hire date!");
        }
        if (isNull(employeeDTO.getSalary())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter the Salary!");
        }
    }

    private void validateEmployeeDto(EmployeeDTO employeeDTO)
            throws ResponseStatusException {
        checkIsNull(employeeDTO);
        if (!employeeDTO.getFirstName().matches("[a-zA-Zа-яА-ЯёЁ]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name must contains only letters!");
        }
        if (!employeeDTO.getLastName().matches("[a-zA-Zа-яА-ЯёЁ]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name must contains only letters!");
        }
        if (employeeDTO.getSalary() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary cannot be negative");
        }
    }

    public EmployeeDTO fromEmployeeToDto(Employee employee) {
        return EmployeeDTO.builder()
                .empNo(employee.getEmpNo())
                .deptNo(employee.getDeptNo())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .hireDate(employee.getHireDate())
                .salary(employee.getSalary())
                .build();
    }

    public Employee fromDtoToEmployee(EmployeeDTO employeeDTO) {
        return Employee.builder()
                .empNo(employeeDTO.getEmpNo())
                .deptNo(employeeDTO.getDeptNo())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .hireDate(employeeDTO.getHireDate())
                .salary(employeeDTO.getSalary())
                .build();
    }

    @Override
    @CacheEvict(cacheNames = "employeesCache", key="'findAllEmployees'")
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO)
            throws ResponseStatusException {
        validateEmployeeDto(employeeDTO);
        try {
            Employee savedEmployee = employeeRepository.save(fromDtoToEmployee(employeeDTO));
            return fromEmployeeToDto(savedEmployee);
        }
        catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find Department!");
        }
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "employeesCache", key="'findAllEmployees'"),
                    @CacheEvict(cacheNames = "employeesCache", key="#employeeDTO.empNo"),
                    @CacheEvict(cacheNames = "employeesCache", key="#employeeDTO.deptNo"),
                    @CacheEvict(cacheNames = "employeesCache", key="#employeeDTO.lastName")
            }
    )
    public void deleteEmployee(EmployeeDTO employeeDTO)
            throws ResponseStatusException {
        employeeRepository.deleteById(employeeDTO.getEmpNo());
    }

    @Override
    @Cacheable(cacheNames = "employeesCache", key="#deptNo")
    public List<EmployeeDTO> findEmployeesByDeptNo(Integer deptNo)
            throws ResponseStatusException {
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllByDeptNo(deptNo)
                .stream()
                .map(this::fromEmployeeToDto)
                .collect(Collectors.toList());

        if (employeeDTOList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employees not found!");
        }

        return employeeDTOList;
    }

    @Override
    @Cacheable(cacheNames = "employeesCache", key="'findAllEmployees'")
    public List<EmployeeDTO> findAllEmployees() {
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAll()
                .stream()
                .map(this::fromEmployeeToDto)
                .collect(Collectors.toList());

        return employeeDTOList;
    }

    @Override
    @Cacheable(cacheNames = "employeesCache", key="#empNo")
    public List<EmployeeDTO> findEmployeesByEmpNo(Integer empNo)
            throws ResponseStatusException {
        List<EmployeeDTO> employeeDTO = employeeRepository.findOneByEmpNo(empNo)
                .stream()
                .map(this::fromEmployeeToDto)
                .collect((Collectors.toList()));

            if(employeeDTO.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee by id " + empNo + " not found!");
            }

            return employeeDTO;
    }

    @Override
    @Cacheable(cacheNames = "employeesCache", key="#lastName")
    public List<EmployeeDTO> findEmployeesByFirstNameAndLastName(String firstName, String lastName)
            throws ResponseStatusException {
        if (!firstName.matches("[a-zA-Zа-яА-ЯёЁ]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name must contains only letters!");
        }
        if (!lastName.matches("[a-zA-Zа-яА-ЯёЁ]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name must contains only letters!");
        }
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllByFirstNameAndLastNameAllIgnoreCase(firstName, lastName)
                .stream()
                .map(this::fromEmployeeToDto)
                .collect(Collectors.toList());

        if (employeeDTOList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employees not found!");
        }

        return employeeDTOList;
    }
}
