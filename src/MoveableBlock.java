import java.util.ArrayDeque;
import java.util.Deque;

public class MoveableBlock extends Block {
    public static final int MAX_REWIND_SIZE = Integer.MAX_VALUE;
    private Deque<Position> positionDeque;
    private Deque<Integer> directionDeque;

    /**
     * Creates a moveableBlock with direction set to DEFAULT_DIRECTION and given position with the min and max positions set to 0, 0.
     * @param position the position
     */
    public MoveableBlock(Position position) {
        this(DEFAULT_DIRECTION, position, new Position(), new Position());
    }

    /**
     * Creates a moveableBlock.
     * @param direction the block's direction
     * @param position the block's position
     * @param minPosition the block's min position
     * @param maxPosition the block's max position
     */
    public MoveableBlock(int direction, Position position, Position minPosition, Position maxPosition) {
        super(direction, position, minPosition, maxPosition);

        positionDeque = new ArrayDeque<>();
        directionDeque = new ArrayDeque<>();
    }

    /**
     * Moves the block in its current direction.
     */
    public void move() {
        move(direction);
    }

    /**
     * Moves the block.
     * @param direction the direction to move in
     */
    public void move(int direction) {
        positionDeque.push(position);
        directionDeque.push(direction);

        capRewindAmount();

        position = getMovedPosition(direction, position);
    }

    /**
     * Caps the rewind amount.
     */
    public void capRewindAmount() {
        if(positionDeque.size() > MAX_REWIND_SIZE) {
            positionDeque.removeLast();
            directionDeque.removeLast();
        }
    }

    /**
     * Rewinds the block.
     * @return true if the block could rewind
     */
    public boolean rewind() {
        if(!positionDeque.isEmpty()) {
            position = positionDeque.pop();
            direction = directionDeque.pop();

            return true;
        }

        return false;
    }

    /**
     * Adds a position and the current direction to stacks without enforcing the cap amount.
     * @param position the position
     */
    public void pushPosition(Position position) {
        positionDeque.push(position);
        directionDeque.push(direction);
    }

    /**
     * Clears the stacks.
     */
    public void clearStacks() {
        positionDeque.clear();
        directionDeque.clear();
    }

    /**
     * Gets the moved position.
     * @return the position after moving in the given direction
     */
    public Position getMovedPosition() {
        return getMovedPosition(direction, new Position(position.x, position.y));
    }

    /**
     * Gets the moved position.
     * @param direction the direction to move in
     * @return the position after moving in the given direction
     */
    public Position getMovedPosition(int direction) {
        return getMovedPosition(direction, new Position(position.x, position.y));
    }

    /**
     * Gets the moved position.
     * @param direction the direction to move in
     * @param position the position to move from
     * @return the position after moving in the given direction
     */
    public Position getMovedPosition(int direction, Position position) {
        Position movedPosition = new Position(position.x, position.y);

        if(direction == LEFT) {
            movedPosition.x--;
        }
        else if(direction == RIGHT) {
            movedPosition.x++;
        }
        else if(direction == UP) {
            movedPosition.y--;
        }
        else if(direction == DOWN) {
            movedPosition.y++;
        }

        return movedPosition;
    }

    /**
     * Gets the opposite direction.
     * @return the opposite direction
     */
    public int getOppositeDirection() {
        return getOppositeDirection(direction);
    }

    /**
     * Gets the opposite direction of the given direction.
     * @param direction the direction
     * @return the opposite direction
     */
    public int getOppositeDirection(int direction) {
        if(direction == LEFT) {
            return RIGHT;
        }
        else if(direction == RIGHT) {
            return LEFT;
        }
        else if(direction == UP) {
            return DOWN;
        }
        else if(direction == DOWN) {
            return UP;
        }

        return direction;
    }
}
