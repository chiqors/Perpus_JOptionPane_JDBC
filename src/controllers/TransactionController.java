package controllers;

import services.TransactionService;
import views.transactions.CreateTransactionView;
import views.transactions.DisplayTransactionView;
import views.transactions.UpdateTransactionView;

public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController() {
        transactionService = new TransactionService();
    }

    // -----------------------------

    public void displayList() {
        new DisplayTransactionView();
    }

    public void create() {
         new CreateTransactionView();
    }

    public void update() {
        new UpdateTransactionView();
    }
}
