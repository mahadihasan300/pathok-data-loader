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

    @Column(name = "book_description", columnDefinition="text", length=10485760)
    private String description;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "covers", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "cover_ids", nullable = false)
    private List<String> coverIds = new ArrayList<>();

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "author_ids", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "author_ids", nullable = false)
    private List<String> authorIds = new ArrayList<>();

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "author_names", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "author_names", nullable = false)
    private List<String> authorNames = new ArrayList<>();


}
