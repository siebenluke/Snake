/**
 * A food.
 */
public class Food extends Block {
    /**Block
     * Creates a food.
     * @param minPosition the food's min position
     * @param maxPosition the food's max position
     */
    public Food(Position minPosition, Position maxPosition) {
        super(minPosition, maxPosition);

        teleport();
    }
}