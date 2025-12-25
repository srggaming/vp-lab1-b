package mk.ukim.finki.wp.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Public access - read-only pages
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/dishes")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/chefs")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/listChefs")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/dish")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/chefDetails")).permitAll()
                        // Admin only - forms and modification endpoints
                        .requestMatchers(new AntPathRequestMatcher("/dishes/dish-form/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/dishes/add")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/dishes/edit/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/dishes/delete/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/chefs/chef-form/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/chefs/add")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/chefs/edit/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/chefs/delete/**")).hasRole("ADMIN")
                        // Static resources
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                        // H2 Console
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/dishes", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/dishes")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                );

        return http.build();
    }
}
