package com.example.firstTry.touur;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/time")
public class TimeExtractorController {

    private final TimeExtractorService timeExtractorService;

    public TimeExtractorController(TimeExtractorService timeExtractorService) {
        this.timeExtractorService = timeExtractorService;
    }

    @PostMapping("/extract")
    public List<TimeExpressionResult> extractTimeExpressions(
            @RequestBody TimeExtractionRequest request) {
        return timeExtractorService.extractTimeExpressions(
                request.text()

        );
    }

    record TimeExtractionRequest(String text) {}
}