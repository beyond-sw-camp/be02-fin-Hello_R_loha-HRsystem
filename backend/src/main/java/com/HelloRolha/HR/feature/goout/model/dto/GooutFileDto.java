package com.HelloRolha.HR.feature.goout.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GooutFileDto {
    private String name;
    private String downloadUrl;
}
