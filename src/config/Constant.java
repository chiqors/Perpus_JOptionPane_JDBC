package config;

import utils.IconResizer;

import javax.swing.*;

public class Constant {
    public static final String APP_NAME = "Perpustakaan XYZ";
    public static final String DB_HOSTNAME = "localhost";
    public static final String DB_NAME = "perpus_java";
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "admin123";
    public static final int DB_PORT = 5432;
    public static final int PAGE_SIZE = 5;
    public static final String RESOURCE_PATH = "src/resources";
    public static final String APP_ICON_PATH = RESOURCE_PATH + "/icon.png";
    public static final ImageIcon APP_ICON_IMG = IconResizer.resizeIcon(APP_ICON_PATH, 50, 50);
}
