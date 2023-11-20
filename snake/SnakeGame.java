import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final int GRID_SIZE = 20;
    private static final int TILE_SIZE = 20;

    private LinkedList<Point> snake;
    private Point food;
    private int direction; // 0: up, 1: right, 2: down, 3: left
    private boolean gameOver;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        generateFood();

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 2) direction = 0;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 3) direction = 1;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 0) direction = 2;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 1) direction = 3;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        });

        new Thread(() -> {
            while (!gameOver) {
                move();
                checkCollision();
                checkFood();
                repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case 0: // up
                newHead.y--;
                break;
            case 1: // right
                newHead.x++;
                break;
            case 2: // down
                newHead.y++;
                break;
            case 3: // left
                newHead.x--;
                break;
        }

        snake.addFirst(newHead);

        if (!hasFood(newHead)) {
            snake.removeLast();
        }
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            gameOver = true;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
            }
        }
    }

    private void checkFood() {
        Point head = snake.getFirst();
        if (head.equals(food)) {
            generateFood();
        }
    }

    private void generateFood() {
        Random random = new Random();
        int x, y;

        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    private boolean hasFood(Point point) {
        return point.equals(food);
    }

    @Override
    public void paint(java.awt.Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        for (Point point : snake) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame();
        snakeGame.setVisible(true);
    }
}
