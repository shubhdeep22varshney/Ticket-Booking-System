package com.shubhdeep.ticketbooking.config;

import com.shubhdeep.ticketbooking.entity.AppUser;
import com.shubhdeep.ticketbooking.enums.Role;
import com.shubhdeep.ticketbooking.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (appUserRepository.findByEmail("admin@gmail.com").isEmpty()) {
            AppUser admin = AppUser.builder()
                    .fullName("Admin User")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            appUserRepository.save(admin);
        }

        if (appUserRepository.findByEmail("user@gmail.com").isEmpty()) {
            AppUser user = AppUser.builder()
                    .fullName("Normal User")
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build();
            appUserRepository.save(user);
        }
    }
}