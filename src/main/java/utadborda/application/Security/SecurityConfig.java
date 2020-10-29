package utadborda.application.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import utadborda.application.Entities.User;
import utadborda.application.web.requestMappings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception{
        auth
            .inMemoryAuthentication();
        auth
            .jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery(
                User.SPRING_SECURITY_USERNAME_QUERY
            )
            .authoritiesByUsernameQuery(
                User.SPRING_SECURITY_AUTHORITIES_BY_USERNAME_QUERY
            )
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .formLogin()
                .loginPage(requestMappings.LOGIN)
                .defaultSuccessUrl(requestMappings.LOGIN_SUCCESS)
                .successHandler(new LoginSuccessHandler(requestMappings.LOGIN_SUCCESS))
                .failureUrl(requestMappings.LOGIN_RETRY)
                .failureHandler(new LoginFailureHandler(requestMappings.LOGIN_RETRY))
            .and()
                .logout()
                .logoutRequestMatcher(
                        new AntPathRequestMatcher(requestMappings.LOGOUT))
                // required because the logout request is a
                // POST when anti-CSRF is enabled
                .logoutSuccessUrl(requestMappings.LOGOUT_SUCCESS)
            .and()
                .headers()
                .frameOptions()
                .sameOrigin();
        http
            .csrf()
                .disable();
        http
            .authorizeRequests()
                .antMatchers(requestMappings.ADMIN + "/**").hasAuthority("ADMIN")
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            //.and()
            //    .rememberMe()
            //        .tokenValiditySeconds(10)
            //        .key("ut_ad_borda")
            .and()
                .httpBasic();
    }

    private static class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
        LoginFailureHandler(String defaultTarget) {
            setDefaultFailureUrl(defaultTarget);
        }


        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException e)
                throws IOException, ServletException
        {
            HttpSession session = request.getSession();
            if(session != null){
                session.setAttribute("referer", request.getHeader("referer"));
            }
            super.onAuthenticationFailure(request, response, e);
        }
    }

    private static class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
        LoginSuccessHandler(String defaultTarget){
            setDefaultTargetUrl(defaultTarget);
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication)
                throws ServletException, IOException
        {
            HttpSession session = request.getSession();
            if(session != null){
                session.setAttribute("referer", request.getHeader("referer"));
            }
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    @Bean(name = "iolAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
