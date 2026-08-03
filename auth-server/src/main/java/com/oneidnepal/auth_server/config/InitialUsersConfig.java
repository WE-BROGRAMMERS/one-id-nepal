package com.oneidnepal.auth_server.config;

import com.oneidnepal.auth_server.entity.User;
import com.oneidnepal.auth_server.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Utsab Dahal
 */
@Configuration
public class InitialUsersConfig {

    @Bean
    public ApplicationRunner initialUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            String rawPassword = "password";
            List<User> users = new ArrayList<>();

            String[] uuids = {
                    "1e7f3c4a-9b5a-4c2f-8123-111111111111",
                    "2a8d4b5c-6c7d-4e8f-9123-222222222222",
                    "3b9e5c6d-7d8e-4f9a-a123-333333333333",
                    "4c0f6d7e-8e9f-40ab-b123-444444444444",
                    "5d1a7e8f-9f0a-41bc-c123-555555555555",
                    "6e2b8f90-0a1b-42cd-d123-666666666666",
                    "7f3c9012-1b2c-43de-e123-777777777777",
                    "809da123-2c3d-44ef-f123-888888888888",
                    "91aeb234-3d4e-4501-0123-999999999999",
                    "a2bfc345-4e5f-4612-1123-aaaaaaaaaaaa",
                    "b3c0d456-5f60-4723-2123-bbbbbbbbbbbb",
                    "c4d1e567-6071-4834-3123-cccccccccccc",
                    "d5e2f678-7182-4945-4123-dddddddddddd",
                    "e6f3a789-8293-4a56-5123-eeeeeeeeeeee",
                    "f704b89a-93a4-4b67-6123-ffffffffffff",
                    "08a5c9ab-a4b5-4c78-7123-111122223333",
                    "19b6da9c-b5c6-4d89-8123-222233334444",
                    "2ac7ebad-c6d7-4e9a-9123-333344445555",
                    "3bd8fcbf-d7e8-40ab-a123-444455556666",
                    "4ce9fdc0-e8f9-41bc-b123-555566667777",
                    // admins
                    "5df0aec1-f901-42cd-c123-666677778888",
                    "6e01bfd2-0a12-43de-d123-777788889999"
            };

            long start = 9818715143L;

            // create 20 citizens with incremental phone numbers
            for (int i = 0; i < 20; i++) {
                String phone = String.valueOf(start + i);
                users.add(User.builder()
                        .id(uuids[i])
                        .phoneNumber(phone)
                        .password(passwordEncoder.encode(rawPassword))
                        .enabled(true)
                        .authorities(new HashSet<>(Set.of("citizen")))
                        .build());
            }

            // create 2 admins continuing the incremental sequence
            for (int i = 20; i < 22; i++) {
                String phone = String.valueOf(start + i);
                users.add(User.builder()
                        .id(uuids[i])
                        .phoneNumber(phone)
                        .password(passwordEncoder.encode(rawPassword))
                        .enabled(true)
                        .authorities(new HashSet<>(Set.of("admin")))
                        .build());
            }

            userRepository.saveAll(users);
        };
    }
}
