package views.transactions;

import config.Constant;
import models.Transaction;
import services.TransactionService;
import views.menu.MainMenu;

import javax.swing.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UpdateTransactionView {
    private final TransactionService transactionService;
    private Object pagedBorrowedTransactionList;
    private List<Transaction> borrowedTransactionList;
    private int currentPage;

    public UpdateTransactionView() {
        transactionService = new TransactionService();

        pagedBorrowedTransactionList = transactionService.getPagedBorrowedTransactionList(1, 3);
        if (pagedBorrowedTransactionList instanceof Object[]) {
            Object[] data = (Object[]) pagedBorrowedTransactionList;
            borrowedTransactionList = (List<Transaction>) data[0];
            int totalPages = Integer.parseInt(data[1].toString());
            currentPage = Integer.parseInt(data[2].toString());

            // Display the list of borrowed books
            int choice = 0;
            String title = "Pengembalian Buku\n";

            // if borrowedTransactionList is empty, then display a message
            if (borrowedTransactionList.isEmpty()) {
                String content = "--Tidak ada buku yang dipinjam--\n\n";
                content += "0. Kembali ke menu utama\n\n";
                content += "Pilihan Anda:";
                String input = JOptionPane.showInputDialog(null, title + content, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                if (input == null) {
                    choice = 0;
                }
            } else {
                do {
                    String content = String.format("Hal %d dari %d\n", currentPage, totalPages);
                    StringBuilder transactionData = new StringBuilder();
                    for (int i = 0; i < borrowedTransactionList.size(); i++) {
                        Transaction transaction = borrowedTransactionList.get(i);
                        transactionData.append(transaction.toString());
                    }
                    content += transactionData.toString();
                    if (currentPage > 1) {
                        content += "\nQ. Hal Sebelumnya\n";
                    } else if (currentPage < totalPages) {
                        content += "\nE. Hal Selanjutnya\n";
                    }
                    content += "\n0. Kembali ke menu utama\n\n";
                    content += "ID Transaksi yang ingin dikembalikan:";

                    String input = JOptionPane.showInputDialog(null, title + content, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                    if (input == null) {
                        choice = 0;
                    } else {
                        if (input.equalsIgnoreCase("q")) {
                            // Return to previous page
                            currentPage--;
                            pagedBorrowedTransactionList = transactionService.getPagedBorrowedTransactionList(currentPage, 3);
                            if (pagedBorrowedTransactionList instanceof Object[]) {
                                data = (Object[]) pagedBorrowedTransactionList;
                                borrowedTransactionList = (List<Transaction>) data[0];
                                totalPages = Integer.parseInt(data[1].toString());
                                currentPage = Integer.parseInt(data[2].toString());
                            }
                            choice = -1; // Update choice to continue the loop
                        } else if (input.equalsIgnoreCase("e")) {
                            // Go to next page
                            currentPage++;
                            pagedBorrowedTransactionList = transactionService.getPagedBorrowedTransactionList(currentPage, 3);
                            if (pagedBorrowedTransactionList instanceof Object[]) {
                                data = (Object[]) pagedBorrowedTransactionList;
                                borrowedTransactionList = (List<Transaction>) data[0];
                                totalPages = Integer.parseInt(data[1].toString());
                                currentPage = Integer.parseInt(data[2].toString());
                            }
                            choice = -1; // Update choice to continue the loop
                        } else if (input.matches("[0-9]+")) {
                            int selectedTransactionId = Integer.parseInt(input);
                            if (selectedTransactionId == 0) {
                                choice = 0;
                            } else {
                                getSelectedChoice(selectedTransactionId);
                                choice = -1; // Update choice to continue the loop
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Input harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                            choice = -1; // Update choice to continue the loop
                        }
                    }
                } while (choice != 0);
            }

            if (choice == 0) {
                new MainMenu();
            }
        }
    }

    private void getSelectedChoice(int selectedTransactionId) {
        Transaction transaction = transactionService.getBorrowedTransactionById(selectedTransactionId);
        if (transaction != null) {
            String title = "Pengembalian Buku\n";
            String content = transaction.toString();
            content += "\n\nApakah Anda yakin ingin mengembalikan buku ini?\n";

            int input = JOptionPane.showConfirmDialog(null, title + content, Constant.APP_NAME, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (input == JOptionPane.YES_OPTION) {
                if (isDueDatePassed(transaction.getExpectedReturnDate())) {
                    JOptionPane.showMessageDialog(null, "Anda terlambat mengembalikan buku!\nDenda Rp. 5000,-", Constant.APP_NAME, JOptionPane.WARNING_MESSAGE);
                }
                transactionService.updateBorrowedToReturnedTransaction(transaction);
                refreshBorrowedTransactionList();
            }
        } else {
            JOptionPane.showMessageDialog(null, "ID Transaksi tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Boolean isDueDatePassed(String dueDate) {
        // YYYY-MM-DD
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = formatter.format(date);
        return currentDate.compareTo(dueDate) > 0;
    }

    private void refreshBorrowedTransactionList() {
        pagedBorrowedTransactionList = transactionService.getPagedBorrowedTransactionList(currentPage, 3);
        if (pagedBorrowedTransactionList instanceof Object[]) {
            Object[] data = (Object[]) pagedBorrowedTransactionList;
            borrowedTransactionList = (List<Transaction>) data[0];
            currentPage = Integer.parseInt(data[2].toString());
        }
    }
}
