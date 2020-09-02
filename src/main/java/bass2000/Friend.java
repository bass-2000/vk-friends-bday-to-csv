package bass2000;


/**
 * POJO сущности Friend для дальнейшей обработки
 */
public class Friend {
    private Integer id;
    private String firstName;
    private String lastName;
    private String bday;
    private String url;

    public Integer getId() {
        return id;
    }

    public Friend setId(Integer id) {
        this.id = id;
        return this;
    }

    String getFirstName() {
        return firstName;
    }

    Friend setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    String getLastName() {
        return lastName;
    }

    Friend setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    String getBday() {
        return bday;
    }

    Friend setBday(String bday) {
        this.bday = bday;
        return this;
    }

    String getUrl() {
        return url;
    }

    Friend setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", bday='" + bday + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
