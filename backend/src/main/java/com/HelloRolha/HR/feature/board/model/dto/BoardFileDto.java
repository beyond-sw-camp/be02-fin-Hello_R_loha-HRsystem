package com.HelloRolha.HR.feature.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFileDto {
    private Integer id;
    private String originalFilename;
    private String downloadUrl;
}
