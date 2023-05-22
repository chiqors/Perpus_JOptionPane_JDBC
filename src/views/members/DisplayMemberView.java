package views.members;

import config.Constant;
import controllers.MemberController;
import models.Member;
import services.MemberService;

import javax.swing.*;
import java.util.List;

public class DisplayMemberView {
    private int currentPage = 1;
    private List<Member> memberList;
    private Object pagedMemberList;

    public DisplayMemberView() {
        displayPagedBookList(currentPage, Constant.PAGE_SIZE);
    }

    public void displayPagedBookList(int page, int pageSize) {
        MemberService bookService = new MemberService();
        Object result = bookService.getPagedMemberList(page, pageSize);

        // Check if the result is an array of objects
        if (result instanceof Object[]) {
            Object[] data = (Object[]) result; // Use typecast, since the compiler doesn't know the type of the array
            memberList = (List<Member>) data[0]; // Use typecast, since the compiler doesn't know the type of the list
            int totalPages = Integer.parseInt(data[1].toString());
            currentPage = Integer.parseInt(data[2].toString());

            // Display the book list in the UI
            int choice = 0;

            do {
                String title = "Daftar Anggota\n";
                String content = String.format("Hal %d dari %d\n\n", currentPage, totalPages);
                StringBuilder memberData = new StringBuilder();
                for (int i = 0; i < memberList.size(); i++) {
                    Member member = memberList.get(i);
                    memberData.append(String.format("%d. %s\n", member.getId(), member.getName()));
                }
                content += memberData + "\n";
                if (currentPage > 1) {
                    content += "q. Hal Sebelumnya\n";
                }
                if (currentPage < totalPages) {
                    content += "e. Hal Berikutnya\n";
                }
                content += "0. Kembali ke menu sebelumnya\n\n";
                content += "Pilih menu: ";

                String input = JOptionPane.showInputDialog(null, title + content, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                // if cancel button is clicked, then return to Main Menu
                if (input == null) {
                    choice = 0;
                } else {
                    if (input.equalsIgnoreCase("q")) {
                        // Go to the previous page
                        currentPage--;
                        pagedMemberList = bookService.getPagedMemberList(currentPage, Constant.PAGE_SIZE);
                        if (pagedMemberList instanceof Object[]) {
                            data = (Object[]) pagedMemberList;
                            memberList = (List<Member>) data[0];
                            totalPages = Integer.parseInt(data[1].toString());
                        }
                    } else if (input.equalsIgnoreCase("e")) {
                        // Go to the next page
                        currentPage++;
                        pagedMemberList = bookService.getPagedMemberList(currentPage, Constant.PAGE_SIZE);
                        if (pagedMemberList instanceof Object[]) {
                            data = (Object[]) pagedMemberList;
                            memberList = (List<Member>) data[0];
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
                new MemberController().displayMenu();
            }
        } else {
            // Handle the case where the result is not as expected
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data buku", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
}
