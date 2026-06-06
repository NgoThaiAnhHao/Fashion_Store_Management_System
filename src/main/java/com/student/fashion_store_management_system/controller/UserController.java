package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.mapper.UserMapper;
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
        model.addAttribute("user", UserMapper.toResponse(user));
        return "user-profile";
    }

    @GetMapping("/edit/{id}")
    public String getProfileA(@PathVariable long id, Model model) {
        UserResponseDto user = userService.findById(id);
        model.addAttribute("user", user);
        return "/admin/edit-user";
    }

    @PostMapping("/update-profile/{id}")
    public String updateProfile(@PathVariable long id,
                                @RequestParam("avatar") MultipartFile multipartFile,
                                @Valid @ModelAttribute("user") UserUpdateDto userUpdateDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        userUpdateDto.setUserId(id);

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
        redirectAttributes.addFlashAttribute("successMessage", "Updated Profile Successfully!");

        // For Admin
        User currentUser = authenticationService.getCurrentUser();
        if ("ROLE_ADMIN".equals(currentUser.getRoles().getRoleName().toString())) {
            return "redirect:/fashion-store/users/users-management";
        }

        return "redirect:/fashion-store/users/my-profile";
    }

    @GetMapping("/users-management")
    public String getAllUsers(Model model) {
        List<UserResponseDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/user-management";
    }

    @PostMapping("/update-status/{id}")
    public String updateUserStatus(@PathVariable long id) {
        userService.updateStatus(id);
        return "redirect:/fashion-store/users/users-management";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/fashion-store/users/users-management";
    }
}
