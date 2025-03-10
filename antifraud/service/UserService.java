package antifraud.service;

import antifraud.dto.RoleChangeRequest;
import antifraud.dto.LockOperationRequest;
import antifraud.dto.UserDTO;
import antifraud.model.AppUser;
import antifraud.model.AppUserDetails;
import antifraud.repository.UserRepository;
import antifraud.enums.AppRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.userRepo = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<?> createUser(UserDTO userDTO) {
        if (userRepo.existsByUserDetailsUsernameIgnoreCase(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        AppUser user = userDTO.toAppUser();
        AppUserDetails userDetails = user.getUserDetails();
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        if (userRepo.count() == 0) {
            userDetails.setRole(AppRole.ADMINISTRATOR);
            userDetails.setNonLocked(true);
        }
        user = userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO.of(user));
    }

    @Transactional
    public ResponseEntity<?> deleteUser(String username) {
        if (!userRepo.existsByUserDetailsUsernameIgnoreCase(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userRepo.deleteByUserDetailsUsername(username);
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("status", "Deleted successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<?> changeRole(RoleChangeRequest roleChangeRequest) {
        if (roleChangeRequest.isAdmininistrator()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot change ADMIN role");
        }
        if (!userRepo.existsByUserDetailsUsernameIgnoreCase(roleChangeRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        AppUser user = userRepo.findByUserDetailsUsernameIgnoreCase(roleChangeRequest.getUsername()).get();
        if (user.getUserDetails().getRole().equals(roleChangeRequest.getRole())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Role not changed");
        }
        user.getUserDetails().setRole(roleChangeRequest.getRole());
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(UserDTO.of(user));
    }

    public ResponseEntity<?> changeLock(LockOperationRequest lockOperationRequest) {
        String username = lockOperationRequest.getUsername();
        if (!userRepo.existsByUserDetailsUsernameIgnoreCase(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        AppUser user = userRepo.findByUserDetailsUsernameIgnoreCase(username).get();
        boolean isUnlocked = lockOperationRequest.getOperation().getValue();
        user.getUserDetails().setNonLocked(isUnlocked);
        userRepo.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("status", "User %s %s!".formatted(username, isUnlocked ? "unlocked" : "locked"));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAllByOrderById().stream()
                .map(UserDTO::of)
                .toList();
    }

}
