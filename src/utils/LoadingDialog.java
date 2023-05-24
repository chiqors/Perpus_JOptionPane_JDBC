package utils;

import config.Constant;

import javax.swing.*;

public class LoadingDialog extends JDialog {
    public LoadingDialog() {
        setIconImage(new ImageIcon(getClass().getResource("/resources/icon.png")).getImage());
        setTitle(Constant.APP_NAME);
        setSize(200, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JLabel label = new JLabel("Loading...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
    }

    public void showLoading() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            setAlwaysOnTop(true);
        });
    }

    public void hideLoading() {
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            dispose();
        });
    }
}
