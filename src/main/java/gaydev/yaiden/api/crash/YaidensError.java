package gaydev.yaiden.api.crash;

public class YaidensError {

    public YaidensError() {
    }

    /**
     * ONLY USE IF THE ERROR IS SO BAD IT NEEDS A VM ERROR. when did i put a dot there? i guess i was serious
     */
    public VirtualMachineError YaidensVMError(String message) {
        return new YaidensVMErrorImpl(message);
    }

    public InvalidPhysicsException YaidensException(String message) {
        return new InvalidPhysicsException(message);
    }

    private static final class YaidensVMErrorImpl
            extends VirtualMachineError {

        private YaidensVMErrorImpl(String message) {
            super(message);
        }
    }
}
