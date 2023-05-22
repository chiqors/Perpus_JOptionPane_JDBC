package controllers;

import config.Constant;
import services.BookService;
import views.books.CreateBookView;
import views.books.SearchBookView;

public class BookController {
    private final BookService bookService;
    private int currentPage = 1;

    public BookController() {
        bookService = new BookService();
    }

    // -----------------------------

    public void displayList() {
        bookService.displayPagedBookList(currentPage, Constant.PAGE_SIZE);
    }

    public void displaySearch() {
        new SearchBookView();
    }

    public void create() {
        new CreateBookView();
    }
}