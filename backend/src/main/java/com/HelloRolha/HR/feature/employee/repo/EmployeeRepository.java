package com.HelloRolha.HR.feature.employee.repo;

import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, EmployeeRepositoryCustom {
    Optional<Employee> findByUsername(String username);

    List<Employee> findAllByStatus(boolean b);

    List<Employee> findALLByAuthority(String userAdmin);

    List<Employee> findByDepartmentId(Integer departmentId);
}
