package br.com.acmeairlines.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://localhost:8081");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user-ms/users/register", "/user-ms/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user-ms/users/validate", "/user-ms/users/check").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user-ms/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/user-ms/users").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/user-ms/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/user-ms/users/cpf/{cpf}").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/user-ms/users/search").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers(HttpMethod.POST, "/user-ms/users/{userId}/role/{roleName}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user-ms/users/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/user-ms/users/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/baggage-ms/baggages").hasAnyRole("ADMIN","BAGGAGE_MANAGER","SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/baggage-ms/baggages").hasAnyRole("ADMIN","BAGGAGE_MANAGER","SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/baggage-ms/baggages/user/{userId}").hasAnyRole("ADMIN","BAGGAGE_MANAGER","SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/baggage-ms/baggages/flight/{flightId}").hasAnyRole("ADMIN","BAGGAGE_MANAGER","SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/baggage-ms/baggages/tag/{tag}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/baggage-ms/baggages/tracker/{trackerUserId}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/baggage-ms/baggages/{baggageId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/baggage-ms/baggages/{baggageId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/baggage-ms/baggages/generate").hasAnyRole("ADMIN","BAGGAGE_MANAGER","SUPPORT")

                        .requestMatchers(HttpMethod.POST, "/flight-ms/flights").hasAnyRole("ADMIN","SUPPORT")
                        .requestMatchers(HttpMethod.PUT, "/flight-ms/flights/{id}").hasAnyRole("ADMIN","SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/flight-ms/flights/id/{id}").hasAnyRole("ADMIN","SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/flight-ms/flights/search").hasAnyRole("ADMIN","SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/flight-ms/flights").hasAnyRole("ADMIN","SUPPORT")
                        .requestMatchers(HttpMethod.DELETE, "/flight-ms/flights/{id}").hasAnyRole("ADMIN","SUPPORT")

                        .requestMatchers(HttpMethod.POST, "/ticket-ms/tickets").hasAnyRole("ADMIN","USER", "SUPPORT")
                        .requestMatchers(HttpMethod.PUT, "/ticket-ms/tickets/{ticketId}").hasAnyRole("ADMIN","USER", "SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/ticket-ms/tickets/{ticketId}").hasAnyRole("ADMIN","USER", "SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/ticket-ms/tickets").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers(HttpMethod.DELETE, "/ticket-ms/tickets/{ticketId}").hasAnyRole("ADMIN", "SUPPORT")
                        .requestMatchers(HttpMethod.POST, "/ticket-ms/tickets/{ticketId}/messages").hasAnyRole("ADMIN","USER", "SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/ticket-ms/tickets/{ticketId}/messages").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ticket-ms/tickets/users/{userId}").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}