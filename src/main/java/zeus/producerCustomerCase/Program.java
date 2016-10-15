package zeus.producerCustomerCase;

import zeus.producerCustomerCase.impl.DataCustomer;
import zeus.producerCustomerCase.impl.DataProducer;
import zeus.producerCustomerCase.impl.Dispatcher;

import java.util.Random;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public class Program {
    public  static  final Random RANDOM = new Random();
    public static void main(String[] args) {
        DataProducer dataProducer = new DataProducer();
        Dispatcher dispatcher = new Dispatcher(dataProducer,2);
        dispatcher.dispatch(new DataCustomer(5));
        dispatcher.start();
        try {
            Thread.sleep(1000);
        }catch (Exception ex){

        }

        dispatcher.stop();
    }
}
