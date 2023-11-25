import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class for the virtual thread example console app.
 */
public class Main {

    /**
     * Entry method for the virtual thread example console app.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {

        // Creating a priority blocking queue of assignments
        PriorityBlockingQueue<Assignment> assignmentPriorityQueue = new PriorityBlockingQueue<>(3);

        // Create an atomic boolean that the producer thread will be able to use to
        // signal the consumer thread that production is done
        AtomicBoolean isProductionDone = new AtomicBoolean(false);

        // Create an atomic integer that the producer thread will be able ot use to
        // signal the consumer thread how many assignments have been produced.  This
        // will be initialized to the max value and then will be updated to a real
        // value once production is complete so the consumer thread will know how
        // many assignments are left to consume
        AtomicInteger numberOfAssignments = new AtomicInteger(Integer.MAX_VALUE);

        // Create a runnable producer task
        Runnable producerTask = new AssignmentProducer(
                assignmentPriorityQueue, isProductionDone, numberOfAssignments);

        // Create a runnable consumer task
        Runnable consumerTask = new AssignmentConsumer(
                assignmentPriorityQueue, isProductionDone, numberOfAssignments);

        // Create a virtual thread builder.  The first virtual thread this builder makes
        // will have the name "myVirtualThread-0" and then automatically the ID will increase
        // as more threads are made.
        Thread.Builder virtualThreadBuilder = Thread.ofVirtual().name("myVirtualThread", 0);

        // Create and start a virtual producer thread (name: "myVirtualThread-0")
        Thread producerThread = virtualThreadBuilder.start(producerTask);

        // Create and start a virtual consumer thread (name: "myVirtualThread-1")
        Thread consumerThread = virtualThreadBuilder.start(consumerTask);

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("All assignments produced and consumed");
    }
}