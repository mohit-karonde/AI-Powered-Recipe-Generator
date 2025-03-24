package com.ai.demo.service;
import com.ai.demo.client.GeminiClient;
import com.ai.demo.entity.Recipe;
import com.ai.demo.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class RecipeService {

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private RecipeRepository recipeRepository;

    private static final String FILE_PATH = "D:\\recipedb";

    public Mono<String> generateAndSaveRecipe(String ingredients, String cuisine) {
        return geminiClient.generateRecipe(ingredients, cuisine)
                .doOnNext(recipeText -> {
                    String fileName = "recipe_" + System.currentTimeMillis() + ".txt";
                    saveRecipeToFile(fileName, recipeText); // Save to file

                    Recipe recipe = new Recipe();
                    recipe.setTitle("Generated Recipe");
                    recipe.setCuisine(cuisine);
                    recipe.setIngredients(ingredients);
                    recipe.setInstructions(fileName); // Store file path instead of full text

                    recipeRepository.save(recipe);
                });
    }

    private void saveRecipeToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(FILE_PATH + fileName)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fetch and load recipe from file
    public String getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    try {
                        String filePath = FILE_PATH + recipe.getInstructions();
                        return new String(Files.readAllBytes(Paths.get(filePath)));
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read recipe file");
                    }
                })
                .orElse(null);
    }
}
