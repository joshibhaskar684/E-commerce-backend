package app.auth.service.Security;

import app.auth.service.Service.CustomOAuth2UserService;
import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableMethodSecurity
@EnableWebSecurity
@Builder
@Configuration
public class SecurityConfiguration  {

    private final UserDetailsService userDetailsService;
    private JwtRequestFilter jwtRequestFilter;
private OAuth2FailureHandler oAuth2FailureHandler;
private OAuth2SuccessHandler oAuth2SuccessHandler;
private CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfiguration(UserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter, OAuth2FailureHandler oAuth2FailureHandler, OAuth2SuccessHandler oAuth2SuccessHandler, CustomOAuth2UserService customOAuth2UserService) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/v3/api-docs/**",
                                "/swagger-ui/**","/oauth2/**",
                                "/swagger-ui.html", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth->
                    oauth

                            .userInfoEndpoint(
                                    userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
                            .successHandler(oAuth2SuccessHandler).failureHandler(oAuth2FailureHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                .cors(withDefaults())

                .authenticationProvider(authenticationProvider()) ; // ✅ Uses the CorsConfigurationSource bean below

        // Optional: Add JWT or other filters here
        // httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    // ✅ Proper CorsConfigurationSource bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:1000","http://localhost:8080","https://www.vhbuyio.site","http://localhost:3001","https://store.vhbuyio.site","https://vhbuyio.site","https://academy.vhbuyio.site","https://careers.vhbuyio.site","https://templates.vhbuyio.site"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }

}
