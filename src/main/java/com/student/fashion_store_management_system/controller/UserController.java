package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.dto.authentication.UserUpdateDto;
import com.student.fashion_store_management_system.model.dto.user.UserResponseDto;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import com.student.fashion_store_management_system.service.UserService;
import com.student.fashion_store_management_system.utils.FileUploadUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/fashion-store/users")
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UserController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/my-profile")
    public String getProfile(Model model) {
        User user = authenticationService.getCurrentUser();
        model.addAttribute("user", user);
        return "user-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam("userId") long userId,
                                @RequestParam("avatar") MultipartFile multipartFile,
                                @Valid @ModelAttribute("user") UserUpdateDto userUpdateDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        userUpdateDto.setUserId(userId);

        // Check validation
        if (bindingResult.hasErrors()) {
            User user = authenticationService.getCurrentUser();
            model.addAttribute("user", user);

            // Get all errors from validation and render to UI
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            model.addAttribute("errors", errors);
            return "user-profile";
        }

        // Processing for upload avatar
        String fileName = "";
        if (multipartFile != null && !multipartFile.isEmpty()) {

            // Get file name: ex: avatar.jpg
            fileName = StringUtils.cleanPath(
                    Objects.requireNonNull(multipartFile.getOriginalFilename())
            );

            // Set for user
            userUpdateDto.setAvatarUrl(fileName);
        }

        // Update user profile to database
        UserResponseDto savedUser = userService.updateProfile(userUpdateDto);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            // Create folder for every body
            // ex: images/users/1000
            String uploadDir = "images/users/" + savedUser.getUserId();

            // Save avatar's data
            try {
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Return message after update successfully
        redirectAttributes.addFlashAttribute("successMessage", "Updated Your Profile Successfully!");

        return "redirect:/fashion-store/users/my-profile";
    }

}
