package learning.formbasedauth.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class MyUserController {

    private static List<MyUser> USERS = new ArrayList<MyUser>(Arrays.asList(
            new MyUser(1, "Hélder", "helder@gmail.com"),
            new MyUser(2, "Maria", "maria@gmail.com"),
            new MyUser(3, "João", "joao@gmail.com")));

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_NORMAL', 'ROLE_ADMIN')")
    public List<MyUser> getUsers() {
        System.out.println("get users");
        return USERS;
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('write')")
    public MyUser getSpecificUser(@PathVariable("id") Integer id) {
        System.out.println("get specific user");
        return USERS.stream().
                filter(user -> id.equals(user.getId())).
                findFirst().
                orElseThrow(() -> new IllegalStateException(String.format("User %d not found", id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public void saveUser(@RequestBody MyUser user) {
        System.out.println("save user");
        USERS.add(user);
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('write')")
    public void deleteUser(@PathVariable("id") Integer id) {
        System.out.println("delete user");
        USERS = USERS.stream().
                filter(u -> !id.equals(u.getId())).
                collect(Collectors.toList());
    }

    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('write')")
    public void updateUser(@PathVariable("id") Integer id, @RequestBody MyUser user) {
        System.out.println("update user");
        USERS = USERS.stream().
                map(u -> id.equals(u.getId()) ? user : u).
                collect(Collectors.toList());
    }

}
