package project.controller.services;

import project.controller.repository.UserRepository;
import project.controller.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        if (!repository.findByEmail(user.getEmail()).isEmpty()) {
            throw new RuntimeException("Email already registered");
        }
        return repository.save(user);
    }

    public User update(User newUser) {
        User oldUser = repository.findById(newUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }

        if (newUser.getEmail() != null) {
            if (!newUser.getEmail().equals(oldUser.getEmail())) {
                if (repository.findByEmail(newUser.getEmail()).isPresent()) {
                    throw new RuntimeException("This email is already in use by another user");
                }
            }
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getType() != null) {
            oldUser.setType(newUser.getType());
        }

        return repository.save(oldUser);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        repository.deleteById(id);
    }
}