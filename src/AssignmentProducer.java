import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Contains the data and behavior of an assignment producer
 */
public class AssignmentProducer implements Runnable {

    /**
     * Priority blocking queue for assignments
     */
    private final PriorityBlockingQueue<Assignment> assignmentPriorityQueue;

    /**
     * Maintains state about whether production of assignments is complete
     */
    private AtomicBoolean isProductionDone;

    /**
     * Final number of assignments produced
     */
    private AtomicInteger numberOfAssignments;

    /**
     * Creates a fully initialized assignment producer using the given data
     *
     * @param assignmentPriorityQueue Priority blocking queue for assignments
     * @param isProductionDone     Maintains state about whether production of assignments is complete
     * @param numberOfAssignments  Final number of assignments produced
     */
    public AssignmentProducer(PriorityBlockingQueue<Assignment> assignmentPriorityQueue,
                              AtomicBoolean isProductionDone,
                              AtomicInteger numberOfAssignments) {

        if (assignmentPriorityQueue == null) {
            throw new IllegalArgumentException("Unable to produce assignments in a null data structure");
        }

        if (isProductionDone == null) {
            throw new IllegalArgumentException("Unable to notify if assignment production is done");
        }

        if (numberOfAssignments == null) {
            throw new IllegalArgumentException("Unable to report number of assignments produced");
        }
        
        this.assignmentPriorityQueue = assignmentPriorityQueue;
        this.isProductionDone = isProductionDone;
        this.numberOfAssignments = numberOfAssignments;
    }

    /**
     * Override of the Runnable interface's run method that
     * inserts assignments into a priority blocking queue.
     */
    @Override
    public void run() {

        List<Assignment> unorderedAssignments = null;
        try {

            // Create a list of assignments (which are unordered relative to due date/priority level)
            unorderedAssignments = createAssignments();

            // Iterate over the unordered assignments and
            // insert them into the priority blocking queue
            for (Assignment unorderedAssignment : unorderedAssignments) {

                System.out.println("Producing: " + unorderedAssignment);
                assignmentPriorityQueue.put(unorderedAssignment);
                Thread.sleep(500); // simulating some work
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Notify that production is complete and the final count of assignments produced
        isProductionDone.set(true);
        numberOfAssignments.set(unorderedAssignments.size());
    }

    /**
     * Creates and returns a list of assignments
     */
    public List<Assignment> createAssignments() {

        List<Assignment> assignments = new ArrayList<>();

        // Insert assignments into the list (note that insertion order
        // is without consideration to due date/priority level rules)
        assignments.add(new Assignment("Buy milk and eggs",
                getDateXNumberOfDaysFromNow(2),
                Assignment.Priority.MEDIUM));

        assignments.add(new Assignment("Find new show on Netflix",
                getDateXNumberOfDaysFromNow(6),
                Assignment.Priority.LOW));

        assignments.add(new Assignment("Continue Udemy course",
                getDateXNumberOfDaysFromNow(5),
                Assignment.Priority.MEDIUM));

        assignments.add(new Assignment("Finish work assignment #1",
                getDateXNumberOfDaysFromNow(4),
                Assignment.Priority.HIGH));

        assignments.add(new Assignment("Finish work assignment #2",
                getDateXNumberOfDaysFromNow(2),
                Assignment.Priority.HIGH));

        assignments.add(new Assignment("Check out new restaurant",
                getDateXNumberOfDaysFromNow(13),
                Assignment.Priority.LOW));

        return assignments;
    }

    /**
     * Returns a date object that is set for X number of days from now
     *
     * @param daysFromNow X number of days from now
     */
    public static Date getDateXNumberOfDaysFromNow(long daysFromNow) {

        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Compute the future time in milliseconds as follows: daysFromNow * 24 * 3600 * 1000
        // This comes from:
        //     [daysFromNow] * 24 hours * 3600 seconds * 1000 milliseconds    -----> days from now in milliseconds
        //                     ________   ____________   _________________
        //                     1 day      1 hour         1 second
        long futureTimeMillis = currentTimeMillis + (daysFromNow * 24 * 3600 * 1000);

        // Create a Date object representing the future time
        return new Date(futureTimeMillis);
    }
}
