package com.example.pathokdataloader.author;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Entity
@Data
@Table(name = "author_by_id")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger oid;

    @Column(name = "author_id")
    private String authorId;

    @Column(name = "author_name")
    private String name;

    @Column(name = "personal_name")
    private String personalName;


}
