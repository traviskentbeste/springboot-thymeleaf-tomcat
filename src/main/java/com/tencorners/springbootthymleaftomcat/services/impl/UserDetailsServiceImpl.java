package com.tencorners.springbootthymleaftomcat.services.impl;

import com.tencorners.springbootthymleaftomcat.entities.Role;
import com.tencorners.springbootthymleaftomcat.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        System.out.println("'" + userName + "' is attempting to login");

        Optional<com.tencorners.springbootthymleaftomcat.entities.User> optionalUser = usersRepository.findByUsername(userName);

        if(optionalUser.isPresent()) {
            com.tencorners.springbootthymleaftomcat.entities.User user = optionalUser.get();

            System.out.println("'" + userName + "' is found in db");

            List<String> roleList = new ArrayList<String>();
            for(Role role:user.getRoles()) {
                roleList.add(role.getName());
            }

            System.out.println("roleList            : " + roleList);
            System.out.println("disabled            : " + user.isDisabled());
            System.out.println("account expired     : " + user.isAccountExpired());
            System.out.println("account locked      : " + user.isAccountLocked());
            System.out.println("credentials expired : " + user.isCredentialsExpired());

            org.springframework.security.core.userdetails.UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .disabled(user.isDisabled())
                    .accountExpired(user.isAccountExpired())
                    .accountLocked(user.isAccountLocked())
                    .credentialsExpired(user.isCredentialsExpired())
                    .roles(roleList.toArray(new String[0]))
                    .build();

            System.out.println("userDetails         : " + userDetails);

            return userDetails;

        } else {

            System.out.println("'" + userName + "' is not found in db");

            throw new UsernameNotFoundException("User Name is not Found");
        }
    }

}
