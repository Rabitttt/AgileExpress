package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.documents.ElasticSearch.UserES;
import com.obss.AgileExpress.documents.Project;
import com.obss.AgileExpress.domain.SearchResultDao;
import com.obss.AgileExpress.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/{searchText}")
    public SearchResultDao getSearchedProject(@PathVariable(value = "searchText") String searchText) {
        return searchService.search(searchText);
    }
}
