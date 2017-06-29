package tepeliculas.exceptions;

/**
 *
 * @author igor
 */
public class GenreNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>GenreNotFoundException</code> without
     * detail message.
     */
    public GenreNotFoundException() {
    }

    /**
     * Constructs an instance of <code>GenreNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GenreNotFoundException(String msg) {
        super(msg);
    }
}
