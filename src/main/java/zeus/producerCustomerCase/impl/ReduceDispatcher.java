package zeus.producerCustomerCase.impl;

import zeus.producerCustomerCase.AbstractDispatcher;
import zeus.producerCustomerCase.IDispatchService;
import zeus.producerCustomerCase.exception.ReduceException;

import java.util.logging.Logger;

/**
 * Created by AdminPerfmlin on 2016/10/15.
 */
public class ReduceDispatcher extends AbstractDispatcher {
    private final static Logger logger = Logger.getLogger(ReduceDispatcher.class.getName());
    public ReduceDispatcher(IDispatchService dispatcherService) {
        super(dispatcherService);
    }
    private int m_consumed;
    @Override
    protected void doDispatch() {
        //发现数据,线程阻塞
        Object data = getDispatcherService().popMessage();
        logger.info(String.format("准备消费数据：%s",data));
        //找消费者
        try {
            getDispatcherService().notifyCustomer(data);
            m_consumed++;
        } catch (ReduceException ex) {

        }

    }

    @Override
    public void report() {
        logger.info(String.format("已消费：%d",m_consumed));
    }
}