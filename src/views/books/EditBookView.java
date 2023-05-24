package views.books;

import config.Constant;
import controllers.BookController;
import models.Book;
import services.BookService;

import javax.swing.*;
import java.util.List;

public class EditBookView {
    private Object pagedBookList;
    private int currentPage = 1;
    private List<Book> bookList;

    public EditBookView() {
        BookService bookService = new BookService();
        pagedBookList = bookService.getPagedBookList(1, Constant.PAGE_SIZE);
        if (pagedBookList instanceof Object[]) {
            Object[] data = (Object[]) pagedBookList;
            bookList = (List<Book>) data[0];
            int totalPages = Integer.parseInt(data[1].toString());
            currentPage = Integer.parseInt(data[2].toString());

            // Display the book list in the UI
            int choice = 0;

            do {
                String title = "Ubah Buku\n";
                String content = String.format("Hal %d dari %d\n\n", currentPage, totalPages);
                StringBuilder bookData = new StringBuilder();
                for (int i = 0; i < bookList.size(); i++) {
                    Book book = bookList.get(i);
                    bookData.append(String.format("%d. %s\n", book.getId(), book.getName()));
                }
                content += bookData + "\n";
                if (currentPage > 1) {
                    content += "Q. Hal Sebelumnya\n";
                }
                if (currentPage < totalPages) {
                    content += "E. Hal Berikutnya\n";
                }
                content += "0. Kembali ke menu sebelumnya\n\n";
                content += "Pilih nomor buku yang akan diubah: ";

                String input = JOptionPane.showInputDialog(null, title + content, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                if (input == null) {
                    choice = 0;
                } else {
                    if (input.equalsIgnoreCase("q")) {
                        if (currentPage > 1) {
                            // Go to the previous page
                            currentPage--;
                            pagedBookList = bookService.getPagedBookList(currentPage, Constant.PAGE_SIZE);
                            if (pagedBookList instanceof Object[]) {
                                data = (Object[]) pagedBookList;
                                bookList = (List<Book>) data[0];
                                totalPages = Integer.parseInt(data[1].toString());
                            }
                            // Update the choice to continue the loop
                            choice = 1;
                        } else {
                            // Show error message if the current page is the first page
                            choice = 1;
                        }
                    } else if (input.equalsIgnoreCase("e")) {
                        if (currentPage < totalPages) {
                            // Go to the next page
                            currentPage++;
                            pagedBookList = bookService.getPagedBookList(currentPage, Constant.PAGE_SIZE);
                            if (pagedBookList instanceof Object[]) {
                                data = (Object[]) pagedBookList;
                                bookList = (List<Book>) data[0];
                                totalPages = Integer.parseInt(data[1].toString());
                            }
                            // Update the choice to continue the loop
                            choice = 1;
                        } else {
                            // Show error message if the current page is the last page
                            choice = 1;
                        }
                    } else if (input.equals("0")) {
                        // Go back to the previous menu
                        choice = 0;
                    } else {
                        // Try to parse the selected book ID
                        try {
                            int selectedBookId = Integer.parseInt(input);
                            getSelectedChoice(selectedBookId);
                        } catch (NumberFormatException e) {
                            // Show error message if the input is not a number
                            JOptionPane.showMessageDialog(null, "Input tidak valid. Silakan coba lagi.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } while (choice != 0);

            if (choice == 0) {
                // Go back to the previous menu
                new BookController().displayMenu();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data buku.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getSelectedChoice(int selectedBookId) {
        BookService bookService = new BookService();

        String title = "Ubah Buku\n\n";
        String askName = "Masukkan nama buku: ";
        String newBookName = JOptionPane.showInputDialog(null, title + askName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

        if (newBookName != null) {
            if (newBookName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama buku tidak boleh kosong.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            } else {
                Book book = bookService.getBookById(selectedBookId);
                if (book != null) {
                    book.setName(newBookName);
                    bookService.updateBook(book);
                } else {
                    JOptionPane.showMessageDialog(null, "Buku tidak ditemukan.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Refresh the page to display the updated bookList
        Object pagedBookList = bookService.getPagedBookList(currentPage, Constant.PAGE_SIZE);
        if (pagedBookList instanceof Object[]) {
            Object[] data = (Object[]) pagedBookList;
            bookList = (List<Book>) data[0];
        }
    }
}
