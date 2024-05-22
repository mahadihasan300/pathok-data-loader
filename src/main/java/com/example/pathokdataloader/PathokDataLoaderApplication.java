package com.example.pathokdataloader;

import com.example.pathokdataloader.author.Author;
import com.example.pathokdataloader.author.AuthorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PathokDataLoaderApplication {

    @Autowired
    AuthorRepository authorRepository;

    public static void main(String[] args) {
        SpringApplication.run(PathokDataLoaderApplication.class, args);
    }

    @PostConstruct
    public void start(){
        System.out.println("Application Started");
        Author author = new Author();
        //author.setId("aaa");
        author.setName("name");
        author.setPersonalName("Last name");

        authorRepository.save(author);
    }

}
