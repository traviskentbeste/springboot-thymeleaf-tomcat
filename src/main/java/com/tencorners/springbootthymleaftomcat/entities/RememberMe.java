package com.tencorners.springbootthymleaftomcat.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name="remember_me")
public class RememberMe implements Serializable {

    @Id
    private String series;
    private String username;
    private String token;
    private Date lastUsed;

}