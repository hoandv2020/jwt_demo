package com.codegym.jwt.controller;

import com.codegym.jwt.config.JwtProvider;
import com.codegym.jwt.config.JwtResponse;
import com.codegym.jwt.config.ResponseMessage;
import com.codegym.jwt.model.*;
import com.codegym.jwt.repository.RoleRepository;
import com.codegym.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 60)
@RestController
@RequestMapping("/api/auth")
public class AccountRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpForm signUpForm) {
        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(), passwordEncoder.encode(signUpForm.getPassword()));
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN));
        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("Succes!!"), HttpStatus.OK);
    }
}
