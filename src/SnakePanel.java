import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A snake panel.
 */
public class SnakePanel extends JPanel {
    public static final int DELAY = 50;
    private final Position MIN_POSITION = new Position(0, 0), MAX_POSITION = new Position(62 * 2, 34 * 2);
    public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, TOGGLE_AI_RESPAWN = 4, REVERSE = 5;
    public static final int MAX_COLOR = 255, MID_COLOR = (int) (MAX_COLOR / 2.0), MIN_COLOR = 0;
    public static final Color BACKGROUND_COLOR = new Color(MIN_COLOR, MIN_COLOR, MIN_COLOR),
            EDGES_COLOR = new Color(MAX_COLOR, MAX_COLOR, MAX_COLOR, 64), ROW_GRID_COLOR = new Color(MAX_COLOR, MAX_COLOR, MAX_COLOR, 16),
            COLUMN_GRID_COLOR = new Color(MAX_COLOR, MAX_COLOR, MAX_COLOR, 16), FOOD_COLOR = new Color(MAX_COLOR, MAX_COLOR, MAX_COLOR, 255);;
    private List<Color> snakeColorList;
    private Line2D.Double line;
    private List<Teleporter> teleporterList;
    private List<TeleporterShape> teleporterShapeList;
    private List<Food> foodList;
    private List<FoodShape> foodShapeList;
    private List<Snake> snakeList;
    private List<SnakeShape> snakeShapeList;
    private List<Integer> snakeHighScoreList;
    private Timer gameTimer;
    private boolean paused, showPausedText, respawnOnDeath;

    /**
     * Creates a snake panel.
     */
    public SnakePanel() {
        paused = true;
        showPausedText = true;
        respawnOnDeath = false;
        line = new Line2D.Double();

        // setup teleporter lists
        teleporterList = new ArrayList<>();
        teleporterShapeList = new ArrayList<>();

        int teleporterAlpha = 255;
        List<Color> teleporterColorList = new ArrayList<>();
        teleporterColorList.add(new Color(MAX_COLOR, MID_COLOR, MAX_COLOR, teleporterAlpha));
        teleporterColorList.add(new Color(MIN_COLOR, MID_COLOR, MAX_COLOR, teleporterAlpha));
        teleporterColorList.add(new Color(MAX_COLOR, MID_COLOR, MIN_COLOR, teleporterAlpha));
        teleporterColorList.add(new Color(MIN_COLOR, MAX_COLOR, MID_COLOR, teleporterAlpha));

        for(int i = 0; i < teleporterColorList.size(); i++) {
            Teleporter teleporter = new Teleporter(MIN_POSITION, MAX_POSITION);
            teleporterList.add(teleporter);
            teleporterShapeList.add(new TeleporterShape(teleporter, teleporterColorList.get(i)));
        }

        // setup food lista
        foodList = new ArrayList<>();
        foodShapeList = new ArrayList<>();
        int foodAmount = 8;
        for(int i = 0; i < foodAmount; i++) {
            addFood();
        }

        // setup snake lista
        snakeColorList = new ArrayList<>();
        int snakeAlpha = 255;
        snakeColorList.add(new Color(MAX_COLOR, MIN_COLOR, MIN_COLOR, snakeAlpha));
        snakeColorList.add(new Color(MIN_COLOR, MAX_COLOR, MIN_COLOR, snakeAlpha));
        snakeColorList.add(new Color(MIN_COLOR, MAX_COLOR, MAX_COLOR, snakeAlpha));
        snakeColorList.add(new Color(MAX_COLOR, MAX_COLOR, MIN_COLOR, snakeAlpha));

        snakeList = new ArrayList<>();
        snakeShapeList = new ArrayList<>();
        snakeHighScoreList = new ArrayList<>();

        int midX = (int) (MAX_POSITION.x / 2.0 + MAX_POSITION.x  % 2);
        int midY = (int) (MAX_POSITION.y  / 2.0 + MAX_POSITION.y % 2);
        snakeList.add(new Snake(Block.LEFT, new Position(midX - 1, midY - 1), MIN_POSITION, MAX_POSITION));
        snakeList.add(new Snake(Block.LEFT, new Position(midX + 1, midY - 1), MIN_POSITION, MAX_POSITION));
        snakeList.add(new Snake(Block.RIGHT, new Position(midX - 1, midY + 1), MIN_POSITION, MAX_POSITION));
        snakeList.add(new Snake(Block.RIGHT, new Position(midX + 1, midY + 1), MIN_POSITION, MAX_POSITION));

        // add additional info to snakeLists
        for(int i = 0; i < snakeList.size(); i++) {
            Snake snake = snakeList.get(i);

            snake.attachTeleporterList(teleporterList);
            snake.attachFoodList(foodList);
            snake.attachSnakeList(snakeList);
            snake.toggleAIStatus();

            snakeShapeList.add(new SnakeShape(snake, snakeColorList.get(i)));

            snakeHighScoreList.add(0);
        }

        // snake timer
        gameTimer = new Timer(DELAY, null);
        gameTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Snake snake : snakeList) {
                    snake.run(respawnOnDeath);
                }

                repaint();
            }
        });

        // setup keys
        final List<List<Integer>> keyCodeListList = new ArrayList<>();
        List<Integer> keyCodeList1 = new ArrayList<>();
        keyCodeList1.add(KeyEvent.VK_W);
        keyCodeList1.add(KeyEvent.VK_S);
        keyCodeList1.add(KeyEvent.VK_A);
        keyCodeList1.add(KeyEvent.VK_D);
        keyCodeList1.add(KeyEvent.VK_E);
        keyCodeList1.add(KeyEvent.VK_Q);
        keyCodeListList.add(keyCodeList1);

        List<Integer> keyCodeList2 = new ArrayList<>();
        keyCodeList2.add(KeyEvent.VK_Y);
        keyCodeList2.add(KeyEvent.VK_H);
        keyCodeList2.add(KeyEvent.VK_G);
        keyCodeList2.add(KeyEvent.VK_J);
        keyCodeList2.add(KeyEvent.VK_U);
        keyCodeList2.add(KeyEvent.VK_T);
        keyCodeListList.add(keyCodeList2);

        List<Integer> keyCodeList3 = new ArrayList<>();
        keyCodeList3.add(KeyEvent.VK_P);
        keyCodeList3.add(KeyEvent.VK_SEMICOLON);
        keyCodeList3.add(KeyEvent.VK_L);
        keyCodeList3.add(KeyEvent.VK_QUOTE);
        keyCodeList3.add(KeyEvent.VK_OPEN_BRACKET);
        keyCodeList3.add(KeyEvent.VK_O);
        keyCodeListList.add(keyCodeList3);

        List<Integer> keyCodeList4 = new ArrayList<>();
        keyCodeList4.add(KeyEvent.VK_UP);
        keyCodeList4.add(KeyEvent.VK_DOWN);
        keyCodeList4.add(KeyEvent.VK_LEFT);
        keyCodeList4.add(KeyEvent.VK_RIGHT);
        keyCodeList4.add(KeyEvent.VK_ENTER);
        keyCodeList4.add(KeyEvent.VK_SHIFT);
        keyCodeListList.add(keyCodeList4);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                // snake commands
                for(int i = 0; i < snakeList.size(); i++) {
                    List<Integer> keyCodeList = keyCodeListList.get(i);
                    Snake snake = snakeList.get(i);

                    // commands if the game is not pause and the timer is running
                    if(!paused && !snake.isDead()) {
                        if(keyCode == keyCodeList.get(UP)) {
                            snake.setDirection(Snake.UP);
                        }
                        else if(keyCode == keyCodeList.get(DOWN)) {
                            snake.setDirection(Snake.DOWN);
                        }
                        else if(keyCode == keyCodeList.get(LEFT)) {
                            snake.setDirection(Snake.LEFT);
                        }
                        else if(keyCode == keyCodeList.get(RIGHT)) {
                            snake.setDirection(Snake.RIGHT);
                        }
                    }

                    // commands that can happen at any time
                    if(keyCode == keyCodeList.get(TOGGLE_AI_RESPAWN)) {
                        if(!paused) {
                            if(snake.isDead()) {
                                snake.respawn();
                            }
                            else {
                                snake.toggleAIStatus();
                            }
                        }
                        else {
                            snake.toggleAIStatus();
                        }
                    }
                    else if(keyCode == keyCodeList.get(REVERSE)) {
                        snake.setRewind(true);
                    }
                }

                // commands that can execute at any time
                if(keyCode == KeyEvent.VK_ESCAPE) {
                    toggleTimers();
                }
                else if(keyCode == KeyEvent.VK_SPACE) {
                    toggleShowPausedText();
                }
                else if(keyCode == KeyEvent.VK_BACK_SPACE) {
                    restart();
                }
                else if(keyCode == KeyEvent.VK_1 || keyCode == KeyEvent.VK_NUMPAD1) {
                    decreaseDelay();
                }
                else if(keyCode == KeyEvent.VK_2 || keyCode == KeyEvent.VK_NUMPAD2) {
                    increaseDelay();
                }
                else if(keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_NUMPAD3) {
                    removeFood();
                }
                else if(keyCode == KeyEvent.VK_4 || keyCode == KeyEvent.VK_NUMPAD4) {
                    addFood();
                }
                else if(keyCode == KeyEvent.VK_0 || keyCode == KeyEvent.VK_NUMPAD0) {
                    toggleRespawnOnDeath();
                }

                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();

                // snake command
                for (int i = 0; i < snakeList.size(); i++) {
                    List<Integer> keyCodeList = keyCodeListList.get(i);
                    Snake snake = snakeList.get(i);

                    // command that can happen at any time
                    if(keyCode == keyCodeList.get(REVERSE)) {
                        snake.setRewind(false);
                    }
                }
            }
        });
        setFocusable(true);
    }

    /**
     * Toggles respawn on death and respawns any dead snakes.
     */
    private void toggleRespawnOnDeath() {
        respawnOnDeath = !respawnOnDeath;

        // respawn any dead snakes
        if(respawnOnDeath) {
            for(Snake snake : snakeList) {
                if(snake.isDead()) {
                    snake.respawn();
                }
            }
        }
    }

    /**
     * Restarts the game.
     */
    private void restart() {
        for(Snake snake : snakeList) {
            snake.respawn();
        }

        for(Food food : foodList) {
            food.teleport();
        }

        for(Teleporter teleporter : teleporterList) {
            teleporter.teleport();
        }

        gameTimer.stop();

        paused = true;
        repaint();
    }

    /**
     * Adds a food.
     */
    private void addFood() {
        Food food = new Food(MIN_POSITION, MAX_POSITION);
        foodList.add(food);

        foodShapeList.add(new FoodShape(food, FOOD_COLOR));
    }

    /**
     * Removes a food.
     * NOTE: The minimum food amount is 1.
     */
    private void removeFood() {
        int foodListSize = foodList.size();

        if(foodListSize > 1) {
            Food removedFood = foodList.remove(foodListSize - 1);

            // notify snakes of removal
            for(Snake snake : snakeList) {
                snake.notifySnakeList(removedFood);
            }

            foodShapeList.remove(foodListSize - 1);
        }
    }

    /**
     * Decreases the timers' delay by 1.
     * NOTE: The minimum delay is 1.
     */
    private void decreaseDelay() {
        int delay = gameTimer.getDelay();

        if(delay > 1) {
            gameTimer.setDelay(delay - 1);
        }
    }

    /**
     * Increases the timers' delay by 1.
     */
    private void increaseDelay() {
        int delay = gameTimer.getDelay();
        gameTimer.setDelay(delay + 1);
    }

    /**
     * Toggles the snakes' timers.
     */
    private void toggleTimers() {
        paused = !paused;

        if(paused) {
            gameTimer.stop();
        }
        else {
            gameTimer.start();
        }

        repaint();
    }

    /**
     * Toggles show paused text.
     */
    private void toggleShowPausedText() {
        showPausedText = !showPausedText;
    }

    /**
     * Paints this panel.
     * @param g the graphics
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        double textPercentage = 5.0;
        double scoreRectHeight = height * (textPercentage / 100.0);

        double worldWidth = width;
        double worldHeight = height - scoreRectHeight;

        // draw background
        Rectangle2D.Double backgroundRect = new Rectangle2D.Double(0, 0, width, height);
        g2.setColor(BACKGROUND_COLOR);
        g2.fill(backgroundRect);

        // setup slices
        double sliceWidth = worldWidth / (MAX_POSITION.x + 2);
        double sliceHeight = worldHeight / (MAX_POSITION.y + 2);

        // draw edges of world
        g2.setColor(EDGES_COLOR);

        Rectangle2D.Double topEdgeRect = new Rectangle2D.Double(sliceWidth, 0, worldWidth - (sliceWidth * 2), sliceHeight);
        g2.fill(topEdgeRect);

        Rectangle2D.Double bottomEdgeRect = new Rectangle2D.Double(sliceWidth, worldHeight - sliceHeight, worldWidth - (sliceWidth * 2), sliceHeight);
        g2.fill(bottomEdgeRect);

        Rectangle2D.Double leftEdgeRect = new Rectangle2D.Double(0, 0, sliceWidth, worldHeight);
        g2.fill(leftEdgeRect);

        Rectangle2D.Double rightEdgeRect = new Rectangle2D.Double(worldWidth - sliceWidth, 0, sliceWidth, worldHeight);
        g2.fill(rightEdgeRect);

        // draw grid
        g2.setColor(ROW_GRID_COLOR);
        double xStart = sliceWidth;
        double xEnd = worldWidth - sliceWidth;
        for(int row = 1; row < MAX_POSITION.y; row++) {
            double y = sliceHeight + (row * sliceHeight);
            line.setLine(xStart, y, xEnd, y);
            g2.draw(line);
        }

        g2.setColor(COLUMN_GRID_COLOR);
        double yStart = sliceHeight;
        double yEnd = worldHeight - sliceHeight;
        for(int column = 1; column < MAX_POSITION.x; column++) {
            double x = sliceWidth + (column * sliceWidth);
            line.setLine(x, yStart, x, yEnd);
            g2.draw(line);
        }

        // draw dead snakes
        for(SnakeShape snakeShape : snakeShapeList) {
            if(snakeShape.getSnake().isDead()) {
                snakeShape.draw(g2, sliceWidth, sliceHeight, sliceWidth, sliceHeight);
            }
        }

        // draw alive snakes
        for(SnakeShape snakeShape : snakeShapeList) {
            if(!snakeShape.getSnake().isDead()) {
                snakeShape.draw(g2, sliceWidth, sliceHeight, sliceWidth, sliceHeight);
            }
        }

        // draw teleporters
        for(TeleporterShape teleporterShape : teleporterShapeList) {
            teleporterShape.draw(g2, sliceWidth, sliceHeight, sliceWidth, sliceHeight);
        }

        // draw food
        for(FoodShape foodShape : foodShapeList) {
            foodShape.draw(g2, sliceWidth, sliceHeight, sliceWidth, sliceHeight);
        }

        // draw scores' background
        int scoreAlpha = 0;
        Color scoreBackgroundColor = new Color(MAX_COLOR, MAX_COLOR, MAX_COLOR, scoreAlpha);
        g2.setColor(scoreBackgroundColor);

        Rectangle2D.Double textBackgroundRect = new Rectangle2D.Double(0, height - scoreRectHeight, worldWidth, scoreRectHeight);
        g2.fill(textBackgroundRect);

        // draw scores
        double textHeight = scoreRectHeight / 2.0;
        double descent = textHeight / 5.0;
        g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, (int) textHeight));

        // setup high scores
        for(int i = 0; i < snakeList.size(); i++) {
            Snake snake = snakeList.get(i);
            int snakeHighScore = snakeHighScoreList.remove(i);

            int snakeScore = snake.getTailList().size();
            if(snakeScore > snakeHighScore) {
                snakeHighScore = snakeScore;
            }

            snakeHighScoreList.add(i, snakeHighScore);
        }

        // setup score texts
        for(int i = 0; i < snakeList.size(); i++) {
            Snake snake = snakeList.get(i);
            int snakeScore = snake.getTailList().size();
            int snakeHighScore = snakeHighScoreList.get(i);
            Color fontColor = snakeColorList.get(i);

            String aiMarker = snake.isAI() ? "*" : " ";
            String text = aiMarker + "Score: " + String.format("%5d", snakeScore) + " High Score: " + String.format("%5d", snakeHighScore);

            // make score text darker on death
            if(snake.isDead()) {
                g2.setColor(fontColor.darker().darker());
            }
            else {
                g2.setColor(fontColor);
            }

            if(i == 0) {
                g2.drawString(text, 0, (int) (height - scoreRectHeight + textHeight - descent));
            }
            else if (i == 1) {
                g2.drawString(text, (int) (width / 2.0), (int) (height - scoreRectHeight + textHeight - descent));
            }
            else if(i == 2) {
                g2.drawString(text, 0, (int) (height - descent));
            }
            else if(i == 3) {
                g2.drawString(text, (int) (width / 2.0), (int) (height - descent));
            }
        }

        // draw statuses
        Color statusColor = new Color(MAX_COLOR, MAX_COLOR, MAX_COLOR, 200);
        g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, (int) textHeight));

        g2.setColor(statusColor);

        String respawnOnDeathStr = respawnOnDeath ? " Respawn Snakes On Death:    On" : " Respawn Snakes On Death:   Off";
        g2.drawString(respawnOnDeathStr, 0, (int) worldHeight);

        String numberStats = String.format(" Delay: %5d Food Count: %5d", gameTimer.getDelay(), foodList.size());
        g2.drawString(numberStats, (int) (width / 2.0), (int) worldHeight);

        // draw pause menu
        if(paused && showPausedText) {
            // draw paused
            g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, (int) (worldHeight / 2.0)));

            g2.setColor(EDGES_COLOR);
            g2.drawString("Paused", (int) (worldWidth / 33.0), (int) (worldHeight - sliceHeight * 2));

            // draw controls
            g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, (int) (worldHeight / 25.0)));

            // snake controls
            g2.setColor(statusColor);
            g2.drawString("---------- Snake Controls ----------", (int) (worldWidth / 33.0), (int) (sliceHeight * 3));

            g2.drawString("Up Down Left Right Rewind AI/Respawn", (int) (worldWidth / 33.0), (int) (sliceHeight * 6));

            g2.setColor(snakeColorList.get(0));
            g2.drawString("W  A    S    D     Q      E", (int) (worldWidth / 33.0), (int) (sliceHeight * 9));

            g2.setColor(snakeColorList.get(1));
            g2.drawString("Y  H    G    J     T      U", (int) (worldWidth / 33.0), (int) (sliceHeight * 12));

            g2.setColor(snakeColorList.get(2));
            g2.drawString("P  ;    L    \"     O      [", (int) (worldWidth / 33.0), (int) (sliceHeight * 15));

            g2.setColor(snakeColorList.get(3));
            g2.drawString("Up Down Left Right Shift  Enter", (int) (worldWidth / 33.0), (int) (sliceHeight * 18));

            // other controls
            g2.setColor(statusColor);
            g2.drawString("--------- Other Controls ---------", (int) (worldWidth / 2.0), (int) (sliceHeight * 3));
            g2.drawString("Toggle Pause             Escape", (int) (worldWidth / 2.0), (int) (sliceHeight * 6));
            g2.drawString("Toggle Pause Text        Space", (int) (worldWidth / 2.0), (int) (sliceHeight * 9));
            g2.drawString("Restart                  Backspace", (int) (worldWidth / 2.0), (int) (sliceHeight * 12));
            g2.drawString("Decrease Delay           1", (int) (worldWidth / 2.0), (int) (sliceHeight * 15));
            g2.drawString("Increase Delay           2", (int) (worldWidth / 2.0), (int) (sliceHeight * 18));
            g2.drawString("Decrease Food            3", (int) (worldWidth / 2.0), (int) (sliceHeight * 21));
            g2.drawString("Increase Food            4", (int) (worldWidth / 2.0), (int) (sliceHeight * 24));
            g2.drawString("Toggle Respawn On Death  0", (int) (worldWidth / 2.0), (int) (sliceHeight * 27));
        }
    }
}
