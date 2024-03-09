package com.example.workflow.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardDto {
    private int totalActiveContracts;
    private double totalContractValue;


    private long pendingContracts;
    private long ongoingContracts;
    private long completedContracts;
    private long total;


    private double averageContractDuration;
    private double longestContractDuration;
    private double shortestContractDuration;
    private DashboardBarDto dashboardBarDto;
    private List<EvolutionChartDto> populateEvolution;
    private List<String> dates;
    private DashboardPoleBarDto dashboardPoleBarDto;
}
