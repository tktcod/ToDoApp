package com.sparta.todoapp.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp="^[a-z0-9]{4,10}$", message = "username 은 최소 4자 이상, 10자 이하이며 알파벳 소문자, 숫자로 구성되어야 합니다.")
    @Column(nullable = false, unique = true)
    private String username;

    @Pattern(regexp="^[a-zA-Z0-9]{8,15}$", message = "password 는 최소 8자 이상, 15자 이하이며 알파벳 대소문자, 숫자로 구성되어야 합니다.")
    @Column(nullable = false)
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
