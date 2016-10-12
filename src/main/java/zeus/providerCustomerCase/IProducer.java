package zeus.providerCustomerCase;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public interface IProducer extends Runnable {
    void bind(IDispatcher dispatcher);
}
