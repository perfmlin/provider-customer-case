package zeus.producerCustomerCase.impl;

import org.junit.Assert;
import org.junit.Test;
import zeus.producerCustomerCase.exception.ReduceException;

/**
 * Created by AdminPerfmlin on 2016/10/17.
 */
public class StringCustomerTest {
    @Test
    public void testReduce() throws ReduceException {
        StringCustomer customer =new StringCustomer(5);
        String result = customer.reduce("test");
        Assert.assertNotEquals(result,null);
    }
}
