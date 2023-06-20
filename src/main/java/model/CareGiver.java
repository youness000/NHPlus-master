package model;
/**
 * Nurses treat patients.
 */
public class CareGiver extends Person {
    private long cgid;
    private String phonenumber;
    private String username;
    private String password;
    private String locked;

    /**
     * constructs a Nurse from the given params.
     * @param firstName
     * @param surname
     * @param phonenummber
     * @param username
     * @param password
     * @param locked
     */
    public CareGiver(String firstName, String surname, String phonenummber, String username, String password, String locked) {
        super(firstName, surname);
        this.phonenumber = phonenummber;
        this.username = username;
        this.password = password;
        this.locked = locked;
    }

    /**
     * constructs a Nurse from the given params.
     * @param firstName
     * @param surname
     * @param phonenummber
     * @param username
     * @param password
     */
    public CareGiver(long cgid, String firstName, String surname, String phonenummber, String username, String password, String locked) {
        super(firstName, surname);
        this.cgid = cgid;
        this.phonenumber = phonenummber;
        this.username = username;
        this.password = password;
        this.locked = locked;
    }

    /**
     * Returns Nurse ID
     * @return Nurse id
     */
    public long getCgid() {
        return cgid;
    }

    /**
     * Returns Nurse phonenumber
     * @return nurse phonenumber
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * to set nurse phonenumber
     * @param phonenumber new phone number
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    /**
     * Returns Nurse Username
     * @return nurse Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * to set username for nurse
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns nurse password
     * @return nurse password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns nurse status
     * @return nurse status (locked or no)
     */
    public String getLocked() {
        return locked;
    }

    /**
     *
     * @param locked new value of locked (accept 'y' | 'n')
     */
    public void setLocked(String locked) {
        if (locked == "y" || locked == "n")
            this.locked = locked;
    }

    /**
     * return representation of the nurse
     * @return string-representation of the nurse
     */
    public String toString() {
        return "Pfleger" + "\nMNID: " + this.getCgid() +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nTelefonnumer: " + this.getPhonenumber() +
                "\nUsername: " + this.getUsername() +
                "\n";
    }
}
