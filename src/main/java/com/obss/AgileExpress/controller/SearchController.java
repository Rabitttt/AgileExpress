package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.entity.Project;
import com.obss.AgileExpress.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/{searchText}")
    public Project getSearchedProject(@PathVariable(value = "searchText") String searchText) {
        return searchService.getSearchedProject(searchText);
    }
}
