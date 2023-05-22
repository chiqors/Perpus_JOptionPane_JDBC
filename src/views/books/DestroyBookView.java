package views.books;

import config.Constant;
import models.Book;
import services.BookService;

import javax.swing.*;
import java.util.List;

public class DestroyBookView {
    private Object pagedBookList;
    private int currentPage = 1;
    private List<Book> bookList;

    public DestroyBookView() {
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
                    content += "q. Hal Sebelumnya\n";
                }
                if (currentPage < totalPages) {
                    content += "e. Hal Berikutnya\n";
                }
                content += "0. Kembali ke menu sebelumnya\n\n";
                content += "Pilih nomor buku yang akan diubah: ";

                String input = JOptionPane.showInputDialog(null, title + content, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                if (input == null) {
                    choice = 0;
                } else {
                    if (input.equalsIgnoreCase("q")) {
                        // Go to the previous page
                        currentPage--;
                        pagedBookList = bookService.getPagedBookList(currentPage, Constant.PAGE_SIZE);
                        if (pagedBookList instanceof Object[]) {
                            data = (Object[]) pagedBookList;
                            bookList = (List<Book>) data[0];
                            totalPages = Integer.parseInt(data[1].toString());
                        }
                    } else if (input.equalsIgnoreCase("e")) {
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
                new MenuBookView();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data buku.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getSelectedChoice(int selectedBookId) {
        BookService bookService = new BookService();
        Book book = bookService.getBookById(selectedBookId);
        if (book == null) {
            JOptionPane.showMessageDialog(null, "Buku tidak ditemukan.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return;
        }

        String confirmDelete = String.format("Apakah Anda yakin ingin menghapus buku '%s'?", book.getName());
        int confirm = JOptionPane.showConfirmDialog(null, confirmDelete, Constant.APP_NAME, JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            bookService.deleteBook(selectedBookId);
        }

        // Refresh the book list
        Object pagedBookList = bookService.getPagedBookList(currentPage, Constant.PAGE_SIZE);
        if (pagedBookList instanceof Object[]) {
            Object[] data = (Object[]) pagedBookList;
            bookList = (List<Book>) data[0];
        }
    }
}