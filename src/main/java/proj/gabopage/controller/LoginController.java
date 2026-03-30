package proj.gabopage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proj.gabopage.service.AdminUserService;

@Controller
public class LoginController {

    private final AdminUserService adminUserService;

    public LoginController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/login/forgot-password")
    public String forgotPasswordForm() {
        return "admin/forgot-password";
    }

    @PostMapping("/login/forgot-password")
    public String forgotPassword(RedirectAttributes redirectAttributes) {
        try {
            adminUserService.generatePasswordResetToken();
            redirectAttributes.addFlashAttribute("message",
                "Password reset instructions have been logged to the console. Check your server logs for the reset link.");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                "Unable to process password reset request.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/login/forgot-password";
    }

    @GetMapping("/login/reset-password")
    public String resetPasswordForm(@RequestParam String token, Model model) {
        if (!adminUserService.validateResetToken(token)) {
            model.addAttribute("error", "Invalid or expired reset token.");
            return "admin/reset-password-error";
        }
        model.addAttribute("token", token);
        return "admin/reset-password";
    }

    @PostMapping("/login/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/login/reset-password?token=" + token;
        }

        try {
            adminUserService.resetPassword(token, password);
            redirectAttributes.addFlashAttribute("message",
                "Password has been reset successfully. You can now login with your new password.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login/reset-password?token=" + token;
        }
    }
}