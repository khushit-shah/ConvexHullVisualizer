import javax.swing.*;
import java.awt.*;

class Window extends JFrame {

    Window(String title, int width, int height, Main main) {
        setTitle(title);

        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Max screen / full screen with top panel.
        add(main);

        setVisible(true);

        main.start();
    }

}