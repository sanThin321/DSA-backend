package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportsController {

    @Autowired
    private ReportService reportService;
}
