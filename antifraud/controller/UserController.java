package antifraud.controller;

import antifraud.dto.RoleChangeRequest;
import antifraud.dto.LockOperationRequest;
import antifraud.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import antifraud.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        return userService.deleteUser(username);
    }

    @PutMapping("/role")
    public ResponseEntity<?> changeRole(@Valid @RequestBody RoleChangeRequest request) {
        return userService.changeRole(request);
    }

    @PutMapping("/access")
    public ResponseEntity<?> changeRole(@Valid @RequestBody LockOperationRequest request) {
        return userService.changeLock(request);
    }

}
