package views.books;

import config.Constant;
import controllers.BookController;
import models.Book;
import services.BookService;

import javax.swing.*;
import java.util.List;

public class DisplayBookView {
    private int currentPage = 1;
    private List<Book> bookList;
    private Object pagedBookList;

    public DisplayBookView() {
        displayPagedBookList(currentPage, Constant.PAGE_SIZE);
    }

    public void displayPagedBookList(int page, int pageSize) {
        BookService bookService = new BookService();
        Object result = bookService.getPagedBookList(page, pageSize);

        // Check if the result is an array of objects
        if (result instanceof Object[]) {
            Object[] data = (Object[]) result; // Use typecast, since the compiler doesn't know the type of the array
            bookList = (List<Book>) data[0]; // Use typecast, since the compiler doesn't know the type of the list
            int totalPages = Integer.parseInt(data[1].toString());
            currentPage = Integer.parseInt(data[2].toString());

            // Display the book list in the UI
            int choice = 0;

            do {
                String title = "Daftar Buku\n";
                String content = String.format("Hal %d dari %d\n\n", currentPage, totalPages);
                StringBuilder bookData = new StringBuilder();
                for (int i = 0; i < bookList.size(); i++) {
                    Book book = bookList.get(i);
                    bookData.append(book.menuListBooks());
                }
                content += bookData + "\n";
                content += "Pilih menu: \n";
                if (currentPage > 1) {
                    content += "Q. Hal Sebelumnya\n";
                }
                if (currentPage < totalPages) {
                    content += "E. Hal Berikutnya\n";
                }
                content += "0. Kembali ke menu sebelumnya";

                String input = JOptionPane.showInputDialog(null, title + content, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                // if cancel button is clicked, then return to Main Menu
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
                            // Handle the case where the current page is the first page
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
                            // Handle the case where the current page is the last page
                            choice = 1;
                        }
                    } else if (input.equals("0")) {
                        // Go back to the previous menu
                        choice = 0;
                    } else {
                        // Handle the case where the input is not as expected
                        JOptionPane.showMessageDialog(null, "Pilihan tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                        choice = 1;
                    }
                }
            } while (choice != 0);

            if (choice == 0) {
                new BookController().displayMenu();
            }
        } else {
            // Handle the case where the result is not as expected
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data buku", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
}
