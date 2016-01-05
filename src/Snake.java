import java.util.ArrayList;
import java.util.List;

/**
 * A snake.
 */
public class Snake extends MoveableBlock {
    private final int AMOUNT_OF_TAILS_TO_ADD = 3;
    private boolean isDead, isAI, inRewind;
    private List<MoveableBlock> tailList;
    private List<Teleporter> teleporterList;
    private List<Food> foodList;
    private List<Snake> snakeList;
    private int foodEaten;
    private SnakeAI snakeAI;

    /**
     * Creates a snake.
     * @param direction the snake's direction
     * @param position the snake's position
     * @param minPosition the snake's min position
     * @param maxPosition the snake's max position
     */
    public Snake(int direction, Position position, Position minPosition, Position maxPosition) {
        super(direction, position, minPosition, maxPosition);

        isDead = false;
        isAI = false;
        inRewind = false;

        tailList = new ArrayList<>();
        teleporterList = new ArrayList<>();
        foodList = new ArrayList<>();
        snakeList = new ArrayList<>();

        foodEaten = 0;

        snakeAI = new SnakeAI(this);
    }

    /**
     * Attaches a list of teleporters for the AI movement.
     * @param teleporterList the list of teleporters
     */
    public void attachTeleporterList(List<Teleporter> teleporterList) {
        this.teleporterList = teleporterList;
    }

    /**
     * Attaches a list of foods for the AI movement.
     * @param foodList the list of foods
     */
    public void attachFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    /**
     * Attaches a list of snakes for the AI movement.
     * @param snakeList the list of snakes
     */
    public void attachSnakeList(List<Snake> snakeList) {
        this.snakeList = snakeList;
    }

    /**
     * Runs the snake.
     * @param respawnOnDeath should the snake repsawn on death
     */
    public void run(boolean respawnOnDeath) {
        if(inRewind) {
            isDead = false;
        }

        if(!isDead) {
            move();

            // if we are rewinding we shouldn't eat food / teleport / check collision
            if(!inRewind) {
                eat();
                checkTeleporter();

                if(collided() && respawnOnDeath) {
                    respawn();
                }
            }
        }
    }

    /**
     * Attempts to eat the food in the snake's food list.
     * @return true if the snake ate a food
     */
    private boolean eat() {
        boolean ate = false;

        for(Food food : foodList) {
            if(food.position.equals(position)) {
                notifySnakeList(food);

                snakeAI.removeGoalFood();

                food.teleport();
                addTails();
                foodEaten++;

                ate = true;
            }
        }

        return ate;
    }

    /**
     * Checks if a position will lead to the snake eating a food.
     * @param position the position
     * @return true if a position will lead to the snake eating a food
     */
    protected boolean willEat(Position position) {
        for(Food food : foodList) {
            if(food.position.equals(position)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Attempts to teleport the snake.
     * @return true on success
     */
    private boolean checkTeleporter() {
        for(Teleporter teleporter : teleporterList) {
            if(teleporter.entranceBlock.position.equals(position)) {
                position = new Position(teleporter.exitBlock.position);

                teleporter.teleportOtherBlock(teleporter.entranceBlock);

                snakeAI.removeGoalFood();

                return true;
            }
            else if(teleporter.exitBlock.position.equals(position)) {
                position = new Position(teleporter.entranceBlock.position);

                teleporter.teleportOtherBlock(teleporter.exitBlock);

                snakeAI.removeGoalFood();

                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the snake will teleport.
     * @return true if the snake will teleport
     */
    private boolean willTeleport() {
        for(Teleporter teleporter : teleporterList) {
            if(teleporter.entranceBlock.position.equals(position) || teleporter.exitBlock.position.equals(position)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Notifies the other snakes that this food was taken.
     * @param food the food that was taken
     */
    public void notifySnakeList(Food food) {
        for(Snake snake : snakeList) {
            Food goalFood = snake.snakeAI.getGoalFood();

            // goal food of another snake was this one
            if(goalFood != null && goalFood.position.equals(food.position)) {
                snake.snakeAI.removeGoalFood();
            }
        }
    }

    /**
     * Adds tails to this snake.
     */
    private void addTails() {
        for(int i = 0; i < AMOUNT_OF_TAILS_TO_ADD; i++) {
            tailList.add(new MoveableBlock(new Position(minPosition.x - maxPosition.x, minPosition.y - maxPosition.y)));
        }
    }

    /**
     * Respawns the snake at its initial position with its initial direction.
     */
    public void respawn() {
        respawn(initialDirection, initialPosition);
    }

    /**
     * Respawns the snake at the given direction and position.
     * @param direction the direction
     * @param position the position
     */
    private void respawn(int direction, Position position) {
        this.direction = direction;
        this.position = position;

        // clear head stack
        clearStacks();

        isDead = false;

        // clear tail stacks
        for(MoveableBlock tail : tailList) {
            tail.clearStacks();
        }
        tailList = new ArrayList<>();

        foodEaten = 0;
        snakeAI.removeGoalFood();
    }

    /**
     * Gets whether or not this snake is an AI.
     * @return true if this snake is an AI
     */
    public boolean isAI() {
        return isAI;
    }

    /**
     * Toggles the AI status of this snake.
     */
    public void toggleAIStatus() {
        isAI = !isAI;
    }

    /**
     * Gets whether or not this snake is dead.
     * @return true if this snake is dead
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Sets the direction of this snake.
     * @param direction the direction
     */
    public void setDirection(int direction) {
        if(isLegalDirectionChange(direction)) {
            this.direction = direction;
        }
    }

    /**
     * Checks if the given direction can be used as the new direction of the snake.
     * @param direction the direction to check
     * @return true if the given direction is legal
     */
    protected boolean isLegalDirectionChange(int direction) {
        return (direction == UP && this.direction != DOWN) || (direction == DOWN && this.direction != UP) ||
                (direction == LEFT && this.direction != RIGHT) || (direction == RIGHT && this.direction != LEFT);
    }

    /**
     * Gets the tail list.
     * @return the tail list
     */
    public List<MoveableBlock> getTailList() {
        return tailList;
    }

    /**
     * Gets the food list.
     * @return the food list
     */
    protected List<Food> getFoodList() {
        return foodList;
    }

    /**
     * Gets the goal food.
     * @return the goal food
     */
    protected Food getGoalFood() {
        return snakeAI.getGoalFood();
    }

    /**
     * Gets the snake list.
     * @return the snake list
     */
    protected List<Snake> getSnakeList() {
        return snakeList;
    }

    /**
     * Gets the amount of food eaten.
     * @return the amount of food eaten
     */
    public int getFoodEaten() {
        return foodEaten;
    }

    /**
     * Checks if the snake collided with its blocks or the bounds.
     * @return true if it collided with its blocks or the bounds
     */
    private boolean collided() {
        if(spotCollided(position)) {
            isDead = true;

            return true;
        }

        return false;
    }

    /**
     * Checks if the snake will collide after going in this direction.
     * @param direction the direciton to try
     * @return true if the snake will collide
     */
    protected boolean spotWillCollide(int direction) {
        Position movedPosition = getMovedPosition(direction, position);

        return spotCollided(movedPosition);
    }

    /**
     * Checks if a position has collided with the snake's blocks or is out of bounds.
     * @param position the position
     * @return true if the position collided with the snake's blocks or is out of bounds
     */
    private boolean spotCollided(Position position) {
        return spotIsInTails(position) || spotIsOutOfBounds(position);
    }

    /**
     * Checks if a position exists in the snake's blocks.
     * @param position the position
     * @return true if the position exists in the snake's blocks
     */
    private boolean spotIsInTails(Position position) {
        for(Block block : tailList) {
            if(block.position.equals(position)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a position is out of bounds.
     * @param position the position
     * @return true if the position is out of bounds
     */
    private boolean spotIsOutOfBounds(Position position) {
        return position.x < minPosition.x || position.x > maxPosition.x - 1 || position.y < minPosition.y || position.y > maxPosition.y - 1;
    }

    /**
     * Moves the snake.
     */
    public void move() {
        if(!inRewind) {
            // move the blocks first
            moveTails();

            // check for AI direction
            if(isAI) {
                setDirection(snakeAI.getAIDirection());
            }

            move(direction);
        }
        else {
            snakeAI.removeGoalFood();

            // keep rewinding the snake until we can't
            if(rewind()) {
                rewindTails();
            }
        }
    }

    /**
     * Moves the snake's tail.
     */
    private void moveTails() {
        // store head's x and y values
        Position previousPosition = position;
        for(MoveableBlock tail : tailList) {
            Position tempPosition = new Position(tail.position);

            tail.pushPosition(tempPosition);

            tail.position = previousPosition;

            // store block's x and y's value
            previousPosition = tempPosition;
        }
    }

    /**
     * Rewinds the snake's tails.
     */
    private void rewindTails() {
        for(int i = tailList.size() - 1; i >= 0; i--) {
            MoveableBlock tail = tailList.get(i);

            if(!tail.rewind()) {
                tailList.remove(i);
            }
        }
    }

    /**
     * Sets rewind.
     * @param inRewind what to set inRewind to
     */
    public void setRewind(boolean inRewind) {
        this.inRewind = inRewind;
    }
}