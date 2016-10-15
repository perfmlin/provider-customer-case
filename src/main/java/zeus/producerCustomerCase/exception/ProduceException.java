package zeus.producerCustomerCase.exception;

/**
 * 生产者产生的错误
 */
public class ProduceException extends Exception {
    public ProduceException(String message, Throwable cause) {
        super(message, cause);
    }
}
