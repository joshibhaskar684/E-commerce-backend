package service.gateway.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers("/", "/v3/api-docs/**",
                                "/swagger-ui/**","/oauth2/**",
                                "/swagger-ui.html", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .oauth2Login(oauth->
//                        oauth
//
//                                .userInfoEndpoint(
//                                        userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
//                                .successHandler(oAuth2SuccessHandler).failureHandler(oAuth2FailureHandler)
//                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                .cors(withDefaults());

//                .authenticationProvider(authenticationProvider()) ; // ✅ Uses the CorsConfigurationSource bean below

        // Optional: Add JWT or other filters here
        // httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    // ✅ Proper CorsConfigurationSource bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:8080","https://www.vhbuyio.site","http://localhost:3001","https://store.vhbuyio.site","https://vhbuyio.site","https://academy.vhbuyio.site","https://careers.vhbuyio.site","https://templates.vhbuyio.site"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
