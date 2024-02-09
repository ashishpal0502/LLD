package DesignLiftSystem;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Enum representing the direction of the lift
enum Direction {
    UP, DOWN
}

// Class representing a request for the lift
class Request {
    private int floor;
    private Direction direction;

    public Request(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }
}

// Class representing a lift car
class LiftCar {
    private int currentFloor;
    private Direction direction;

    public LiftCar() {
        currentFloor = 0; // Assuming the lift starts from ground floor
        direction = Direction.UP; // Initial direction
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        if (direction == Direction.UP) {
            currentFloor++;
        } else {
            currentFloor--;
        }
    }

    public void stop() {
        // Stop the lift and open doors
        System.out.println("Lift stopped at floor " + currentFloor);
    }
}

// Class representing the lift controller
class LiftController {
    private LiftCar liftCar;
    private BlockingQueue<Request> requestQueue;
    private ExecutorService executorService;

    public LiftController(LiftCar liftCar, int capacity) {
        this.liftCar = liftCar;
        this.requestQueue = new ArrayBlockingQueue<>(capacity);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void addRequest(Request request) {
        try {
            requestQueue.put(request);
        } catch (InterruptedException e) {
            System.err.println("Error adding request: " + e.getMessage());
        }
    }

    public void processRequests() {
        executorService.execute(() -> {
            while (true) {
                try {
                    Request request = requestQueue.take();
                    moveToFloor(request.getFloor());
                    liftCar.stop();
                } catch (InterruptedException e) {
                    System.err.println("Error processing request: " + e.getMessage());
                }
            }
        });
    }

    private void moveToFloor(int floor) {
        while (liftCar.getCurrentFloor() != floor) {
            if (liftCar.getCurrentFloor() < floor) {
                liftCar.move();
            } else {
                liftCar.move();
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

// Main class for simulating user interactions
public class LiftSystem {
    public static void main(String[] args) {
        LiftCar liftCar = new LiftCar();
        LiftController liftController = new LiftController(liftCar, 10);

        // Simulate user requests
        liftController.addRequest(new Request(3, Direction.UP));
        liftController.addRequest(new Request(6, Direction.DOWN));

        // Process requests
        liftController.processRequests();

        // Shutdown controller (assuming lift usage has ended)
        // This should be done gracefully in a real application
        liftController.shutdown();
    }
}

