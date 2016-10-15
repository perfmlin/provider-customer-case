package zeus.producerCustomerCase.impl;

import zeus.producerCustomerCase.IDispatcher;
import zeus.producerCustomerCase.IProducer;
import zeus.producerCustomerCase.Program;

import java.util.logging.Logger;


public class DataProducer implements IProducer {
    private static Logger logger = Logger.getLogger(DataProducer.class.getName());
    private IDispatcher m_dispatcher;
    @Override
    public void bind(IDispatcher dispatcher) {
        m_dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while(m_dispatcher.isOnline()) {
            Object data=null;
            try {
                //todo: fetch data from any source
                data = String.format("产品-%d", Program.RANDOM.nextInt(100));
                logger.info(String.format("准备生产:%s", data));
                try{
                    Thread.sleep(50);
                }catch (Exception ex){

                }
            }catch (Exception ex){
                //todo: 拉去异常，记录日志
            }
            if(null!=data) {
                m_dispatcher.push(data);
            }
        }
    }

}
