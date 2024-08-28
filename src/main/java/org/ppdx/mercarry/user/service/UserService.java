package org.ppdx.mercarry.user.service;

import org.ppdx.mercarry.user.domain.Role;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.repository.RoleRepository;
import org.ppdx.mercarry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerNewUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);

        // Default user role is "USER"
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role \"USER\" not found"));

        user.setRoles(Set.of(role));

        return userRepository.save(user);
    }
}