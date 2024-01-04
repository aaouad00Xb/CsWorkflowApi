package com.example.workflow.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardBarDto {

    List<Integer> contractCounts = new ArrayList<>();
    List<Integer> restartedContractsCount = new ArrayList<>();
    List<Integer> validatedContractsCount = new ArrayList<>();
    List<String> divisionNames = new ArrayList<>();

}
