package model;
/**
 * Person class.
 */
public abstract class Person {
    private String firstName;
    private String surname;

    /**
     * constructs a person from the given params.
     * @param firstName
     * @param surname
     */
    public Person(String firstName, String surname) {
        this.firstName = firstName;
        this.surname = surname;
    }

    /**
     * Returns the person firstname
     * @return Person firstname
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the person firstname
     * @param firstName new firstname
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the person surname
     * @return Person surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set the person surname
     * @param surname new surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
}
