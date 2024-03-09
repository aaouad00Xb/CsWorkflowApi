package com.example.workflow.Controllers;

import com.example.workflow.Dto.DashboardBarDto;
import com.example.workflow.Dto.DashboardDto;
import com.example.workflow.Dto.DashboardPoleBarDto;
import com.example.workflow.Dto.EvolutionChartDto;
import com.example.workflow.Helpers.DateHelper;
import com.example.workflow.Services.interfaces.IcontratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Controller")
public class DashboardController {

    @Autowired
    private IcontratService icontratService;
    @Autowired
    private DateHelper dateHelper;




    @GetMapping("/getDashboard")
    public ResponseEntity<?> getFirstElements(){

        try{
            System.out.println("hello");
            int totalActiveContracts = icontratService.getTotalActiveContracts();
            double totalContractValue = icontratService.calculateTotalContractValue();
            long pendingContracts = icontratService.countContractsInStepOne();
            long ongoingContracts = icontratService.countOngoingContracts();
            long completedContracts = icontratService.getTotalActiveContracts();
            long all = icontratService.countAll();

            double averageContractDuration = icontratService.calculateAverageContractTraceabilityDuration();
            double longestContractDuration = icontratService.findLongestContractDuration();
            double shortestContractDuration = icontratService.findShortestContractDuration();
//            DashboardBarDto dashboardBarDto = icontratService.populateLists();
            List<EvolutionChartDto> populateEvolution = icontratService.populateEvolution();
            List<String> Dates =new ArrayList<>();
            int j = 6;
            while(j>=0) {
                LocalDateTime startDate = LocalDateTime.now().minusMonths(j);
                Dates.add(dateHelper.formatLocalDateTime(startDate));
                j--;
            }
            DashboardPoleBarDto dashboardPoleBarDto = icontratService.populatePoleList();


            DashboardDto dashboardDto = DashboardDto.builder()
                    .totalActiveContracts(totalActiveContracts)
                    .total(all)
                    .dates(Dates)
                    .totalContractValue(totalContractValue)
                    .pendingContracts(pendingContracts)
                    .ongoingContracts(ongoingContracts)
                    .completedContracts(completedContracts)
                    .averageContractDuration(averageContractDuration)
                    .longestContractDuration(longestContractDuration)
                    .shortestContractDuration(shortestContractDuration)
//                    .dashboardBarDto(dashboardBarDto)
                    .populateEvolution(populateEvolution)
                    .dashboardPoleBarDto(dashboardPoleBarDto)
                    .build();
            return ResponseEntity.status(200).body(dashboardDto);
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("some thing goes wrong");
        }






    }

}
