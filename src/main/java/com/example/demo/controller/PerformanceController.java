package com.example.demo.controller;

import com.example.demo.dto.PerformanceDto;
import com.example.demo.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceController {
    private final PerformanceService performanceService;

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("performances", performanceService.findAll());
        model.addAttribute("performance", new PerformanceDto());
        return "all-performances";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("performance", performanceService.findById(id));
        return "update-performance";
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable("id") Long id, @ModelAttribute PerformanceDto performanceDto, Model model) {
        performanceDto.setId(id);
        performanceService.save(performanceDto);
        return "redirect:/performance";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        performanceService.deleteById(id);
        return "redirect:/performance";
    }

    @PostMapping("")
    public String create(@ModelAttribute PerformanceDto performanceDto, Model model) {
        performanceService.save(performanceDto);
        return "redirect:/performance";
    }


}
