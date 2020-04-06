package edu.fbansept.springsecurity.security;

import edu.fbansept.springsecurity.dao.UtilisateurDao;
import edu.fbansept.springsecurity.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MonUserDetailsService implements UserDetailsService {

    @Autowired
    UtilisateurDao utilisateurDao;

    @Override
    public MonUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurDao.findByPseudo(userName)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Inconnu : " + userName));

        return new MonUserDetails(utilisateur);
    }
}