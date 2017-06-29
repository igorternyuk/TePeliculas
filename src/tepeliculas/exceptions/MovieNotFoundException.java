package tepeliculas.exceptions;

/**
 *
 * @author igor
 */
public class MovieNotFoundException extends Exception {

    public MovieNotFoundException() {
    }

    /**
     * Constructs an instance of <code>MovieNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MovieNotFoundException(String msg) {
        super(msg);
    }
}
