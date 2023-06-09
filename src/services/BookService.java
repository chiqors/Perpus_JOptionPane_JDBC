package services;

import config.Constant;
import controllers.BookController;
import models.Book;
import repositories.BookRepository;
import utils.LoadingDialog;
import views.books.MenuBookView;

import javax.swing.*;
import java.util.List;

public class BookService {
    private final LoadingDialog loadingDialog;
    private final BookRepository bookRepository;

    public BookService() {
        loadingDialog = new LoadingDialog();
        bookRepository = new BookRepository();
    }

    // -----------------------------
    // All Non-Void Methods Below
    // -----------------------------

    public Book getBookById(int bookId) {
        loadingDialog.showLoading();
        Book book = bookRepository.getBookById(bookId);
        loadingDialog.hideLoading();
        return book;
    }

    public Object getPagedBookList(int page, int pageSize) {
        loadingDialog.showLoading();

        // get the paged book list
        List<Book> bookList = bookRepository.getPagedBooks(page, pageSize);
        // get the total number of books
        int totalBooks = bookRepository.getTotalBooks();
        // get the total number of pages
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
        // get the current page
        int currentPage = page;
        // combine all the data into a single object to be returned
        Object[] result = {bookList, totalPages, currentPage};
        // result: [bookList, totalPages, currentPage]

        loadingDialog.hideLoading();
        return result;
    }

    public List<Book> getBookListByName(String bookName) {
        loadingDialog.showLoading();
        List<Book> searchResults = bookRepository.searchBooksByName(bookName);
        loadingDialog.hideLoading();
        return searchResults;
    }

    // -----------------------------
    // All Void Methods Below
    // -----------------------------

    public void addBook(Book book) {
        loadingDialog.showLoading();
        boolean statusQuery = bookRepository.insertBook(book);
        loadingDialog.hideLoading();
        if (statusQuery) {
            JOptionPane.showMessageDialog(null, "Buku berhasil ditambahkan!", Constant.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Buku gagal ditambahkan!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateBook(Book book) {
        loadingDialog.showLoading();
        boolean statusQuery = bookRepository.updateBook(book);
        loadingDialog.hideLoading();
        if (statusQuery) {
            JOptionPane.showMessageDialog(null, "Buku berhasil diupdate!", Constant.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Buku gagal diupdate!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteBook(int bookId) {
        loadingDialog.showLoading();
        boolean statusQuery = bookRepository.deleteBook(bookId);
        loadingDialog.hideLoading();
        if (statusQuery) {
            JOptionPane.showMessageDialog(null, "Buku berhasil dihapus!", Constant.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Buku gagal dihapus!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
}
