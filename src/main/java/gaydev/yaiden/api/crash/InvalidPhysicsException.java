package gaydev.yaiden.api.crash;

public class InvalidPhysicsException extends RuntimeException {
    /** this one doesnt aka no message */
    public InvalidPhysicsException() {
        super();
    }
    /**this function includes a message */
    public InvalidPhysicsException(String M) {
        super(M);
    }
}
