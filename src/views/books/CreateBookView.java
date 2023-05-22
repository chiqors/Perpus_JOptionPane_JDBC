package views.books;

import config.Constant;
import services.BookService;
import models.Book;

import javax.swing.*;

public class CreateBookView {
    public CreateBookView() {
        BookService bookService = new BookService();
        boolean isCreating = true;
        do {
            String title = "Tambah Buku\n\n";
            String askBookName = "Masukkan nama buku: ";
            String askBookAuthor = "Masukkan nama pengarang: ";
            String askBookYear = "Masukkan tanggal terbit (YYYY-MM-DD): ";
            String askBookStock = "Masukkan jumlah stok: ";

            String bookName = JOptionPane.showInputDialog(null, title + askBookName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (bookName == null) {
                isCreating = false;
                break;
            } else if (bookName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama buku tidak boleh kosong!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            }

            String bookAuthor = JOptionPane.showInputDialog(null, title + askBookAuthor, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (bookAuthor == null) {
                isCreating = false;
                break;
            } else if (bookAuthor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama pengarang tidak boleh kosong!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            }

            String bookYear = JOptionPane.showInputDialog(null, title + askBookYear, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (bookYear == null) {
                isCreating = false;
                break;
            } else if (bookYear.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tanggal terbit tidak boleh kosong!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            }

            String bookStock = JOptionPane.showInputDialog(null, title + askBookStock, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (bookStock == null) {
                isCreating = false;
                break;
            } else if (bookStock.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Jumlah stok tidak boleh kosong!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            }
            int stock = Integer.parseInt(bookStock);
            if (stock <= 0) {
                JOptionPane.showMessageDialog(null, "Jumlah stok tidak boleh kurang dari atau sama dengan 0!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            }

            isCreating = false;
            Book book = new Book(bookName, bookAuthor, bookYear, stock);
            bookService.addBook(book);
        } while (isCreating);

        if (!isCreating) {
            new MenuBookView();
        }
    }
}
