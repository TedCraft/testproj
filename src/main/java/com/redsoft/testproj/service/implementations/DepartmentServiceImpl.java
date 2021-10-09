package com.redsoft.testproj.service.implementations;

import com.redsoft.testproj.dto.DepartmentDTO;
import com.redsoft.testproj.dto.EmployeeDTO;
import com.redsoft.testproj.entity.Department;
import com.redsoft.testproj.repository.DepartmentRepository;
import com.redsoft.testproj.service.interfaces.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private void checkIsNull(DepartmentDTO departmentDTO)
            throws ResponseStatusException {
        if (isNull(departmentDTO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Table cannot be null!");
        }
        if (isNull(departmentDTO.getName()) || departmentDTO.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter the name of department!");
        }
        if (isNull(departmentDTO.getBudget())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter the budget!");
        }
    }

    private void validateDepartmentDto(DepartmentDTO departmentDTO)
            throws ResponseStatusException {
        checkIsNull(departmentDTO);
        if (!departmentDTO.getName().matches("[a-zA-Zа-яА-ЯёЁ]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must contains only letters!");
        }
        if (departmentDTO.getBudget() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Budget cannot be negative");
        }
    }

    private DepartmentDTO fromDepartmentToDto(Department department) {
        return DepartmentDTO.builder()
                .deptNo(department.getDeptNo())
                .name(department.getName())
                .budget(department.getBudget())
                .build();
    }

    private Department fromDtoToDepartment(DepartmentDTO departmentDTO) {
        return Department.builder()
                .deptNo(departmentDTO.getDeptNo())
                .name(departmentDTO.getName())
                .budget(departmentDTO.getBudget())
                .build();
    }

    @Override
    @CacheEvict(cacheNames = "departmentsCache", key="'findAllDepartments'")
    public DepartmentDTO saveDepartment(DepartmentDTO employeeDTO) {
        validateDepartmentDto(employeeDTO);
        Department savedDepartment = departmentRepository.save(fromDtoToDepartment(employeeDTO));
        return fromDepartmentToDto(savedDepartment);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "departmentsCache", key="'findAllDepartments'"),
                    @CacheEvict(cacheNames = "departmentsCache", key="#departmentDTO.deptNo")
            }
    )
    public void deleteDepartment(DepartmentDTO departmentDTO) {
        departmentRepository.deleteById(departmentDTO.getDeptNo());
    }

    @Override
    @Cacheable(cacheNames = "departmentsCache", key="#deptNo")
    public List<DepartmentDTO> findDepartmentByDeptNo(Integer deptNo)
            throws ResponseStatusException {
        List<DepartmentDTO> departmentDTOList = departmentRepository.findOneByDeptNo(deptNo)
                .stream()
                .map(this::fromDepartmentToDto)
                .collect(Collectors.toList());

        if (departmentDTOList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Departments not found!");
        }

        return departmentDTOList;
    }

    @Override
    @Cacheable(cacheNames = "departmentsCache", key="'findAllDepartments'")
    public List<DepartmentDTO> findAllDepartments()
            throws ResponseStatusException {
        List<DepartmentDTO> departmentDTOList = departmentRepository.findAll()
                .stream()
                .map(this::fromDepartmentToDto)
                .collect(Collectors.toList());

        if (departmentDTOList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Departments not found!");
        }

        return departmentDTOList;
    }
}
