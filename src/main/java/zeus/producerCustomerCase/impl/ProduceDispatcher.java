package zeus.producerCustomerCase.impl;

/**
 * Created by AdminPerfmlin on 2016/10/15.
 */

import zeus.producerCustomerCase.AbstractDispatcher;
import zeus.producerCustomerCase.IDispatchService;
import zeus.producerCustomerCase.IProducer;
import zeus.producerCustomerCase.IProducerDispatcher;
import zeus.producerCustomerCase.exception.ProduceException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public  class ProduceDispatcher extends AbstractDispatcher implements IProducerDispatcher {
    private final static Logger logger = Logger.getLogger(ProduceDispatcher.class.getName());
    public ProduceDispatcher(IDispatchService dispatcherService) {
        super(dispatcherService);
    }
    private int m_produced=0;
    private final List<IProducer> m_producerList = new ArrayList<>();
    @Override
    protected void doDispatch()  {
        for(int i=0;i<m_producerList.size();i++) {
            Object message = null;
            try {
               message = m_producerList.get(i).produce();
            }catch (ProduceException ex){

            }
            if (null != message) {
                //线程阻塞
                getDispatcherService().pushMessage(message);
                m_produced++;
            }
        }
    }



    @Override
    public void report() {

        logger.info(String.format("已生产：%d", m_produced));

    }

    @Override
    public boolean registerProducer(IProducer producer) {
        m_producerList.add(producer);
        return true;
    }

}
