package views.menu;

import config.Constant;
import controllers.BookController;
import controllers.MemberController;
import controllers.TransactionController;
import views.books.MenuBookView;

import javax.swing.*;

public class MainMenu {
    public MainMenu() {
        int choice = 0;
        do {
            String title = "Main Menu\n\n";
            String menuData = "1. Pengelolaan Data Buku\n2. Registrasi Member\n3. Peminjaman Buku\n4. Pengembalian Buku\n5. Transaksi\n\n0. Keluar dari aplikasi";
            String menuChoice = "\n\nMasukkan pilihan menu:";

            String menu = JOptionPane.showInputDialog(null, title + menuData + menuChoice, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            if (menu == null) {
                System.exit(0);
            } else if (!menu.matches("[0-9]*")) {
                JOptionPane.showMessageDialog(null, "Input harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            choice = Integer.parseInt(menu);

            switch (choice) {
                case 1:
                    new BookController().displayMenu();
                    break;
                case 2:
                    new MemberController().displayMenu();
                    break;
                case 3:
                    new TransactionController().create();
                    break;
                case 4:
                    new TransactionController().update();
                    break;
                case 5:
                    new TransactionController().displayList();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (choice < 1 || choice > 6);
    }
}
