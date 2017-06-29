package tepeliculas.exceptions;

/**
 *
 * @author igor
 */
public class ClassificationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ClassificationNotFoundException</code>
     * without detail message.
     */
    public ClassificationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ClassificationNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ClassificationNotFoundException(String msg) {
        super(msg);
    }
}
