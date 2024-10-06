package org.ppdx.mercarry.user.service;

import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.user.domain.Role;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.repository.RoleRepository;
import org.ppdx.mercarry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private WalletService walletService;

    @Transactional
    public User registerNewUser(String username, String password) {
        // Check if the username already exists.
        // This check is not working properly in multi-threaded environment because
        // the check is not atomic. To ensure the uniqueness of the username, the
        // username column has unique constraint in the database.
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BusinessException("This username is already taken.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);

        // Default user role is "USER"
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role \"USER\" not found"));

        user.setRoles(Set.of(role));

        userRepository.save(user);
        walletService.create(user);
        return user;
    }

    public void chargeWallet(User user, BigDecimal amount) {
        walletService.topUp(user.getWallet(), amount);
    }
}
