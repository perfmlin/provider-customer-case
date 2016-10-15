package zeus.producerCustomerCase;

import zeus.producerCustomerCase.impl.StringCustomer;
import zeus.producerCustomerCase.impl.StringProducer;
import zeus.producerCustomerCase.impl.SimpleDispatchService;

import java.util.Random;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public class Program {
    public static final Random RANDOM = new Random();

    public static void main(String[] args) {
        IDispatchService simpleDispatchService = new SimpleDispatchService(2);
        simpleDispatchService.registerProducer(new StringProducer());
        simpleDispatchService.registerCustomer(new StringCustomer(5));
        simpleDispatchService.start();
        try {
            //堵塞主线程，假定我们的调度器线上工作1秒，之后停止工作。
            Thread.sleep(1000);
        } catch (Exception ex) {

        }
        simpleDispatchService.stop();
    }
}
