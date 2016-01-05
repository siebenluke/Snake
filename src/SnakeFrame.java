import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * A frame.
 */
public class SnakeFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake 2015-10-11");
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new SnakePanel());

        // add frame icon
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        URL url = SnakeFrame.class.getResource("Snake.png");
        if(url != null) {
            Image image = toolkit.getImage(url);
            if(image != null) {
                frame.setIconImage(image);
            }
        }

        frame.setVisible(true);
    }
}