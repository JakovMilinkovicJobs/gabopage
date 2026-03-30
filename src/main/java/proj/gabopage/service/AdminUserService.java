package proj.gabopage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import proj.gabopage.model.AdminUser;
import proj.gabopage.repository.AdminUserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AdminUserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserService.class);
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(adminUser.getUsername())
                .password(adminUser.getPassword())
                .roles("ADMIN")
                .build();
    }

    public String generatePasswordResetToken() {
        // Single admin system - get the only admin user
        AdminUser user = adminUserRepository.findByUsername("admin")
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
        adminUserRepository.save(user);

        // Log the reset link to console (works for both dev and production)
        String resetLink = "/gabo-secure-admin-2024/reset-password?token=" + token;
        log.info("==================================================");
        log.info("PASSWORD RESET REQUESTED");
        log.info("Reset Link: {}", resetLink);
        log.info("Token expires at: {}", user.getResetTokenExpiry());
        log.info("==================================================");

        return token;
    }

    public boolean validateResetToken(String token) {
        return adminUserRepository.findByResetToken(token)
                .map(user -> user.getResetTokenExpiry().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    public void resetPassword(String token, String newPassword) {
        AdminUser user = adminUserRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        adminUserRepository.save(user);

        log.info("Password successfully reset for user: {}", user.getUsername());
    }

    public void createAdminIfNotExists(String username, String password) {
        if (adminUserRepository.findByUsername(username).isEmpty()) {
            AdminUser admin = new AdminUser(username, passwordEncoder.encode(password));
            adminUserRepository.save(admin);
            log.info("Admin user created: {}", username);
        }
    }
}
