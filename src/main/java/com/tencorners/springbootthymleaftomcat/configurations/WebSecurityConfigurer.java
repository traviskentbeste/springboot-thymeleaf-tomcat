package com.tencorners.springbootthymleaftomcat.configurations;

import com.tencorners.springbootthymleaftomcat.handlers.CustomAccessDeniedHandler;
import com.tencorners.springbootthymleaftomcat.handlers.CustomAuthenticationFailureHandler;
import com.tencorners.springbootthymleaftomcat.handlers.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Value("${hostname}")
    private String hostname;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        // user role
        UserDetails user = User.withUsername("user")
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .password("user").roles("USER").build();

        // admin role
        UserDetails admin = User.withUsername("admin")
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .password("admin").roles("ADMIN", "USER").build();

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(user);
        userDetailsManager.createUser(admin);

        return userDetailsManager;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        System.out.println("hostname is " + hostname);
        
        http
                .csrf()
                    .disable()

                .authorizeRequests()

                    .antMatchers("/", "/index", "/bootstrap/**", "/jquery/**").permitAll()
                    .antMatchers("/authentication/user").hasAnyRole("ADMIN", "USER")
                    .antMatchers("/authentication/admin").hasRole("ADMIN")

                .anyRequest()
                    .authenticated()

                .and()

                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .failureHandler(customAuthenticationFailureHandler)
                    .defaultSuccessUrl("http://" + hostname + "/index")


                .and()

                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/")
                    .logoutSuccessHandler(logoutSuccessHandler())
                    .permitAll()

                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler())

                ;
    }
}
