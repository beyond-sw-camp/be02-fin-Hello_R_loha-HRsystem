package com.HelloRolha.HR.feature.salary.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class GetDatesOfSalaryRes {
    private LocalDate firstDate;
    private LocalDate lastDate;
    private List<SalaryDto> salaryDtoList;
}
