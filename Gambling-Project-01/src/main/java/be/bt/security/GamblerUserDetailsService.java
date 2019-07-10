package be.bt.security;

import be.bt.domain.Gambler;
import be.bt.domain.Role;
import be.bt.repository.GamblerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GamblerUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamblerUserDetailsService.class);

    private GamblerRepository gamblerRepository;

    public GamblerUserDetailsService(GamblerRepository gamblerRepository) {

        this.gamblerRepository = gamblerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Gambler gambler = gamblerRepository.findByUsername(username);
            if (gambler == null) {
                LOGGER.debug("user not found with the provided username");
                return null;
            }
            LOGGER.debug(" user from username " + gambler.toString());
            return new org.springframework.security.core.userdetails.User(gambler.getUsername(), gambler.getPassword(), getAuthorities(gambler));
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Set<GrantedAuthority> getAuthorities(Gambler user) {

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        LOGGER.debug("user authorities are " + authorities.toString());
        return authorities;
    }


}
