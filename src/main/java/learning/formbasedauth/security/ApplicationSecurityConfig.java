package learning.formbasedauth.security;

import learning.formbasedauth.auth.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(ApplicationUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().
                disable().
                authorizeRequests().
                antMatchers("/ index /css/** /js/**").permitAll().
                anyRequest().
                authenticated().
                and().
                formLogin().
                    loginPage("/login").
                    permitAll().
                    passwordParameter("password").
                    usernameParameter("username").
                    defaultSuccessUrl("/success", true).
                and().
                rememberMe().
                    tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).
                    key("sun").
                    rememberMeParameter("remember-me").
                and().
                logout().
                    logoutUrl("/logout").
                    clearAuthentication(true).
                    invalidateHttpSession(true).
                    deleteCookies("JSESSIONID", "remember-me").
                    logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}
