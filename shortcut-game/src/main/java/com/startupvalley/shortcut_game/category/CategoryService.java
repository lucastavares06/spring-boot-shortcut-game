package com.startupvalley.shortcut_game.category;

import com.startupvalley.shortcut_game.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String ERROR_MESSAGE_NOT_FOUND = "Category not found with id: ";

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public String deleteById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(ERROR_MESSAGE_NOT_FOUND + categoryId);
        }

        Category category = findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE_NOT_FOUND + categoryId));

        int affectedGamesCount = category.getGames().size(); // Count the games

        categoryRepository.delete(category); // Delete the category

        return affectedGamesCount + " games were affected and deleted.";
    }

    public String getErrorMessageNotFound(Long categoryId) {
        return ERROR_MESSAGE_NOT_FOUND + categoryId;
    }
}
