package com.example.library.util;

import com.example.library.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<Book> findLimitedBook(List<Book> books, int positionOfList){
        ArrayList<Book> tempBooks = new ArrayList<>();
        int count = 0;
        if (books.size() <= 10){
            return books;
        }else if (positionOfList*10 > books.size()){
            return tempBooks;
        }else {
            while (positionOfList*10 < books.size() && count < 10){
                tempBooks.add(books.get(positionOfList*10+count));
                count++;
            }
            return tempBooks;
        }
    }
}
