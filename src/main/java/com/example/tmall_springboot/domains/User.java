package com.example.tmall_springboot.domains;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    private String salt;

    @Transient
    private String anonymousName;

    public String getAnonymousName() {

        if      (null != anonymousName) { return anonymousName; }
        if      (null == name) { anonymousName = null; }
        else if (name.length() <= 1) { anonymousName = "*"; }
        else if (name.length() == 2) { anonymousName = name.substring(0, 1) + "*"; }
        else {
            char[] cs = name.toCharArray();
            for (int i = 1; i < cs.length - 1; i++) {
                cs[i] = '*';
            }
            anonymousName = new String(cs);
        }
        return anonymousName;
    }

}
