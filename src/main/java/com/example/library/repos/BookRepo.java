package com.example.library.repos;

import com.example.library.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BookRepo extends CrudRepository<Book, Long> {

    @Query("select b from Book b where b.title LIKE CONCAT('%',?1,'%') AND b.readAlready = ?2")
    List<Book>findByTitleAndReadAlready(String title, boolean readAlready);

    @Query("select b from Book b where b.title LIKE CONCAT('%',?1,'%')")
    List<Book>findByTitle(String title);

    @Query("select b from Book b where b.author LIKE CONCAT('%',?1,'%') AND b.readAlready = ?2")
    List<Book>findByAuthorAndReadAlready(String author, boolean readAlready);

    @Query("select b from Book b where b.author LIKE CONCAT('%',?1,'%')")
    List<Book>findByAuthor(String author);

    @Query("select b from Book b ")
    List<Book> findLimited(Pageable pageable);

    void deleteById(Integer id);

    @Query("select b from Book b")
    List<Book>findAll();

    @Modifying
    @Query("update Book b set b.readAlready = ?1 where b.id = ?2")
    void setBookReadById(boolean read, Integer id);

    @Modifying
    @Query("update Book b set b.title = ?1, b.description = ?2, b.isbn = ?3, b.printYear = ?4, b.readAlready = ?5 where b.id = ?6")
    void setBookUpdateById(String title, String description, String isbn, Integer printYear, boolean read, Integer id);
}
