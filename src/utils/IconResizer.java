package utils;

import javax.swing.ImageIcon;
import java.awt.Image;

public class IconResizer {
    public static ImageIcon resizeIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}