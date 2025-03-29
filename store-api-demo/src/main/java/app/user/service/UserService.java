package app.user.service;


import app.exception.DomainException;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;

import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void register(RegisterRequest registerRequest){

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());

        if (optionalUser.isPresent()){
            throw new RuntimeException("User with this email/username already exist.");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .isActive(true)
                .build();

        userRepository.save(user);

    }

    // Test Case: When there is no user in the database (repository returns Optional.empty()) - then expect an exception
    // of type DomainException is thrown
    @CacheEvict(value = "users", allEntries = true)
    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {

        User user = getById(userId);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setEmail(userEditRequest.getEmail());

//        if (!userEditRequest.getEmail().isBlank()) {
//            notificationService.saveNotificationPreference(userId, true, userEditRequest.getEmail());
//        } else {
//            notificationService.saveNotificationPreference(userId, false, null);
//        }

        userRepository.save(user);
    }


    // В началото се изпълнява веднъж този метод и резултата се пази в кеш
    // Всяко следващо извикване на този метод ще се чете резултата от кеша и няма да се извиква четенето от базата
    @Cacheable("users")
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getById(UUID id) {

        Optional<User> user = userRepository.findById(id);

        user.orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));

        return user.get();
    }

    @CacheEvict(value = "users", allEntries = true)
    public void switchStatus(UUID userId) {

        User user = getById(userId);

        // НАЧИН 1:
//        if (user.isActive()){
//            user.setActive(false);
//        } else {
//            user.setActive(true);
//        }

        // false -> true
        // true -> false
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    // If user is ADMIN -> USER
    // If user is USER -> ADMIN
    @CacheEvict(value = "users", allEntries = true)
    public void switchRole(UUID userId) {

        User user = getById(userId);

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with this username does not exist."));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole(), user.isActive());
    }

}
