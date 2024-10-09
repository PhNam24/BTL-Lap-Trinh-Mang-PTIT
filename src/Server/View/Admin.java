package Server.View;

import javax.swing.*;
import java.awt.*;

public class Admin extends JFrame implements Runnable {

    @Override
    public void run() {
        new Admin().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Admin adminPanel = new Admin();
            adminPanel.setVisible(true);
        });
    }
}
