package rxjava.droid.retrofit.beans;

/**
 * Created by rohitsingh on 28/04/16.
 */
public class UserWithContributor {
    public User user;
    public Contributor contributor;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public UserWithContributor(User user, Contributor contributor) {
        this.user = user;
        this.contributor = contributor;
    }
}
