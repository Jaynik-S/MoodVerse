package use_case.z_old_note;

/**
 * Exception thrown when there is an error with accessing data.
 */
public class DataAccessException extends Exception {
    public DataAccessException(String string) {
        super(string);
    }
}
