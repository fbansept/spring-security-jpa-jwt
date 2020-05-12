package edu.fbansept.springsecurity.controller;

import edu.fbansept.springsecurity.dao.UtilisateurDao;
import edu.fbansept.springsecurity.model.Role;
import edu.fbansept.springsecurity.model.Utilisateur;
import edu.fbansept.springsecurity.security.JwtUtil;
import edu.fbansept.springsecurity.security.MaConfigurationSecurite;
import edu.fbansept.springsecurity.security.MonUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UtilisateurController {

    private UtilisateurDao utilisateurDao;
    private AuthenticationManager authenticationManager;
    private MonUserDetailsService userDetailsService;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurController(
            UtilisateurDao utilisateurDao,
            AuthenticationManager authenticationManager,
            MonUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.utilisateurDao = utilisateurDao;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
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

    @PutMapping("/inscription")
    public int inscription(@RequestBody Utilisateur utilisateur){
        utilisateur.setActif(true);

        /*Role roleUser = new Role();
        roleUser.setId(1);
        utilisateur.getListeRole().add(roleUser);*/
        ///role: [{id:1}]
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        utilisateurDao.save(utilisateur);
        return utilisateur.getId();
    }

    @GetMapping("/user/utilisateurs")
    public List<Utilisateur> getListeUtilisateur() throws Exception {
        return utilisateurDao.findAll();
    }

    @GetMapping("/user/utilisateur/{$id}")
    public Utilisateur getListeUtilisateur(@PathVariable int id) throws Exception {
        return utilisateurDao.findById(id).orElse(null);
    }

    @PutMapping("/user/utilisateur")
    public int saveUtilisateur(@RequestBody Utilisateur utilisateur){

        Utilisateur user = utilisateurDao.saveAndFlush(utilisateur);
        return utilisateur.getId();
    }
}
