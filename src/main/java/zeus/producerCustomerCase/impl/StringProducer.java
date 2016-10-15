package zeus.producerCustomerCase.impl;

import zeus.producerCustomerCase.IProducer;
import zeus.producerCustomerCase.Program;
import zeus.producerCustomerCase.exception.ProduceException;

import java.util.logging.Logger;


public class StringProducer implements IProducer<String> {
    private static Logger logger = Logger.getLogger(StringProducer.class.getName());

    /**
     * 数据生成工作实现
     * @return
     */
    @Override
    public String produce() throws ProduceException {
        String data=null;
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
            throw new ProduceException("生产异常",ex);
        }
        return data;
    }
}
