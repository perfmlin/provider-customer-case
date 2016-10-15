package zeus.producerCustomerCase;

/**
 * Created by AdminPerfmlin on 2016/10/15.
 */
public abstract class AbstractDispatcher implements IDispatcher {
    private IDispatchService m_dispatcherService;
    protected IDispatchService getDispatcherService() {
        return m_dispatcherService;
    }

    public AbstractDispatcher(IDispatchService dispatcherService) {
        bind(dispatcherService);
    }

    @Override
    public final void bind(IDispatchService dispatcherService) {
        m_dispatcherService = dispatcherService;
    }

    private boolean m_running;
    @Override
    public final void run() {
        m_running =true;
        while (!m_dispatcherService.isStopped()){

            boolean b = m_dispatcherService.isStopped();
            doDispatch();
        }
        m_running =false;
    }

    protected abstract void doDispatch();

    @Override
    public boolean isRunning() {
        return m_running;
    }


}
