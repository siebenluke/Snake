import java.util.Random;

/**
 * A block.
 */
public class Block {
    public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, DEFAULT_DIRECTION = RIGHT;
    protected int direction, initialDirection;
    protected Position position, initialPosition, minPosition, maxPosition;
    private Random random;


    /**
     * Creates a block with direction set to DEFAULT_DIRECTION and positions set to 0, 0.
     */
    public Block() {
        this(new Position());
    }

    /**
     * Creates a block with the given direction and positions set to 0, 0.
     * @param direction the direction
     */
    public Block(int direction) {
        this(direction, new Position());
    }

    /**
     * Creates a block with direction set to DEFAULT_DIRECTION and given position with the min and max positions set to 0, 0.
     * @param position the position
     */
    public Block(Position position) {
        this(position, new Position(), new Position());
    }

    /**
     * Creates a block with the given direction and position with the min and max positions set to 0,0.
     * @param direction the direction
     * @param position the position
     */
    public Block(int direction, Position position) {
        this(direction, position, new Position(), new Position());
    }

    /**
     * Creates a block with the given min and max positions with position set to 0, 0 and direction set to DEFAULT_DIRECTION.
     * @param minPosition the block's min position
     * @param maxPosition the block's max position
     */
    public Block(Position minPosition, Position maxPosition) {
        this(new Position(), minPosition, maxPosition);
    }

    /**
     * Creates a block with the given positions and direction set to DEFAULT_DIRECTION.
     * @param position the block's position
     * @param minPosition the block's min position
     * @param maxPosition the block's max position
     */
    public Block(Position position, Position minPosition, Position maxPosition) {
        this(DEFAULT_DIRECTION, position, minPosition, maxPosition);
    }

    /**
     * Creates a block.
     * @param direction the block's direction
     * @param position the block's position
     * @param minPosition the block's min position
     * @param maxPosition the block's max position
     */
    public Block(int direction, Position position, Position minPosition, Position maxPosition) {
        this.direction = direction;
        initialDirection = this.direction;

        this.position = position;
        initialPosition = new Position(this.position.x, this.position.y);

        this.minPosition = minPosition;
        this.maxPosition = maxPosition;

        random = new Random();
    }

    /**
     * Teleports the block to a random position within its min and max positions.
     */
    public void teleport() {
        position.x = random.nextInt(maxPosition.x) + minPosition.x;
        position.y = random.nextInt(maxPosition.y) + minPosition.y;
    }
}