package views.books;

import config.Constant;
import controllers.BookController;
import views.menu.MainMenu;

import javax.swing.*;

public class MenuBookView {
    public MenuBookView() {
        int choice = 0;
        do {
            String title = "Pengelolaan Data Buku\n\n";
            String menuData = "1. List Buku\n2. Cari Buku\n3. Tambah Buku\n4. Edit Buku\n5. Hapus Buku\n\n0. Kembali ke menu utama";
            String menuChoice = "\n\nMasukkan pilihan menu:";

            String menu = JOptionPane.showInputDialog(null, title + menuData + menuChoice, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            if (menu != null) {
                if (!menu.matches("[0-9]*")) {
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
                        new BookController().search();
                        break;
                    case 3:
                        new BookController().create();
                        break;
                    case 4:
                        new BookController().edit();
                        break;
                    case 5:
                        new BookController().destroy();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Pilihan menu tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } else {
                new MainMenu(); // Create a new MainMenu instance
                return; // Exit the method
            }
        } while (choice < 1 || choice > 5);
    }
}
