package zeus.producerCustomerCase.exception;

/**
 * 消费者产生的错误
 */
public class ReduceException extends Exception {
    public ReduceException(String message, Throwable cause) {
        super(message, cause);
    }
}
