package classes.model;

public class ServiceParams {

    private String description;
    private String name;
    private String type;
    private ServiceStatus status;
    private int version;

    private ServiceParams() {

    }

    public static ServiceParams create() {
        return new ServiceParams();
    }

    public ServiceParams withDescription(String description) {
        this.description = description;
        return this;
    }

    public ServiceParams withName(String name) {
        this.name = name;
        return this;
    }

    public ServiceParams withType(String type) {
        this.type = type;
        return this;
    }

    public ServiceParams withStatus(ServiceStatus status) {
        this.status = status;
        return this;
    }

    public ServiceParams withVersion(int version) {
        this.version = version;
        return this;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getVersion() {
        return version;
    }
}
