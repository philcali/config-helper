package me.philcali.config.api.exception;

public class ConfigProvisionException extends RuntimeException {
    private static final long serialVersionUID = 1537716957147065913L;
    private final String groupName;

    public ConfigProvisionException(final String groupName, final Throwable ex) {
        super(ex);
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
