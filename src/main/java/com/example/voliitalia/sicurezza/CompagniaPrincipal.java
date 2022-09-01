package com.example.voliitalia.sicurezza;

import com.example.voliitalia.entità.CompagniaAerea;
import com.example.voliitalia.entità.Utente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompagniaPrincipal implements UserDetails {
    private CompagniaAerea compagniaAerea;

    public CompagniaPrincipal(CompagniaAerea c){
        compagniaAerea =c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list= new ArrayList<>();
        String ruolo = "airline";
        list.add(new SimpleGrantedAuthority(ruolo));
        return list;
    }

    @Override
    public String getPassword() {
        return compagniaAerea.getPassword();
    }

    @Override
    public String getUsername() {
        return compagniaAerea.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
