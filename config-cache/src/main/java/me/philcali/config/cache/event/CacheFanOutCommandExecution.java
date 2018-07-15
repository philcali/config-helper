package me.philcali.config.cache.event;

import java.util.ArrayList;
import java.util.List;

import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;
import me.philcali.config.cache.command.CacheCommandType;
import me.philcali.config.cache.command.ICacheCommandExecution;

public class CacheFanOutCommandExecution implements ICacheSubscriber, ICacheCommandExecution {
    private final List<ICacheCommandExecution> trackingParameters;

    public CacheFanOutCommandExecution(final List<ICacheCommandExecution> trackingParameters) {
        this.trackingParameters = trackingParameters;
    }

    public CacheFanOutCommandExecution() {
        this(new ArrayList<>());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public void accept(final CacheEventType type, final IParameters group, final IParameter parameter) {
        switch (type) {
        case CREATED:
            if (group instanceof ICacheCommandExecution && !trackingParameters.contains(group)) {
                trackingParameters.add((ICacheCommandExecution) group);
            }
            break;
        default:
            // do nothing for commands
        }
    }

    @Override
    public void execute(final CacheCommandType command) {
        trackingParameters.forEach(execution -> execution.execute(command));
    }

}
