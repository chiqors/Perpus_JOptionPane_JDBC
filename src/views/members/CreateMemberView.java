package views.members;

import config.Constant;
import controllers.MemberController;
import models.Member;
import services.MemberService;

import javax.swing.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CreateMemberView {
    public CreateMemberView() {
        MemberService memberService = new MemberService();
        boolean isCreating = true;
        do {
            String title = "Tambah Anggota\n\n";
            String askMemberName = "Masukkan nama anggota: ";
            String askEmail = "Masukkan email: ";
            String askPhone = "Masukkan nomor telepon: ";

            String memberName = JOptionPane.showInputDialog(null, title + askMemberName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (memberName == null) {
                isCreating = false;
                break;
            } else if (memberName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama anggota tidak boleh kosong!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            }

            String memberEmail = JOptionPane.showInputDialog(null, title + askEmail, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (memberEmail == null) {
                isCreating = false;
                break;
            } else if (memberEmail.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Email tidak boleh kosong!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            } else if (!memberEmail.matches("^(.+)@(.+)$")) {
                JOptionPane.showMessageDialog(null, "Format email tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            }

            String memberPhone = JOptionPane.showInputDialog(null, title + askPhone, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (memberPhone == null) {
                isCreating = false;
                break;
            } else if (memberPhone.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nomor telepon tidak boleh kosong!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                isCreating = true;
                continue;
            }

            // registered_at auto-generated
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String registered_at = formatter.format(date);

            isCreating = false;
            Member member = new Member(memberName, memberEmail, memberPhone, registered_at);
            memberService.addMember(member);
        } while (isCreating);

        if (!isCreating) {
            new MemberController().displayMenu();
        }
    }
}
