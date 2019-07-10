package be.bt.service;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

}
