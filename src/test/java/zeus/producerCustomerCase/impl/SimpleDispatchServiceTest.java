package zeus.producerCustomerCase.impl;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by AdminPerfmlin on 2016/10/17.
 */
public class SimpleDispatchServiceTest {
    @Test
    public void testStart() throws InterruptedException {
        SimpleDispatchService service = new SimpleDispatchService(5);
        Assert.assertTrue(service.registerProducer(new StringProducer()));
        Assert.assertTrue(service.registerCustomer(new StringCustomer(5)));
        service.start();
        Assert.assertTrue(service.isRunning());
        Assert.assertFalse(service.isStopped());
        Thread.sleep(1000);
        service.stop();
        Assert.assertTrue(service.isStopped());
    }
}
