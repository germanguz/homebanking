package com.ap.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    protected SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    //.antMatchers(HttpMethod.POST,"/**").permitAll();  //solo para pruebas, habilita totalidad
                // a lo que puede acceder cualquier usuario, para registrarse o iniciar sesión
                .antMatchers("/web/index.html", "/web/js/index.js", "/web/css/style.css", "/web/img/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login", "/api/logout").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()

                // a lo que puede acceder solo admin
                .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/manager.html", "manager.js").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")

                //lo correcto, creo, sería /api/accounts/** para ADMIN pero, el cliente no podría ver sus cuentas
                .antMatchers("/api/accounts").hasAuthority("ADMIN")

                // acceso tanto para admin como para cliente
                //si saco /api/accounts/** entonces cliente no podría ver sus cuentas porque se ven con id y no con current
                .antMatchers("/api/clients/current/**", "/web/**", "/api/accounts/**", "/api/loans").hasAnyAuthority("ADMIN", "CLIENT")
                // task7
                .antMatchers(HttpMethod.POST, "/api/transactions", "/api/loans").hasAnyAuthority("ADMIN", "CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAnyAuthority("ADMIN", "CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAnyAuthority("ADMIN", "CLIENT")

                // solo para admin
                .antMatchers("/api/clients/**").hasAuthority("ADMIN")

                //para bloquear en totalidad lo que no está especificado
                .anyRequest().denyAll();

                //otra forma de bloqueo pero en este caso para quien no esté autenticado
                //.anyRequest().authenticated();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");


        // turn off checking for CSRF tokens
        http.csrf().disable();
        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();
        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
