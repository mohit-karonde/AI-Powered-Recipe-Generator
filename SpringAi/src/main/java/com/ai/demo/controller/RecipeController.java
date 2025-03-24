package com.ai.demo.controller;

import com.ai.demo.entity.Recipe;
import com.ai.demo.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "http://localhost:3000")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    // Generate a recipe
    @GetMapping("/generate")
    public Mono<String> generateRecipe(
            @RequestParam String ingredients,
            @RequestParam String cuisine) {
        return recipeService.generateAndSaveRecipe(ingredients, cuisine);
    }

    // Fetch a recipe by ID
    @GetMapping("/{id}")
    public ResponseEntity<String> getRecipeById(@PathVariable Long id) {
        String recipeText = recipeService.getRecipeById(id);
        if (recipeText != null) {
            return ResponseEntity.ok(recipeText);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}