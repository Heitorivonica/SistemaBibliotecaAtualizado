package project.controller;

import org.springframework.web.bind.annotation.*;
import project.controller.dto.Create.CreateUserRequest;
import project.controller.dto.Update.UpdateUserRequest;
import project.controller.dto.UserDTO;
import project.controller.model.User;
import project.controller.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDTO getMensager(@PathVariable Long id) {
        User user = userService.findById(id);
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getType(),
                user.isBlocked()
        );
    }

    @GetMapping
    public List<UserDTO> getAllUser() {
        List<User> users = userService.findAll();
        return users.stream().map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getType(),
                        user.isBlocked()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserDTO createUser(@RequestBody CreateUserRequest request) {
        User createrUser = new User();
        createrUser.setName(request.getName());
        createrUser.setEmail(request.getEmail());
        createrUser.setType(request.getType());
        createrUser.setBlocked(false);

        User savedUser = userService.save(createrUser);

        return new UserDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getType(),
                savedUser.isBlocked()
        );
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        User existingUser = userService.findById(id);
        String oldEmail = existingUser.getEmail();
        String newEmail = request.getEmail();

        if (newEmail != null && !newEmail.equals(oldEmail)) {
            if (userService.findByEmail(newEmail).isPresent()) {
                throw new RuntimeException("This email is already in use by another user");
            }
        }

        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getType() != null) {
            existingUser.setType(request.getType());
        }
        if (request.getBlocked() != null) {
            existingUser.setBlocked(request.getBlocked());
        }

        User updateUser = userService.update(existingUser);

        return new UserDTO(
                updateUser.getId(),
                updateUser.getName(),
                updateUser.getEmail(),
                updateUser.getType(),
                updateUser.isBlocked()
        );

        }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        userService.delete(id);

    }
}