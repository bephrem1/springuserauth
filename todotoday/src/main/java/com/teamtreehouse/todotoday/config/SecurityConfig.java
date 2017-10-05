package com.teamtreehouse.todotoday.config;

import com.teamtreehouse.todotoday.service.UserService;
import com.teamtreehouse.todotoday.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Autowired //Set Spring to user our service as the one for authentication
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder()); //Set password encoder
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ //Create password encoder Bean
        return new BCryptPasswordEncoder(10);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**"); //Bypass security checks for static resources
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests() //Ensures that all requests must be authorized at this point
                .anyRequest().hasRole("USER") //Any user request must have the role "ROLE_USER" (Method prepends "ROLE_")
                .and()                         //Now every request other than the ones to static assets is restricted
            .formLogin()
                .loginPage("/login") //Tell spring where login page is
                .permitAll() //All anyone to see and use the login page
                .successHandler(loginSuccessHandler()) //Handle login success
                .failureHandler(loginFailureHandler()) //Handle login failure
                .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/login") //Could use logoutSuccessHandler if you wanted
                .and()
            .csrf(); //CSRF - Cross Site Request Forgery - Prevents forged 'get' requests on authenticated user by another
                     //site. It creates a randomly generated value and stores it in session data and compares to what's
                     //passed into the form submission.

        //The JSESSIONID holds the information so users don't need to login EVERY request
    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
        return ((request, response, authentication) -> response.sendRedirect("/")); //Success login, redirect to root.
        //This is the one method of AuthenticationSuccessHandler we need to implement so we used a lambda
    }

    private AuthenticationFailureHandler loginFailureHandler() {
        return ((request, response, exception) -> {
            request.getSession().setAttribute("flash", new FlashMessage("Incorrect username and/or password. Try again please.",
                                                                               FlashMessage.Status.FAILURE));
            response.sendRedirect("/login");
        });
    }

    @Bean
    public EvaluationContextExtension securityExtension(){ //This is so we can use principal.id in the TaskDao interface
        return new EvaluationContextExtensionSupport() {
            @Override
            public String getExtensionId() {
                return "security";
            }

            @Override
            public Object getRootObject() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return new SecurityExpressionRoot(authentication) {};
            }
        };
    }

}
