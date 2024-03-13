package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.authentification.LoginRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.authentification.SignupRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.service.CustomerService;


@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerService customerService;
    

    
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequestDTO loginDto) {
        Authentication authentication;
        try{
         authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
        }
        catch (AuthenticationException e) {
            if(e.getMessage().equals("User does not exist.")){
            // Incorrect password
            return new ResponseEntity<>("User with email "+ loginDto.getEmail()+" does not exist.", HttpStatus.UNAUTHORIZED);
            }
            else if(e.getMessage().equals("Bad credentials")){
            // Incorrect password
            return new ResponseEntity<>("Incorrect password, please try again.", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("Authentication error.", HttpStatus.UNAUTHORIZED);
    }
    
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDTO signUpDto){
        customerService.createCustomer(signUpDto.getName(), signUpDto.getEmail(), signUpDto.getPassword());
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }
}