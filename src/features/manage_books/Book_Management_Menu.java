package features.manage_books;

import javax.swing.*;

import config.Constant;
import features.Main_Menu;

public class Book_Management_Menu {
    public Book_Management_Menu() {
        int choice = 0;
        do {
            String title = "Pengelolaan Data Buku\n\n";
            String menuData = "1. List Buku\n2. Cari Buku\n3. Tambah Buku\n4. Edit Buku\n5. Hapus Buku\n\n0. Kembali";
            String menu = JOptionPane.showInputDialog(null, title + menuData, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            // if cancel button is clicked, then return to Main Menu
            if (menu == null) {
                choice = 0;
                new Main_Menu();
            }
            choice = Integer.parseInt(menu);

            switch (choice) {
                case 1:
                    new List_Books();
                    break;
                case 2:
                    new Search_Book();
                    break;
                case 3:
                    new Add_Book();
                    break;
                case 4:
                    new Edit_Book();
                    break;
                case 5:
                    new Delete_Book();
                    break;
                case 0:
                    new Main_Menu();
            }
        } while (choice < 1 || choice > 5);
    }
}
