package zeus.producerCustomerCase.exception;

/**
 * 调度者产生的错误
 */
public class DispatchException extends Exception {
    public DispatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
