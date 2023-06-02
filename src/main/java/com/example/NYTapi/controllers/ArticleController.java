package com.example.NYTapi.controllers;

import com.example.NYTapi.models.Doc;
import com.example.NYTapi.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class ArticleController {


    @Autowired
    ArticleService articleService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("articleList", articleService.getMostPopular());
        return "index";
    }



    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }

    @PostMapping("/search")
    public String performSearch(@RequestParam("searchText") String searchText, Model model) {
        List<Doc> searchResults = articleService.getSearchResults(searchText);
        model.addAttribute("searchResults", searchResults);
        return "search-results";
    }
}