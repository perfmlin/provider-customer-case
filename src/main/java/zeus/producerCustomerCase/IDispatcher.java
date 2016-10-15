package zeus.producerCustomerCase;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public interface IDispatcher {
    void dispatch(ICustomer customer);
    void push(Object data);
    Object pop();
    boolean isOnline();


}
