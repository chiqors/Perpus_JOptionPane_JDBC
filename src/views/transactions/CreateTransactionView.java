package views.transactions;

import config.Constant;
import models.Book;
import models.Member;
import models.Transaction;
import services.BookService;
import services.MemberService;
import services.TransactionService;
import views.menu.MainMenu;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class CreateTransactionView {
    private final TransactionService transactionService;
    private final BookService bookService;
    private final MemberService memberService;

    private Object pagedBookList;
    private int currentPage = 1;
    private List<Book> bookList;

    private Transaction savedTransaction;
    private ArrayList<Book> savedBorrowedBooks;

    // Additional class variables
    private int iMemberId;
    private String iMemberName;
    private String iExpectedReturnDate;

    public CreateTransactionView() {
        transactionService = new TransactionService();
        bookService = new BookService();
        memberService = new MemberService();

        savedBorrowedBooks = new ArrayList<>();
        savedTransaction = new Transaction();

        displayBookList();
    }

    private void displayBookList() {
        pagedBookList = bookService.getPagedBookList(1, Constant.PAGE_SIZE);
        if (pagedBookList instanceof Object[]) {
            Object[] data = (Object[]) pagedBookList;
            bookList = (List<Book>) data[0];
            int totalPages = Integer.parseInt(data[1].toString());
            currentPage = Integer.parseInt(data[2].toString());

            // Display the book list in the UI
            int choice = 0;

            do {
                String title = "Peminjaman Buku\n";
                String content = String.format("Hal %d dari %d\n\n", currentPage, totalPages);
                StringBuilder bookData = new StringBuilder();
                for (int i = 0; i < bookList.size(); i++) {
                    Book book = bookList.get(i);
                    String stock = book.getStock() > 0 ? book.getStock() + " tersedia" : "habis";
                    bookData.append(String.format("%d. %s | %s | %s | %s\n", book.getId(), book.getName(), book.getAuthor(), book.getPublished(), stock));
                }
                content += bookData + "\n";
                if (currentPage > 1) {
                    content += "Q. Hal Sebelumnya\n";
                } else if (currentPage < totalPages) {
                    content += "E. Hal Berikutnya\n";
                }
                content += "0. Kembali ke menu sebelumnya\n\n";
                content += "Pilih nomor buku yang akan dipinjam: ";

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
                                currentPage = Integer.parseInt(data[2].toString());
                            }
                            choice = -1; // Update choice to continue the loop
                        } else {
                            choice = -1;
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
                                currentPage = Integer.parseInt(data[2].toString());
                            }
                            choice = -1; // Update choice to continue the loop
                        } else {
                            choice = -1;
                        }
                    } else if (input.matches("[0-9]+")) {
                        int selectedBookId = Integer.parseInt(input);
                        if (selectedBookId == 0) {
                            choice = 0;
                        } else {
                            getSelectedChoice(selectedBookId);
                            choice = -1; // Update choice to continue the loop
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Pilihan tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                        choice = -1;
                    }
                }
            } while (choice != 0);

            if (choice == 0) {
                new MainMenu();
            }
        }
    }

    private void getSelectedChoice(int selectedBookId) {
        Book selectedBook = bookService.getBookById(selectedBookId);

        if (selectedBook != null) {
            String requestBook = "Apakah anda yakin ingin meminjam buku ini?\n\n";
            requestBook += String.format("ID Buku: %d\n", selectedBook.getId());
            requestBook += String.format("Nama Buku: %s\n", selectedBook.getName());
            requestBook += String.format("Pengarang: %s\n", selectedBook.getAuthor());
            requestBook += String.format("Tgl Terbit: %s\n", selectedBook.getPublished());

            // Show confirmation dialog before proceeding
            int confirmChoice = JOptionPane.showConfirmDialog(null, requestBook, Constant.APP_NAME, JOptionPane.YES_NO_OPTION);
            if (confirmChoice == JOptionPane.YES_OPTION) {
                // Prompt for the number of books to borrow
                int howManySameBookBorrow = promptForNumberOfBooks();

                if (selectedBook.getStock() >= howManySameBookBorrow) {
                    int memberId = promptForMemberId(memberService);

                    if (memberId != 0) {
                        Member member = memberService.getMemberById(memberId);

                        if (confirmMemberDetails(member)) {
                            iMemberId = member.getId();
                            iMemberName = member.getName();

                            String expectedReturnDate = promptForExpectedReturnDate();
                            iExpectedReturnDate = expectedReturnDate;

                            if (isValidExpectedReturnDate(expectedReturnDate)) {
                                boolean isMemberHasBorrowedSameBook = checkIfMemberHasBorrowedSameBook(selectedBook);

                                if (isMemberHasBorrowedSameBook) {
                                    updateBorrowedBookQuantity(selectedBook, howManySameBookBorrow);
                                } else {
                                    addNewBorrowedBook(selectedBook, howManySameBookBorrow);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Tgl Pengembalian tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "ID Anggota tidak ditemukan.", Constant.APP_NAME, JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Stok buku tidak mencukupi.", Constant.APP_NAME, JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private int promptForNumberOfBooks() {
        int howManySameBookBorrow = 0;
        try {
            String inputBooks = JOptionPane.showInputDialog(null, "Masukkan jumlah buku ini yang akan dipinjam: ", Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            howManySameBookBorrow = Integer.parseInt(inputBooks);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Jumlah buku tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam melakukan peminjaman.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
        return howManySameBookBorrow;
    }

    private int promptForMemberId(MemberService memberService) {
        int memberId = 0;
        boolean isValidMemberId = false;
        do {
            try {
                memberId = Integer.parseInt(JOptionPane.showInputDialog(null, "Masukkan ID Anggota: ", Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE));
                if (memberService.isMemberIdValid(memberId)) {
                    isValidMemberId = true;
                } else {
                    JOptionPane.showMessageDialog(null, "ID Anggota tidak ditemukan.", Constant.APP_NAME, JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID Anggota tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan dalam melakukan peminjaman.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            }
        } while (!isValidMemberId);
        return memberId;
    }

    private boolean confirmMemberDetails(Member member) {
        String confirmMemberDetails = "Detail anggota yang meminjam buku sebagai berikut:\n\n";
        confirmMemberDetails += String.format("ID Anggota: %d\n", member.getId());
        confirmMemberDetails += String.format("Nama Anggota: %s\n", member.getName());
        confirmMemberDetails += String.format("Email: %s\n", member.getEmail());

        int confirmMemberChoice = JOptionPane.showConfirmDialog(null, confirmMemberDetails, Constant.APP_NAME, JOptionPane.YES_NO_OPTION);
        return confirmMemberChoice == JOptionPane.YES_OPTION;
    }

    private String promptForExpectedReturnDate() {
        return JOptionPane.showInputDialog(null, "Masukkan Tgl Pengembalian (YYYY-MM-DD): ", Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
    }

    private boolean isValidExpectedReturnDate(String expectedReturnDate) {
        // Check if expected return date is valid and not in the past
        // First check if the date format is valid (YYYY-MM-DD)
        if (expectedReturnDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date expectedReturnDateObj = sdf.parse(expectedReturnDate);
                Date currentDate = new Date();

                // Check if the date is not in the past
                return expectedReturnDateObj.after(currentDate);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean checkIfMemberHasBorrowedSameBook(Book selectedBook) {
        for (Book borrowedBook : savedBorrowedBooks) {
            if (borrowedBook.getId() == selectedBook.getId()) {
                return true;
            }
        }
        return false;
    }

    private void updateBorrowedBookQuantity(Book selectedBook, int howManySameBookBorrow) {
        for (Book borrowedBook : savedBorrowedBooks) {
            if (borrowedBook.getId() == selectedBook.getId()) {
                borrowedBook.setQty(borrowedBook.getQty() + howManySameBookBorrow);
                break;
            }
        }
    }

    private void addNewBorrowedBook(Book selectedBook, int howManySameBookBorrow) {
        System.out.println(selectedBook);
        Book currentBorrowBook = new Book();
        currentBorrowBook.setId(selectedBook.getId());
        currentBorrowBook.setName(selectedBook.getName());
        currentBorrowBook.setAuthor(selectedBook.getAuthor());
        currentBorrowBook.setPublished(selectedBook.getPublished());
        currentBorrowBook.setQty(howManySameBookBorrow);

        // Add the current borrowed book to the temporary list
        savedBorrowedBooks.add(currentBorrowBook);

        int confirmChoice = JOptionPane.showConfirmDialog(null, "Apakah Anda ingin meminjam buku lain?", Constant.APP_NAME, JOptionPane.YES_NO_OPTION);
        if (confirmChoice == JOptionPane.NO_OPTION) {
            createTransaction();
            refreshBookList();
        } else {
            refreshBookList();
            displayListForBorrow();
        }
    }

    public void displayListForBorrow() {
        pagedBookList = bookService.getPagedBookList(1, Constant.PAGE_SIZE);
        if (pagedBookList instanceof Object[]) {
            Object[] data = (Object[]) pagedBookList;
            bookList = (List<Book>) data[0];
            int totalPages = Integer.parseInt(data[1].toString());
            currentPage = Integer.parseInt(data[2].toString());

            // Display the book list in the UI
            int choice = 0;

            do {
                String title = "Peminjaman Buku\n";
                String content = String.format("Hal %d dari %d\n\n", currentPage, totalPages);
                StringBuilder bookData = new StringBuilder();
                for (int i = 0; i < bookList.size(); i++) {
                    Book book = bookList.get(i);
                    String stock = book.getStock() > 0 ? book.getStock() + " tersedia" : "habis";
                    bookData.append(String.format("%d. %s | %s | %s | %s\n", book.getId(), book.getName(), book.getAuthor(), book.getPublished(), stock));
                }
                content += bookData + "\n";
                if (currentPage > 1) {
                    content += "Q. Hal Sebelumnya\n";
                } else if (currentPage < totalPages) {
                    content += "E. Hal Berikutnya\n";
                }
                content += "0. Kembali ke menu sebelumnya\n\n";
                content += "Pilih nomor buku yang akan dipinjam: ";

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
                                currentPage = Integer.parseInt(data[2].toString());
                            }
                            choice = -1; // Update choice to continue the loop
                        } else {
                            choice = -1;
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
                                currentPage = Integer.parseInt(data[2].toString());
                            }
                            choice = -1; // Update choice to continue the loop
                        } else {
                            choice = -1;
                        }
                    } else if (input.matches("[0-9]+")) {
                        int selectedBookId = Integer.parseInt(input);
                        if (selectedBookId == 0) {
                            choice = 0;
                        } else {
                            getSelectedChoiceForBorrow(selectedBookId);
                            choice = -1; // Update choice to continue the loop
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Pilihan tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                        choice = -1;
                    }
                }
            } while (choice != 0);

            if (choice == 0) {
                new MainMenu();
            }
        }
    }

    public void getSelectedChoiceForBorrow(int selectedBookId) {
        Book selectedBook = bookService.getBookById(selectedBookId);

        if (selectedBook != null) {
            String requestBook = "Apakah anda yakin ingin meminjam buku ini?\n\n";
            requestBook += String.format("ID Buku: %d\n", selectedBook.getId());
            requestBook += String.format("Nama Buku: %s\n", selectedBook.getName());
            requestBook += String.format("Pengarang: %s\n", selectedBook.getAuthor());
            requestBook += String.format("Tgl Terbit: %s\n", selectedBook.getPublished());

            // Show confirmation dialog before proceeding
            int confirmChoice = JOptionPane.showConfirmDialog(null, requestBook, Constant.APP_NAME, JOptionPane.YES_NO_OPTION);
            if (confirmChoice == JOptionPane.YES_OPTION) {
                // Prompt for the number of books to borrow
                int howManySameBookBorrow = promptForNumberOfBooks();

                if (selectedBook.getStock() >= howManySameBookBorrow) {
                    addNewBorrowedBook(selectedBook, howManySameBookBorrow);
                } else {
                    JOptionPane.showMessageDialog(null, "Stok buku tidak mencukupi.", Constant.APP_NAME, JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private void createTransaction() {
        savedTransaction.setMemberId(iMemberId);
        savedTransaction.setMemberName(iMemberName);
        savedTransaction.setExpectedReturnDate(iExpectedReturnDate);
        savedTransaction.setReturnedDate("");
        savedTransaction.setBorrowedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        savedTransaction.setBorrowedBooks(savedBorrowedBooks);
        savedTransaction.setStatus("borrowed");

        transactionService.addBorrowTransaction(savedTransaction);
        savedTransaction = new Transaction();
    }

    private void refreshBookList() {
        pagedBookList = bookService.getPagedBookList(1, Constant.PAGE_SIZE);
        if (pagedBookList instanceof Object[]) {
            Object[] data = (Object[]) pagedBookList;
            bookList = (List<Book>) data[0];
            currentPage = Integer.parseInt(data[2].toString());
        }
    }
}
