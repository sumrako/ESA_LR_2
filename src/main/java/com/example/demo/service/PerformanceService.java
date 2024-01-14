package com.example.demo.service;

import com.example.demo.dto.PerformanceDto;

import java.util.List;

public interface PerformanceService {
    void save(PerformanceDto performanceDto);
    void deleteById(Long id);
    void deleteAll();
    PerformanceDto findById(Long id);
    List<PerformanceDto> findAll();

}
