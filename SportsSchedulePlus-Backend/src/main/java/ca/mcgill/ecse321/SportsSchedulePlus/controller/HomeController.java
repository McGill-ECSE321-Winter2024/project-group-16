package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.authentification.LoginDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.authentification.SignupDto;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;

@RestController
@RequestMapping("/authentication")
public class HomeController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PersonRepository userRepository;
    @Autowired
    private PersonRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
    }
    
    
        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@RequestBody SignupDto signUpDto){
           
            // checking for email exists in a database
            if(userRepository.findPersonByEmail(signUpDto.getEmail()) != null){
                return new ResponseEntity<>("Email already exista!", HttpStatus.BAD_REQUEST);
            }
            // creating user object
            Person user = new Person();
            user.setName(signUpDto.getName());
            user.setEmail(signUpDto.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            PersonRole role = new Owner();
            roleRepository.save(role);
            user.setPersonRole(role);
            userRepository.save(user);
            return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
        }
}