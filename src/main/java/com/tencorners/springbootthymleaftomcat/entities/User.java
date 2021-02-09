package com.tencorners.springbootthymleaftomcat.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Integer isDisabled;
    private Integer isAccountExpired;
    private Integer isAccountLocked;
    private Integer isCredentialsExpired;
    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public boolean isDisabled() {
        return (this.isDisabled != 0);
    }
    public boolean isAccountExpired() {
        return (this.isAccountExpired != 0);
    }
    public boolean isAccountLocked() {
        return (this.isAccountLocked != 0);
    }
    public boolean isCredentialsExpired() {
        return (this.isCredentialsExpired != 0);
    }
}
