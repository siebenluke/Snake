import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A teleporter shape.
 */
public class TeleporterShape {
    private Teleporter teleporter;
    private Color color;
    private Rectangle2D.Double entranceTeleporterRect, exitTeleporterRect;

    /**
     * Creates a teleporter shape.
     * @param teleporter the teleporter
     * @param color the teleporter's color
     */
    public TeleporterShape(Teleporter teleporter, Color color) {
        this.teleporter = teleporter;
        this.color = color;

        // setup colors
        entranceTeleporterRect = new Rectangle2D.Double();
        exitTeleporterRect = new Rectangle2D.Double();
    }

    /**
     * Draws the teleporter.
     * @param g2 the graphics2D
     * @param width the teleporter's width
     * @param height the teleporter's height
     * @param xMin the teleporter's min x value
     * @param yMin the teleporter's min y value
     */
    public void draw(Graphics2D g2, double width, double height, double xMin, double yMin) {
        g2.setColor(color);
        entranceTeleporterRect.setRect(teleporter.entranceBlock.position.x * width + xMin, teleporter.entranceBlock.position.y * height + yMin, width, height);
        exitTeleporterRect.setRect(teleporter.exitBlock.position.x * width + xMin, teleporter.exitBlock.position.y * height + yMin, width, height);

        g2.fill(entranceTeleporterRect);
        g2.fill(exitTeleporterRect);
    }
}

