package main.ptudj2ee_bai6.service;

import main.ptudj2ee_bai6.model.Account;
import main.ptudj2ee_bai6.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOpt = accountRepository.findByLoginName(username);
        
        if (accountOpt.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user: " + username);
        }

        Account account = accountOpt.get();
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Convert roles to authorities with "ROLE_" prefix
        account.getRoles().forEach(role -> {
            String roleName = role.getName();
            String authority = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
            authorities.add(new SimpleGrantedAuthority(authority));
        });

        return new User(
            account.getLoginName(),
            account.getPassword(),
            authorities
        );
    }

    public Account findByLoginName(String loginName) {
        return accountRepository.findByLoginName(loginName).orElse(null);
    }

    public Account createAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }
}