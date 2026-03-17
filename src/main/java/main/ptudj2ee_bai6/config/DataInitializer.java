package main.ptudj2ee_bai6.config;

import main.ptudj2ee_bai6.model.Account;
import main.ptudj2ee_bai6.model.Role;
import main.ptudj2ee_bai6.repository.AccountRepository;
import main.ptudj2ee_bai6.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Ensure roles
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_ADMIN");
            return roleRepository.save(r);
        });

        Role roleUser = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            return roleRepository.save(r);
        });

        // Ensure user account
        Optional<Account> userOpt = accountRepository.findByLoginName("user");
        if (userOpt.isEmpty()) {
            Account user = new Account();
            user.setLoginName("user");
            user.setPassword(passwordEncoder.encode("123456"));
            // assign ROLE_USER
            user.setRoles(new HashSet<>());
            user.getRoles().add(roleUser);
            accountRepository.save(user);
            System.out.println("Created user: user (ROLE_USER)");
        } else {
            System.out.println("User 'user' already exists");
        }

        // Ensure admin account
        Optional<Account> adminOpt = accountRepository.findByLoginName("admin");
        if (adminOpt.isEmpty()) {
            Account admin = new Account();
            admin.setLoginName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            // assign ROLE_ADMIN
            admin.setRoles(new HashSet<>());
            admin.getRoles().add(roleAdmin);
            accountRepository.save(admin);
            System.out.println("Created user: admin (ROLE_ADMIN)");
        } else {
            System.out.println("User 'admin' already exists");
        }

    }
}
