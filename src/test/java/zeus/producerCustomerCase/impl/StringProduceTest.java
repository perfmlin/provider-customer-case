package zeus.producerCustomerCase.impl;

import org.junit.Assert;
import org.junit.Test;
import zeus.producerCustomerCase.exception.ProduceException;

/**
 * Created by AdminPerfmlin on 2016/10/17.
 */
public class StringProduceTest {

    @Test
    public void testProduce() throws ProduceException {
        StringProducer producer = new StringProducer();
        String data = producer.produce();
        Assert.assertNotEquals(data,null);
    }
}
