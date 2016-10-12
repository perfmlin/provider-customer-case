package zeus.providerCustomerCase.impl;

import zeus.providerCustomerCase.ICustomer;
import zeus.providerCustomerCase.IDispatcher;
import zeus.providerCustomerCase.IProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public class Dispatcher implements IDispatcher {
    private static Logger logger = Logger.getLogger(Dispatcher.class.getName());

    //共享数据
    private final BlockingQueue m_sharedQueue;
    private final Thread m_threadProducer;
    private final ExecutorService m_customerService = Executors.newCachedThreadPool();
    private final List<Future> m_customerServiceFutures = new ArrayList();
    private int m_pushed,m_popped;
    public Dispatcher(IProducer producer, int queueSize) {
        producer.bind(this);
        if (queueSize < 1 || queueSize > 100) queueSize = 10;
        m_sharedQueue = new ArrayBlockingQueue(queueSize);
        m_threadProducer = new Thread(producer);

    }
    public void start(){
        m_online =true;
        m_threadProducer.start();
    }

    private boolean m_online;
    public void stop() {
        if (m_online) {
            m_online = false;
            try {
                m_threadProducer.join();
                for(int i = 0; i< m_customerServiceFutures.size(); i++){
                    m_customerServiceFutures.get(i).get();
                }
            }catch (Exception ex){

            }
            logger.info(String.format("累计：生产%d,消费%d次,",m_pushed,m_popped));
        }
    }

    @Override
    public boolean isOnline() {
        return m_online;
    }

    @Override
    public void push(Object data) {
        try {
            m_sharedQueue.put(data);
            logger.info(String.format("结束生产:%s", data));
            m_pushed++;
        }catch (InterruptedException ex){
            logger.log(Level.SEVERE,null,ex);
        }
    }

    @Override
    public Object pop() {
        try {
            Object data = m_sharedQueue.take();
            m_popped++;
            return data;
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void dispatch(ICustomer customer) {
        customer.bind(this);
        m_customerServiceFutures.add(m_customerService.submit(customer));
    }
}
