package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GooutListRes {
    private List<GooutList> goouts;
    private long totalElements;
}
