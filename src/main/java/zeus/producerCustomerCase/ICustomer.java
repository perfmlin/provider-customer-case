package zeus.producerCustomerCase;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public interface ICustomer extends Runnable {
    void bind(IDispatcher dispatcher);

}
