package com.startupvalley.shortcut_game.game;

import com.startupvalley.shortcut_game.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    private static final String ERROR_MESSAGE_NOT_FOUND = "Game not found with id: ";

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }

    public void save(Game game) {
        gameRepository.save(game);
    }

    public void deleteById(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException(ERROR_MESSAGE_NOT_FOUND + id);
        }
        gameRepository.deleteById(id);
    }

    public String getErrorMessageNotFound(Long id) {
        return ERROR_MESSAGE_NOT_FOUND + id;
    }
}
