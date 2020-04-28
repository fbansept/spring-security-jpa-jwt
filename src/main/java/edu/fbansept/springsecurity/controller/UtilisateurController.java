package edu.fbansept.springsecurity.controller;

import edu.fbansept.springsecurity.dao.UtilisateurDao;
import edu.fbansept.springsecurity.model.Utilisateur;
import edu.fbansept.springsecurity.security.JwtUtil;
import edu.fbansept.springsecurity.security.MonUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UtilisateurController {

    private UtilisateurDao utilisateurDao;
    private AuthenticationManager authenticationManager;
    private MonUserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    @Autowired
    public UtilisateurController(
            UtilisateurDao utilisateurDao,
            AuthenticationManager authenticationManager,
            MonUserDetailsService userDetailsService,
            JwtUtil jwtUtil) {
        this.utilisateurDao = utilisateurDao;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authentification")
    public ResponseEntity<String> authentification(@RequestBody Utilisateur utilisateur) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            utilisateur.getPseudo(), utilisateur.getPassword()));
        }
        catch (BadCredentialsException e) {
            throw new Exception("Pseudo ou mot de passe incorrect", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(utilisateur.getPseudo());

        return ResponseEntity.ok(jwtUtil.generateToken(userDetails));
    }

    @GetMapping("/user/utilisateurs")
    public List<Utilisateur> getListeUtilisateur() throws Exception {
        return utilisateurDao.findAll();
    }
}
