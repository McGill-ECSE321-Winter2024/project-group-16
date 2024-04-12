package ca.mcgill.ecse321.SportsSchedulePlus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    public StartupListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            userService.getOwner();
        } catch (Exception e) {
            userService.createOwner();
        }
    }
}