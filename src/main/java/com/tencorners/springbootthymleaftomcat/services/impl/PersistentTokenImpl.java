package com.tencorners.springbootthymleaftomcat.services.impl;

import com.tencorners.springbootthymleaftomcat.entities.RememberMe;
import com.tencorners.springbootthymleaftomcat.repositories.RememberMeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository("persistentTokenRepository")
public class PersistentTokenImpl implements PersistentTokenRepository {

    @Autowired
    private RememberMeRepository rememberMeRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {

        System.out.println("createNewToken");
        System.out.println("getUsername   : " + persistentRememberMeToken.getUsername());
        System.out.println("getSeries     : " + persistentRememberMeToken.getSeries());
        System.out.println("getTokenValue : " + persistentRememberMeToken.getTokenValue());
        System.out.println("getDate       : " + persistentRememberMeToken.getDate());

        RememberMe rememberMe = new RememberMe();
        rememberMe.setUsername(persistentRememberMeToken.getUsername());
        rememberMe.setSeries(persistentRememberMeToken.getSeries());
        rememberMe.setToken(persistentRememberMeToken.getTokenValue());
        rememberMe.setLastUsed(persistentRememberMeToken.getDate());
        rememberMeRepository.save(rememberMe);

        System.out.println("persistentRememberMeToken : " + persistentRememberMeToken);
        System.out.println("rememberMe                : " + rememberMe);

    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {

        System.out.println("getTokenForSeries : " + series);

        Optional<RememberMe> rememberMeOptional = rememberMeRepository.getBySeries(series);
        if (rememberMeOptional.isPresent()) {
            RememberMe rememberMe = rememberMeOptional.get();

            return new PersistentRememberMeToken(
                    rememberMe.getUsername(),
                    rememberMe.getSeries(),
                    rememberMe.getToken(),
                    rememberMe.getLastUsed()
            );

        }

        return null;

    }

    @Override
    public void removeUserTokens(String username) {

        System.out.println("removeUserTokens : " + username);

        List<RememberMe> rememberMeList = rememberMeRepository.getAllByUsername(username);
        for (RememberMe rememberMe : rememberMeList) {
            rememberMeRepository.delete(rememberMe);
        }

    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {

        System.out.println("updateToken for series : " + series + " new values, token=" + tokenValue + " lastUsed=" + lastUsed);

        Optional<RememberMe> rememberMeOptional = rememberMeRepository.getBySeries(series);
        if (rememberMeOptional.isPresent()) {
            RememberMe rememberMe = rememberMeOptional.get();
            rememberMe.setToken(tokenValue);
            rememberMe.setLastUsed(lastUsed);
            rememberMeRepository.save(rememberMe);
        }

    }

}
