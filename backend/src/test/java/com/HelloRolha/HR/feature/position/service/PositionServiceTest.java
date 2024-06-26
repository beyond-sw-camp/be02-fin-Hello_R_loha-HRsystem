package com.HelloRolha.HR.feature.position.service;

import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpReq;
import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpRes;
import com.HelloRolha.HR.feature.employee.model.entity.Employee;
import com.HelloRolha.HR.feature.employee.repo.EmployeeRepository;
import com.HelloRolha.HR.feature.employee.service.EmployeeService;
import com.HelloRolha.HR.feature.position.model.dto.create.CreatePositionReq;
import com.HelloRolha.HR.feature.position.model.dto.create.CreatePositionRes;
import com.HelloRolha.HR.feature.position.model.entity.Position;
import com.HelloRolha.HR.feature.position.repo.PositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PositionServiceTest {
    @Mock
    private PositionRepository positionRepository;
//    @Mock
//    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PositionService positionService;
    @Test
    void create() {
        System.out.println("test");
        Position position = Position.builder()
                .id(1)
                .build();
        given(positionRepository.save(any(Position.class))).willReturn(position);
        CreatePositionReq request = CreatePositionReq.builder()
                .positionNum(123)
                .positionName("테스트")
                .build();
        //when
        CreatePositionRes response = positionService.create(request);
        //then
//        assertNotNull(response.getId());
        assertEquals(response.getId(),1);
    }
}