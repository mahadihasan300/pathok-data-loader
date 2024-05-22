package com.example.pathokdataloader.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface BookRepository extends JpaRepository<Book, BigInteger> {
}
