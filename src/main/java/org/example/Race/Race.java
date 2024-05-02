package org.example.Race;
import java.util.concurrent.Phaser;
import java.util.logging.*;

import static org.example.Race.Race.LOGGER;
import static org.example.Race.Race.PHASER;


public class Race {
    protected static final Logger LOGGER = Logger.getLogger(Race.class.getName());

    protected static final Phaser PHASER = new Phaser(6);
    protected static final int TRACK_LENGTH = 500000;

    public static void main(String[] args) throws InterruptedException {

        LOGGER.setLevel(Level.INFO);
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        LOGGER.addHandler(consoleHandler);

        for (int i = 1; i <= 5; i++) {
            new Thread(new Car(i, (int) (Math.random() * 100 + 50))).start();
            Thread.sleep(1000);
        }

        Thread.sleep(1000);
        LOGGER.info("На старт!");
        PHASER.arriveAndDeregister();
        Thread.sleep(1000);
        LOGGER.info("Внимание!");
        PHASER.arriveAndDeregister();
        Thread.sleep(1000);
        LOGGER.info("Марш!");
        PHASER.arriveAndDeregister();
    }
}

class Car implements Runnable {
    private final int carNumber;
    private final int carSpeed;

    public Car(int carNumber, int carSpeed) {
        this.carNumber = carNumber;
        this.carSpeed = carSpeed;
    }

    @Override
    public void run() {
        try {
            LOGGER.info(String.format("Автомобиль №%d подъехал к стартовой прямой.\n", carNumber));
            PHASER.arriveAndAwaitAdvance();
            Thread.sleep(Race.TRACK_LENGTH / carSpeed);
            LOGGER.info(String.format("Автомобиль №%d финишировал!", carNumber));
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "При запуске потока car произошла ошибка.", e);
        }
    }
}