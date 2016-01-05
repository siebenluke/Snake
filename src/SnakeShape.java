import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A snake shape.
 */
public class SnakeShape {
    private Snake snake;
    private Color color, darkerColor;
    private Rectangle2D.Double snakeRect;

    /**
     * Creates a snake shape.
     * @param snake the snake
     * @param color the snake's color
     */
    public SnakeShape(Snake snake, Color color) {
        this.snake = snake;
        this.color = color;

        // setup colors
        int red = this.color.getRed();
        int green = this.color.getGreen();
        int blue = this.color.getBlue();
        int alpha = this.color.getAlpha();
        int maxAlpha = alpha;
        int minAlpha = (int) (maxAlpha * (50.0 / 100.0));

        darkerColor = new Color(red, green, blue, minAlpha);

        snakeRect = new Rectangle2D.Double();
    }

    /**
     * Draws the snake.
     * @param g2 the graphics2D
     * @param width the snake's width
     * @param height the snake's height
     * @param xMin the snake's min x value
     * @param yMin the snake's min y value
     */
    public void draw(Graphics2D g2, double width, double height, double xMin, double yMin) {
        // make snake darker on death
        Color snakeColor = color;
        Color tailColor = darkerColor;
        if(snake.isDead()) {
            snakeColor = snakeColor.darker();
            tailColor = tailColor.darker();
        }

        // draw the snake's head
        snakeRect.setRect(snake.position.x * width + xMin, snake.position.y * height + yMin, width, height);

        g2.setColor(snakeColor);
        g2.draw(snakeRect);

        g2.setColor(tailColor);
        g2.fill(snakeRect);

        // draw the snake's tails
        g2.setColor(tailColor);
        for(Block block : snake.getTailList()) {
            snakeRect.setRect(block.position.x * width + xMin, block.position.y * height + yMin, width, height);

            g2.draw(snakeRect);
        }
    }

    /**
     * Gets the snake.
     * @return the snake
     */
    public Snake getSnake() {
        return snake;
    }
}

