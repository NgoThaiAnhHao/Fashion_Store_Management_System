package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.enums.RoleNameEnum;
import com.student.fashion_store_management_system.mapper.UserMapper;
import com.student.fashion_store_management_system.model.dto.authentication.UserUpdateDto;
import com.student.fashion_store_management_system.model.dto.user.UserResponseDto;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import com.student.fashion_store_management_system.service.UserService;
import com.student.fashion_store_management_system.utils.FileUploadUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("/fashion-store")
@AllArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping("/users/my-profile")
    public String getProfile(Model model) {
        User user = authenticationService.getCurrentUser();
        model.addAttribute("user", UserMapper.toResponse(user));
        return "user-profile";
    }

    @GetMapping("/users/edit/{id}")
    public String getProfileById(@PathVariable long id, Model model) {
        UserResponseDto user = userService.findById(id);
        model.addAttribute("user", user);
        return "/admin/user/edit-user";
    }

    @PostMapping("/users/update-profile/{id}")
    public String updateProfile(@PathVariable long id,
                                @RequestParam("avatar") MultipartFile multipartFile,
                                @RequestParam(required = false) RoleNameEnum role,
                                @Valid @ModelAttribute("user") UserUpdateDto userUpdateDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        userUpdateDto.setUserId(id);

        // Check validation
        if (bindingResult.hasErrors()) {
            User user = authenticationService.getCurrentUser();
            model.addAttribute("user", UserMapper.toResponse(user));

            // Get all errors from validation and render to UI
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);

            // For admin
            if (isAdmin()) {
                return "admin/user/edit-user";
            }

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

        UserResponseDto savedUser;

        // Processing for role (User updated by admin)
        if (isAdmin()) {
            savedUser = userService.updateProfile(userUpdateDto, role);
        } else {
            // Update user profile to database
            savedUser = userService.updateProfile(userUpdateDto, RoleNameEnum.ROLE_CUSTOMER);
        }

        if (multipartFile != null && !multipartFile.isEmpty()) {
            // Create folder for every body
            // ex: uploads/users/1000
            String uploadDir = "uploads/users/" + savedUser.getUserId();

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
        if (isAdmin()) {
            return "redirect:/fashion-store/users/users-management";
        }

        return "redirect:/fashion-store/users/my-profile";
    }

    @GetMapping("/users/users-management")
    public String getAllUsers(Model model) {
        List<UserResponseDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/user/user-management";
    }

    @GetMapping("/users/users-management/{roleName}")
    public String getAllUsersByRole(@PathVariable RoleNameEnum roleName,
                                    Model model) {
        List<UserResponseDto> users = userService.findAllByRole(roleName);
        model.addAttribute("users", users);
        return "admin/user/user-management";
    }

    @PostMapping("/users/update-status/{id}")
    public String updateUserStatus(@PathVariable long id) {
        userService.updateStatus(id);
        return "redirect:/fashion-store/users/users-management";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Deleted Success!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete current user, or user is admin, or user have FK!");
        }

        return "redirect:/fashion-store/users/users-management";
    }

    @GetMapping("/users/search-by-name")
    public String searchByName(@RequestParam String keyword, Model model) {
        List<UserResponseDto> users = userService.findAllByFullName(keyword);
        model.addAttribute("users", users);
        return "admin/user/user-management";
    }

    private boolean isAdmin() {
        User currentUser = authenticationService.getCurrentUser();
            return "ROLE_ADMIN".equals(
                    currentUser.getRoles().getRoleName().toString()
            );
    }
}
