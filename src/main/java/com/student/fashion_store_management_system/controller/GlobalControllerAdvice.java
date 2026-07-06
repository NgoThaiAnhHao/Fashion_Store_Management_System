package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import com.student.fashion_store_management_system.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@AllArgsConstructor
public class GlobalControllerAdvice {

    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        return authenticationService.getCurrentUser();
    }

    @ModelAttribute("unreadNotificationCount")
    public long getUnreadNotificationCount() {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser != null) {
            return notificationService.getUnreadNotificationCount(currentUser);
        }
        return 0;
    }
}
