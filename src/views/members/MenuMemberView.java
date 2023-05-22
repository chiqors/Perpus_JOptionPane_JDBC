package views.members;

import config.Constant;
import controllers.MemberController;
import views.menu.MainMenu;

import javax.swing.*;

public class MenuMemberView {
    public MenuMemberView() {
        int choice;
        do {
            String title = "Pengelolaan Data Anggota\n";
            String content = "1. List Anggota\n2. Tambah Anggota\n\n0. Kembali";
            content += "\n\nMasukkan pilihan menu: ";
            String menu = JOptionPane.showInputDialog(null, title + content, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            if (menu == null) {
                choice = 0;
            } else {
                if (menu.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Masukkan pilihan menu!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                    choice = -1;
                } else if (!menu.matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(null, "Masukkan pilihan menu yang valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                    choice = -1;
                } else {
                    choice = Integer.parseInt(menu);

                    switch (choice) {
                        case 0:
                            break;
                        case 1:
                            new MemberController().displayList();
                            break;
                        case 2:
                            new MemberController().create();
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Masukkan pilihan menu yang valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                            choice = -1;
                            break;
                    }
                }
            }
        } while (choice != 0);

        new MainMenu();
    }
}
