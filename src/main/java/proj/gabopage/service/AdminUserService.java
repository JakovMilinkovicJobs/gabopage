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

    public void createAdminIfNotExists(String username, String password) {
        if (adminUserRepository.findByUsername(username).isEmpty()) {
            AdminUser admin = new AdminUser(username, passwordEncoder.encode(password));
            adminUserRepository.save(admin);
            log.info("Admin user created: {}", username);
        }
    }
}
