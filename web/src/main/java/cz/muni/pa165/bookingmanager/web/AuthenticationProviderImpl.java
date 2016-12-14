package cz.muni.pa165.bookingmanager.web;

import cz.muni.pa165.bookingmanager.iface.dto.UserDto;
import cz.muni.pa165.bookingmanager.iface.facade.UserFacade;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AuthenticationProviderImpl implements AuthenticationProvider {
    @Inject
    private UserFacade userFacade;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();

        UserDto userDto = userFacade
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));


        String password = (String) authentication.getCredentials();
        boolean success = userFacade.authenticate(email, password);

        if (!success){
            throw new BadCredentialsException("Invalid email or password");
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + userDto.getAccountState());
        return new UsernamePasswordAuthenticationToken(email, password, authorities);
    }

    public static void logInUser(UserDto user, String password){
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + user.getAccountState());

        Authentication auth =
                new UsernamePasswordAuthenticationToken(user.getEmail(), password, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void logout(HttpServletRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, null, auth);
    }

    public static String getLoggedUserEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
