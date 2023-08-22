package com.hackathon.portfoliomanagerapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@Table(name = "Users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String name;

    public User(long userId) {
        this.userId = userId;
    }

    public User(long userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}