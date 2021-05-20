package learning.basicauth.security;

public enum ApplicationUserPermission {
    WRITE("write"),
    READ("read");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
