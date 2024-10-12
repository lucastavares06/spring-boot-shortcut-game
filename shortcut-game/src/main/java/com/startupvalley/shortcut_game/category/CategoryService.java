package com.startupvalley.shortcut_game.category;

import com.startupvalley.shortcut_game.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String ERROR_MESSAGE_NOT_FOUND = "Category not found with id: ";

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE_NOT_FOUND + id));
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public String deleteById(Long categoryId) {
        Category category = findById(categoryId); // Find the category first

        int affectedGamesCount = category.getGames().size(); // Count the games

        categoryRepository.delete(category); // Delete the category

        return affectedGamesCount + " games were affected and deleted.";
    }

    public String getErrorMessageNotFound(Long categoryId) {
        return ERROR_MESSAGE_NOT_FOUND + categoryId;
    }
}
