package controllers;

import services.BookService;
import views.books.*;

public class BookController {
    private final BookService bookService;

    public BookController() {
        bookService = new BookService();
    }

    // -----------------------------

    public void displayMenu() {
        new MenuBookView();
    }

    public void displayList() {
        new DisplayBookView();
    }

    public void displaySearch() {
        new SearchBookView();
    }

    public void create() {
        new CreateBookView();
    }

    public void edit() {
        new EditBookView();
    }
}