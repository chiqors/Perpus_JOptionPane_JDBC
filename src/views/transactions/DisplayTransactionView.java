package views.transactions;

import config.Constant;
import models.Transaction;
import services.TransactionService;
import views.menu.MainMenu;

import javax.swing.*;
import java.util.List;

public class DisplayTransactionView {
    private int currentPage = 1;
    private List<Transaction> transactionList;
    private Object pagedTransactionList;

    public DisplayTransactionView() {
        displayPagedTransactionList(currentPage, 2);
    }

    public void displayPagedTransactionList(int page, int pageSize) {
        TransactionService transactionService = new TransactionService();
        Object result = transactionService.getPagedTransactionList(page, pageSize);

        // Check if the result is an array of objects
        if (result instanceof Object[]) {
            Object[] data = (Object[]) result; // Use typecast, since the compiler doesn't know the type of the array
            transactionList = (List<Transaction>) data[0]; // Use typecast, since the compiler doesn't know the type of the list
            int totalPages = Integer.parseInt(data[1].toString());
            currentPage = Integer.parseInt(data[2].toString());

            // Display the book list in the UI
            int choice = 0;

            do {
                String title = "Daftar Transaksi\n";
                String content = String.format("Hal %d dari %d\n", currentPage, totalPages);
                StringBuilder transactionData = new StringBuilder();
                for (int i = 0; i < transactionList.size(); i++) {
                    Transaction transaction = transactionList.get(i);
                    transactionData.append(String.format("%s", transaction.toString()));
                }
                content += transactionData+"\n";
                if (currentPage > 1) {
                    content += "Q. Hal Sebelumnya\n";
                }
                if (currentPage < totalPages) {
                    content += "E. Hal Berikutnya\n";
                }
                content += "\nMenu:\n";
                content += "0. Kembali ke menu sebelumnya";

                String input = JOptionPane.showInputDialog(null, title + content, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                // if cancel button is clicked, then return to Main Menu
                if (input == null) {
                    choice = 0;
                } else {
                    if (input.equalsIgnoreCase("q")) {
                        // Go to the previous page
                        currentPage--;
                        pagedTransactionList = transactionService.getPagedTransactionList(currentPage, Constant.PAGE_SIZE);
                        if (pagedTransactionList instanceof Object[]) {
                            data = (Object[]) pagedTransactionList;
                            transactionList = (List<Transaction>) data[0];
                            totalPages = Integer.parseInt(data[1].toString());
                        }
                    } else if (input.equalsIgnoreCase("e")) {
                        // Go to the next page
                        currentPage++;
                        pagedTransactionList = transactionService.getPagedTransactionList(currentPage, Constant.PAGE_SIZE);
                        if (pagedTransactionList instanceof Object[]) {
                            data = (Object[]) pagedTransactionList;
                            transactionList = (List<Transaction>) data[0];
                            totalPages = Integer.parseInt(data[1].toString());
                        }
                        // Update the choice to continue the loop
                        choice = 1;
                    } else if (input.equals("0")) {
                        // Go back to the previous menu
                        choice = 0;
                    }
                }
            } while (choice != 0);

            if (choice == 0) {
                new MainMenu();
            }
        } else {
            // Handle the case where the result is not as expected
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data buku", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
}
