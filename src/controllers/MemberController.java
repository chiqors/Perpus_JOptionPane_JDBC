package controllers;

import services.MemberService;
import views.members.DisplayMemberView;
import views.members.MenuMemberView;

public class MemberController {
    private final MemberService memberService;

    public MemberController() {
        memberService = new MemberService();
    }

    // -----------------------------

    public void displayMenu() {
        new MenuMemberView();
    }

    public void displayList() {
        new DisplayMemberView();
    }
}
