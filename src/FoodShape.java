import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A food shape.
 */
public class FoodShape {
    private Food food;
    private Color color;
    private Rectangle2D.Double foodRect;

    /**
     * Creates a food shape.
     * @param food the food
     * @param color the food's color
     */
    public FoodShape(Food food, Color color) {
        this.food = food;
        this.color = color;

        foodRect = new Rectangle2D.Double();
    }

    /**
     * Draws the food.
     * @param g2 the graphics2D
     * @param width the food's width
     * @param height the food's height
     * @param xMin the food's min x value
     * @param yMin the food's min y value
     */
    public void draw(Graphics2D g2, double width, double height, double xMin, double yMin) {
        foodRect.setRect(food.position.x * width + xMin, food.position.y * height + yMin, width, height);

        g2.setColor(color);
        g2.draw(foodRect);
    }
}

