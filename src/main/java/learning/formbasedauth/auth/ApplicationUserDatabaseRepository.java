package learning.formbasedauth.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static learning.formbasedauth.security.ApplicationUserRole.*;

@Repository("real-db-repository")
public class ApplicationUserDatabaseRepository implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserDatabaseRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getUsers().
                stream().
                filter(user -> username.equals(user.getUsername())).
                findFirst();
    }

    private List<ApplicationUser> getUsers() {
        List<ApplicationUser> users = List.of(
                new ApplicationUser("Mario",
                        passwordEncoder.encode("password"),
                        NORMAL.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true),
                new ApplicationUser("Juliu",
                        passwordEncoder.encode("password"),
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true)
        );

        return users;
    }
}
