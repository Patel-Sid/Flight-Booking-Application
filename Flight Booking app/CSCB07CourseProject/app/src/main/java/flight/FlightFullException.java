package flight;

public class FlightFullException extends Exception {

    private static final long serialVersionUID = -6583983596208150143L;

    /**
     * Creates a new <code>FlightFullException</code> with the given message.
     *
     * @param message the message for this <code>InvalidFlightAddition</code>
     */
    public FlightFullException(String message) {
        super(message);
    }
}
