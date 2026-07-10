package com.oneidnepal.auth_server.config;

import com.oneidnepal.auth_server.entity.User;
import com.oneidnepal.auth_server.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JpaUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Collection<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getPhoneNumber())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.isEnabled())
                .build();
    }

    @Override
    public void createUser(UserDetails userDetails) {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .phoneNumber(userDetails.getUsername())
                .password(userDetails.getPassword())
                .enabled(true)
                .authorities(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .build();
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails user) {}

    @Override
    public void deleteUser(String username) {}

    @Override
    public void changePassword(String oldPassword, String newPassword) {}

    @Override
    public boolean userExists(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}