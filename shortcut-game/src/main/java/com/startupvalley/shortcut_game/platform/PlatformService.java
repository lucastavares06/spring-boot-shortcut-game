package com.startupvalley.shortcut_game.platform;

import com.startupvalley.shortcut_game.exception.ResourceNotFoundException;
import com.startupvalley.shortcut_game.exception.ResourceDependencyException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    private static final String ERROR_MESSAGE_NOT_FOUND = "Platform not found with id: ";
    private static final String ERROR_MESSAGE_DEPENDENCY = "Platform cannot be deleted because it is associated with existing games.";

    public List<Platform> findAll() {
        return platformRepository.findAll();
    }

    public Optional<Platform> findById(Long id) {
        return platformRepository.findById(id);
    }

    public void save(Platform platform) {
        platformRepository.save(platform);
    }

    @Transactional
    public void deleteById(Long platformId) {
        Platform platform = platformRepository.findById(platformId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE_NOT_FOUND + platformId));

        if (!platform.getGames().isEmpty()) {
            throw new ResourceDependencyException(ERROR_MESSAGE_DEPENDENCY);
        }

        platformRepository.delete(platform);
    }

    public String getErrorMessageNotFound(Long platformId) {
        return ERROR_MESSAGE_NOT_FOUND + platformId;
    }

    public String getErrorMessageDependency() {
        return ERROR_MESSAGE_DEPENDENCY;
    }
}
