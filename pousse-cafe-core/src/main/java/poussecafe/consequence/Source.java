package poussecafe.consequence;

public class Source {

    public static final String DEFAULT_COMMAND_SOURCE_NAME = "commands";

    public static final Source DEFAULT_COMMAND_SOURCE = Source.forName(DEFAULT_COMMAND_SOURCE_NAME);

    public static final String DEFAULT_DOMAIN_EVENT_SOURCE_NAME = "domainEvents";

    public static final Source DEFAULT_DOMAIN_EVENT_SOURCE = Source.forName(DEFAULT_DOMAIN_EVENT_SOURCE_NAME);

    public static Source forName(String name) {
        return new Source(name);
    }

    private String name;

    private Source(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Source other = (Source) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Source [name=" + name + "]";
    }

}
