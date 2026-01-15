package fr.eni.cave.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableWebSecurity
public class AppConfigSecurity {

    /**
     * Récupération des utilisateurs de l'application via la base de données
     */
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT login, password, 1 FROM cav_user WHERE login = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT login, authority FROM cav_user WHERE login = ?");
        return jdbcUserDetailsManager;
    }

    /**
     * Restriction des URLs selon la connexion utilisateur et leurs rôles
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> {
           auth.requestMatchers(HttpMethod.GET, "/caveavin/bouteilles").permitAll()

                   .requestMatchers(HttpMethod.GET,"/caveavin/paniers/**").hasAnyRole("CLIENT", "OWNER")

                   .requestMatchers(HttpMethod.POST, "/caveavin/paniers").hasRole("CLIENT")
                   .requestMatchers(HttpMethod.PUT, "/caveavin/paniers").hasRole("CLIENT")

                   .requestMatchers( "/caveavin/bouteilles/**").hasRole("OWNER")
                   /*.requestMatchers(HttpMethod.GET, "/caveavin/bouteilles/**").hasRole("OWNER")
                   .requestMatchers(HttpMethod.POST, "/caveavin/bouteilles").hasRole("OWNER")
                   .requestMatchers(HttpMethod.PUT, "/caveavin/bouteilles").hasRole("OWNER")
                   .requestMatchers(HttpMethod.DELETE, "/caveavin/bouteilles/**").hasRole("OWNER")*/

                   .anyRequest().denyAll();


        });

        // Use Http Basic Authentication
        http.httpBasic(Customizer.withDefaults());

        // Désactivé Cross Site Request Forgery
        http.csrf(csrf -> {
            csrf.disable();
        });

        return http.build();


    }
}
