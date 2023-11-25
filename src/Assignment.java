import java.util.Date;
import java.util.Objects;

/**
 * Defines the characteristics of an assignment.
 */
public record Assignment(String description, Date dueDate, Priority priority)  implements Comparable<Assignment> {

    /**
     * Defines priority enum values
     */
    public enum Priority {
        HIGH(1),
        MEDIUM(2),
        LOW(3);

        /**
         * Numerical value associated with a priority enum.
         * Note: a lower numerical value corresponds to a higher priority enum
         */
        private final int value;

        /**
         * Facilitates the association between a numerical value and a priority enum
         *
         * @param value A numerical value
         */
        Priority(int value) {
            this.value = value;
        }

        /**
         * Returns the numerical value associated with a priority enum
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * Override of Comparable.compareTo to specify that elements should be compared based on:
     * 1. due date
     * 2. priority
     * @param o the object to be compared.
     */
    @Override
    public int compareTo(Assignment o) {

        // Compare by due date first
        int dateComparison = this.dueDate.compareTo(o.dueDate);

        // If due dates are different return the result
        if (dateComparison != 0) {
            return dateComparison;
        }

        // Since due dates are equal break the tie with priority
        return this.priority.compareTo(o.priority);
    }
}
