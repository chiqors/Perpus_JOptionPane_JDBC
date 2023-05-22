package services;

import config.Constant;
import models.Book;
import repositories.BookRepository;
import utils.LoadingDialog;
import views.menu.BookManagementMenu;

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

    private Object getPagedBookList(int page, int pageSize) {
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

    public void displayPagedBookList(int page, int pageSize) {
        Object result = getPagedBookList(page, pageSize);

        // Check if the result is an array of objects
        if (result instanceof Object[]) {
            Object[] data = (Object[]) result; // Use typecast, since the compiler doesn't know the type of the array
            List<Book> bookList = (List<Book>) data[0]; // Use typecast, since the compiler doesn't know the type of the list
            int totalPages = Integer.parseInt(data[1].toString());
            int currentPage = Integer.parseInt(data[2].toString());

            // Display the book list in the UI
            int choice = 0;

            do {
                String title = "Daftar Buku\n\n";
                String bookData = "";
                for (int i = 0; i < bookList.size(); i++) {
                    bookData += (i + 1) + ". " + bookList.get(i) + "\n";
                }
                String menuChoice = "\nPilih menu: \n";

                if (currentPage > 1) {
                    menuChoice += "1. Kembali\n";
                }
                if (currentPage < totalPages) {
                    menuChoice += "2. Lanjut\n";
                }
                menuChoice += "0. Kembali ke menu sebelumnya\n";

                String menu = JOptionPane.showInputDialog(null, title + bookData + menuChoice, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                // if cancel button is clicked, then return to Main Menu
                if (menu == null) {
                    choice = 0;
                    break;
                }
                // if user inputted alphabet or symbol, then show error message
                if (!menu.matches("[0-9]*")) {
                    JOptionPane.showMessageDialog(null, "Input harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                choice = Integer.parseInt(menu);

                switch (choice) {
                    case 0:
                        break;
                    case 1:
                        if (currentPage > 1) {
                            displayPagedBookList(currentPage - 1, pageSize);
                            choice = 0; // Reset choice to prevent going back to the default list
                        }
                        break;
                    case 2:
                        if (currentPage < totalPages) {
                            displayPagedBookList(currentPage + 1, pageSize);
                            choice = 0; // Reset choice to prevent going back to the default list
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } while (choice != 0);

            if (choice == 0) {
                new BookManagementMenu();
            }
        } else {
            // Handle the case where the result is not as expected
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void displaySearchedBookList(String bookName) {
        List<Book> searchResults = getBookListByName(bookName);

        // Display the book list in the UI
        int choice = 0;

        do {
            String title = "Hasil Pencarian Buku\n\n";
            String bookData = "";
            for (int i = 0; i < searchResults.size(); i++) {
                bookData += (i + 1) + ". " + searchResults.get(i) + "\n";
            }
            String menuChoice = "\nPilih menu: \n";

            menuChoice += "0. Kembali ke menu sebelumnya\n";

            String menu = JOptionPane.showInputDialog(null, title + bookData + menuChoice, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to Main Menu
            if (menu == null) {
                choice = 0;
                break;
            }
            // if user inputted alphabet or symbol, then show error message
            if (!menu.matches("[0-9]*")) {
                JOptionPane.showMessageDialog(null, "Input harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            choice = Integer.parseInt(menu);

            switch (choice) {
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (choice != 0);

        if (choice == 0) {
            new BookManagementMenu();
        }
    }

    public void addBook(Book book) {
        loadingDialog.showLoading();
        bookRepository.insertBook(book);
        loadingDialog.hideLoading();
        // Display a success message in the UI
    }

    public void updateBook(Book book) {
        loadingDialog.showLoading();
        bookRepository.updateBook(book);
        loadingDialog.hideLoading();
        // Display a success message in the UI
    }

    public void deleteBook(int bookId) {
        loadingDialog.showLoading();
        bookRepository.deleteBook(bookId);
        loadingDialog.hideLoading();
        // Display a success message in the UI
    }
}
