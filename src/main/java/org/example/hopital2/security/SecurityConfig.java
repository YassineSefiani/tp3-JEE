package org.example.hopital2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
        String encodedPassword = passwordEncoder.encode("1234");
        System.out.println(encodedPassword);
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(encodedPassword).roles("USER").build(),
                User.withUsername("user2").password(encodedPassword).roles("USER").build(),
                User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,RememberMeServices rememberMeServices) throws Exception {
        return httpSecurity
                .formLogin(form -> form
                        .loginPage("/login")        // ➤ URL de la page personnalisée
                        .loginProcessingUrl("/login") // ➤ URL du POST du formulaire (th:action)
                        .defaultSuccessUrl("/", true) // ➤ Redirection après succès
                        .permitAll()                 // ➤ Important pour permettre l'accès à la page login
                )
                .rememberMe(remember -> remember
                        .rememberMeServices(rememberMeServices))
                .authorizeHttpRequests(ar->ar.requestMatchers("/webjars/**","h2-console/**").permitAll())
                .authorizeHttpRequests(ar->ar.requestMatchers("/deletePatient/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
                //.authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
                //.authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .exceptionHandling(exception->
                exception.accessDeniedPage("/notAuthorized"))
                .build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // tokenRepository.setCreateTableOnStartup(true); // À activer une seule fois pour créer la table
        return tokenRepository;
    }

    @Bean
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService,
                                                 PersistentTokenRepository tokenRepository) {
        return new PersistentTokenBasedRememberMeServices(
                "my-remember-me-key", // Clé secrète
                userDetailsService,
                tokenRepository
        );
    }
}
