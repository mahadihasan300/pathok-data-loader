package com.example.pathokdataloader.book;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "book_by_id")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger oid;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "book_name")
    private String name;

    @Column(name = "book_description")
    private String description;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "covers", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "cover_ids", nullable = false)
    private List<String> coverIds = new ArrayList<>();
}
