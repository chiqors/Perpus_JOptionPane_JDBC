package views.menu;

import config.Constant;
import controllers.BookController;

import javax.swing.*;

public class BookManagementMenu {
    public BookManagementMenu() {
        int choice = 0;
        do {
            String title = "Pengelolaan Data Buku\n\n";
            String menuData = "1. List Buku\n2. Cari Buku\n3. Tambah Buku\n4. Edit Buku\n5. Hapus Buku\n\n0. Kembali";
            String menu = JOptionPane.showInputDialog(null, title + menuData, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to Main Menu
            if (menu == null) {
                choice = 0;
            } else if (!menu.matches("[0-9]*")) { // if user inputted alphabet or symbol, then show error message
                JOptionPane.showMessageDialog(null, "Input harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            choice = Integer.parseInt(menu);

            switch (choice) {
                case 0:
                    new MainMenu(); // Create a new MainMenu instance
                    return; // Exit the method
                case 1:
                    new BookController().displayList();
                    break;
                case 2:
                    new BookController().displaySearch();
                    break;
            }
        } while (choice < 1 || choice > 5);
    }
}
