package com.startupvalley.shortcut_game.util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class FlashMessageUtil {

    public static final String SUCCESS_MESSAGE = "successMessage";
    public static final String ERROR_MESSAGE = "errorMessage";

    public static void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, message);
    }

    public static void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, message);
    }
}
