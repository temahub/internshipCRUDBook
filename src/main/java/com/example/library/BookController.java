package com.example.library;

import com.example.library.domain.Book;
import com.example.library.repos.BookRepo;
import com.example.library.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class BookController {
    private static final boolean READED = true;
    private static final boolean NOTREADED = false;
    private static int positionOfList = 0;
    private static String searchValue = "";
    private static String searchItem = "";
    private static boolean searchReadAlready = false;
    private List<Book> books;
    @Autowired
    private BookRepo bookRepo;

    @GetMapping
    public String main(Model model){

        if (searchValue.equals("")){
            while ((books = bookRepo.findLimited(new PageRequest(positionOfList, 10))).isEmpty() && positionOfList > 0) {
                positionOfList--;
            }
        }else {
            List<Book> tempBooks;
            if (searchItem.equals("title")){
                books = bookRepo.findByTitleAndReadAlready(searchValue, searchReadAlready);
            }else {
                books = bookRepo.findByAuthorAndReadAlready(searchValue, searchReadAlready);
            }
            while ( (tempBooks = Util.findLimitedBook(books, positionOfList)).isEmpty() && positionOfList > 0){
                positionOfList--;
            }
            model.addAttribute("books", tempBooks);
            return "main";
        }

        model.addAttribute("books", books);
        return "main";
    }

    @GetMapping("/remove/{id}")
    @Transactional
    public String removeBook(@PathVariable("id") Integer id, Model model){

        bookRepo.deleteById(id);

        return "redirect:/main";
    }
    @GetMapping("/read/{id}")
    @Transactional
    public String read(@PathVariable("id") Integer id, Model model){

        bookRepo.setBookReadById(READED, id);

        return "redirect:/main";
    }

    @GetMapping("/previous")
    public String previous(){
        if (positionOfList > 0){
            positionOfList--;
        }
        return "redirect:/main";
    }

    @GetMapping("/next")
    public String next(){
        positionOfList++;

        return "redirect:/main";
    }

    @PostMapping("update")
    @Transactional
    public String updateBook(
            @RequestParam Integer id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String isbn,
            @RequestParam Integer printYear
    ){
        bookRepo.setBookUpdateById(title, description, isbn, printYear, NOTREADED, id);

        return "redirect:/main";
    }

    @PostMapping("add")
    public String add(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String author,
            @RequestParam String isbn,
            @RequestParam int printYear){

        Book book = new Book(title, description, author, isbn, printYear);
        bookRepo.save(book);

        return "redirect:/main";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String search(@RequestParam String search,
                         @RequestParam String item,
                         @RequestParam(defaultValue = "false") boolean searchAlready,
                         Model model){

        positionOfList = 0;
        searchValue = search;
        searchItem = item;
        searchReadAlready = searchAlready;

        if (searchValue.equals("")){
            searchValue = "";
            searchItem = "";
            searchReadAlready = false;
            books = bookRepo.findAll();
            model.addAttribute("books", books);
        }

        return "redirect:/main";
    }
}
