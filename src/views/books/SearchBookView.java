package views.books;

import config.Constant;
import services.BookService;

import javax.swing.*;

public class SearchBookView {
    public SearchBookView() {
        BookService bookService = new BookService();
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
                bookService.displaySearchedBookList(menu);
            }
        } while (isSearching);
    }
}
