package racegame.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import racegame.service.UserDetailService;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter
{
    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JwtRequestFilters jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/lobby-websocket").permitAll()
                .antMatchers("/lobby.createLobby").permitAll()
                .antMatchers("/app/lobby.createLobby").permitAll()
                .antMatchers("/lobby.refreshLobby").permitAll()
                .antMatchers("/app/lobby.refreshLobby").permitAll()
                .antMatchers("/lobby.joinLobby").permitAll()
                .antMatchers("/app/lobby.joinLobby").permitAll()
                .antMatchers("app").permitAll()
                .antMatchers("/account/authenticate").permitAll()
                .antMatchers("/account/register").permitAll()
                .antMatchers("/hi").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }
}
