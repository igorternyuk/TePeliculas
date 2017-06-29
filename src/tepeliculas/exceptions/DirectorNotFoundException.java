package tepeliculas.exceptions;

/**
 *
 * @author igor
 */
public class DirectorNotFoundException extends Exception {

    public DirectorNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DirectorNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DirectorNotFoundException(String msg) {
        super(msg);
    }
}
