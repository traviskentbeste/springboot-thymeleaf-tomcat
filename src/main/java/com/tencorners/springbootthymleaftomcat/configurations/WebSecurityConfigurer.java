package com.tencorners.springbootthymleaftomcat.configurations;

import com.tencorners.springbootthymleaftomcat.handlers.CustomAccessDeniedHandler;
import com.tencorners.springbootthymleaftomcat.handlers.CustomAuthenticationFailureHandler;
import com.tencorners.springbootthymleaftomcat.handlers.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("persistentTokenRepository")
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        System.out.println("logoutSuccessHandler getting called");
        return new CustomLogoutSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

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
                    .defaultSuccessUrl("/index")


                .and()

                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .logoutSuccessHandler(logoutSuccessHandler())
                    .permitAll()

                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler())

                .and()

                .rememberMe()
                    .key("secretKey")
                    .tokenValiditySeconds(60)
                    .rememberMeParameter("remember-me")
                    .tokenRepository(persistentTokenRepository).userDetailsService(userDetailsService)

                ;
    }

}