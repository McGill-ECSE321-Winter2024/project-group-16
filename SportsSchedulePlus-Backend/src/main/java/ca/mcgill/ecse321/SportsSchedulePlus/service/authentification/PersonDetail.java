package ca.mcgill.ecse321.SportsSchedulePlus.service.authentification;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;

@Service
public class PersonDetail implements UserDetailsService {
@Autowired    
PersonRepository userRepo;

@Override
    public UserDetails loadUserByUsername(String email){
        Person user = userRepo.findPersonByEmail(email);
        if(user==null){
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "User does not exist.");
        }

           
      List<GrantedAuthority> authorities  = new ArrayList <> ();
      authorities.add(new SimpleGrantedAuthority(user.getPersonRole().getClass().getName()));
   
        return new org.springframework.security.core.userdetails.User(email,user.getPassword(),authorities);
       
    }
}
