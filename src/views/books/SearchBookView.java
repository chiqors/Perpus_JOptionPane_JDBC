package views.books;

import config.Constant;
import controllers.BookController;
import models.Book;
import services.BookService;

import javax.swing.*;
import java.util.List;

public class SearchBookView {
    public SearchBookView() {
        boolean isSearching = true;
        do {
            String title = "Cari Buku\n\n";
            String askBookName = "Masukkan nama buku: ";
            String menu = JOptionPane.showInputDialog(null, title + askBookName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to Book Management Menu
            if (menu == null) {
                isSearching = false;
            } else if (menu.isEmpty()) { // if user inputted empty string, then show error message
                JOptionPane.showMessageDialog(null, "Nama buku tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                isSearching = true;
            } else {
                isSearching = false;
                displaySearchedBookList(menu);
            }
        } while (isSearching);
    }

    public void displaySearchedBookList(String bookName) {
        BookService bookService = new BookService();
        List<Book> searchResults = bookService.getBookListByName(bookName);

        // Display the book list in the UI
        int choice = 0;

        do {
            String title = "Hasil Pencarian Buku\n\n";
            String bookData = "";
            for (int i = 0; i < searchResults.size(); i++) {
                bookData += searchResults.get(i).menuListBooks();
            }
            if (searchResults.size() == 0) {
                bookData += "Tidak ada buku yang ditemukan!\n";
            }
            String menuChoice = "\nPilih menu: \n";

            menuChoice += "0. Kembali ke menu sebelumnya";

            String menu = JOptionPane.showInputDialog(null, title + bookData + menuChoice, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to Main Menu
            if (menu == null) {
                choice = 0;
            } else if (!menu.matches("[0-9]*")) {
                JOptionPane.showMessageDialog(null, "Menu yang Anda masukkan salah!", "Error", JOptionPane.ERROR_MESSAGE);
                choice = -1;
            } else if (menu.equals("0")) {
                choice = 0;
            } else {
                choice = -1;
            }
        } while (choice != 0);

        if (choice == 0) {
            new BookController().displayMenu();
        }
    }
}
