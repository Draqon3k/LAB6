package com.example.laboratorul6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends Application {

    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final double INITIAL_SPEED = 0.1;

    private int snakeX = 0;
    private int snakeY = 0;
    private int directionX = 1;
    private int directionY = 0;
    private double speed = INITIAL_SPEED;
    private final List<BodySegment> bodySegments = new ArrayList<>();
    private boolean running = false;

    private static class BodySegment {
        int x;
        int y;

        BodySegment(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("snake_game.fxml"));
        Pane root = loader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Canvas gameCanvas = (Canvas) root.lookup("#gameCanvas");
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        Button startButton = (Button) root.lookup("#startButton");
        Button stopButton = (Button) root.lookup("#stopButton");
        Button increaseSpeedButton = (Button) root.lookup("#increaseSpeedButton");
        Button decreaseSpeedButton = (Button) root.lookup("#decreaseSpeedButton");

        startButton.setOnAction(event -> startGame());
        stopButton.setOnAction(event -> stopGame());
        increaseSpeedButton.setOnAction(event -> increaseSpeed());
        decreaseSpeedButton.setOnAction(event -> decreaseSpeed());

        gameCanvas.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            if (key == KeyCode.UP && directionY != 1) {
                directionX = 0;
                directionY = -1;
            } else if (key == KeyCode.DOWN && directionY != -1) {
                directionX = 0;
                directionY = 1;
            } else if (key == KeyCode.LEFT && directionX != 1) {
                directionX = -1;
                directionY = 0;
            } else if (key == KeyCode.RIGHT && directionX != -1) {
                directionX = 1;
                directionY = 0;
            }
            event.consume();
        });

        gameCanvas.setFocusTraversable(true);
        
        initializeGame();

        new javafx.animation.AnimationTimer() {
            long lastUpdate = 0;

            public void handle(long now) {
                if (running && now - lastUpdate >= speed * 1_000_000_000) {
                    lastUpdate = now;
                    update();
                    draw(gc);
                }
            }
        }.start();

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGame() {
        // Reset snake's initial position and body segments
        snakeX = WIDTH / 2;
        snakeY = HEIGHT / 2;
        bodySegments.clear();

        // Add initial body segments (change the number as desired)
        for (int i = 1; i <= 20; i++) {
            bodySegments.add(new BodySegment(snakeX - i * TILE_SIZE, snakeY));
        }
    }

    private void startGame() {
        running = true;
    }

    private void stopGame() {
        running = false;
    }

    private void increaseSpeed() {
            speed -= 0.1;

    }

    private void decreaseSpeed() {
            speed += 0.1;

    }

    

    private void update() {
        // Update snake's position
        snakeX += directionX * TILE_SIZE;
        snakeY += directionY * TILE_SIZE;

        // Check if snake hits the wall (boundary checking)
        if (snakeX < 0) snakeX = WIDTH - TILE_SIZE;
        if (snakeX >= WIDTH) snakeX = 0;
        if (snakeY < 0) snakeY = HEIGHT - TILE_SIZE;
        if (snakeY >= HEIGHT) snakeY = 0;

        // Move body segments
        for (int i = bodySegments.size() - 1; i > 0; i--) {
            bodySegments.get(i).x = bodySegments.get(i - 1).x;
            bodySegments.get(i).y = bodySegments.get(i - 1).y;
        }
        if (!bodySegments.isEmpty()) {
            bodySegments.getFirst().x = snakeX;
            bodySegments.getFirst().y = snakeY;
        }
    }

    private void draw(GraphicsContext gc) {
        // Clear the canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw the snake (head and body)
        gc.setFill(Color.GREEN);
        gc.fillText("S", snakeX, snakeY); // Draw snake's head

        for (BodySegment segment : bodySegments) {
            gc.fillText("o", segment.x, segment.y); // Draw snake's body segments
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
