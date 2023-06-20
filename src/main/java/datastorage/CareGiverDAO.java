package datastorage;

import model.CareGiver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CareGiverDAO extends DAOimp<CareGiver> {

    public CareGiverDAO(Connection conn) {
        super(conn);
    }

    /**
     * generates a <code>INSERT INTO</code>-Statement for a given patient
     * @param careGiver for which a specific INSERT INTO is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getCreateStatementString(CareGiver careGiver) {
        return String.format("INSERT INTO caregiver (firstname, surname, phonenumber, username, password, locked) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                careGiver.getFirstName(), careGiver.getSurname(), careGiver.getPhonenumber(), careGiver.getUsername(), careGiver.getPassword(), careGiver.getLocked());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM caregiver WHERE cgid = %d", key);
    }

    @Override
    protected CareGiver getInstanceFromResultSet(ResultSet result) throws SQLException {
        CareGiver cg = null;
        cg = new CareGiver(result.getInt(1), result.getString(2),
                result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7));
        return cg;
    }

    public int nurseCheckLogin(String username, String password) throws SQLException {
        String query = "SELECT COUNT(*) FROM CAREGIVER WHERE USERNAME = '%s' AND PASSWORD = '%s' AND locked = '%s'";
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format(query, username, password, "n"));
        result.next();
        return result.getInt(1);
    }

    public boolean CheckIfNurseUsernameExist(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM CAREGIVER WHERE USERNAME = '%s'";
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format(query, username));
        result.next();
        if (result.getInt(1) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int nurseCheckBehandlungenlast10Years(CareGiver careGiver) throws SQLException {
        String query = "SELECT COUNT(*) FROM CAREGIVER INNER JOIN TREATMENT ON TREATMENT.CGID = CAREGIVER.CGID WHERE CAST(DATEDIFF(CURDATE(), TREATMENT.TREATMENT_DATE)/365.25 as int ) <= 10 AND CAREGIVER.CGID = %d ";
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format(query, careGiver.getCgid()));
        result.next();
        return result.getInt(1);
    }

    /**
     * generates a <code>SELECT</code>-Statement for all care giver.
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getReadAllStatementString() {
        return  String.format("SELECT * FROM caregiver where locked = '%s'", "n");
    }

    /**
     * maps a <code>ResultSet</code> to a <code>CareGiver-List</code>
     * @param result ResultSet with a multiple rows. Data will be mapped to caregiver-object.
     * @return ArrayList with caregivers from the resultSet.
     */
    @Override
    protected ArrayList<CareGiver> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<CareGiver> list = new ArrayList<CareGiver>();
        CareGiver cg = null;
        while (result.next()) {
            cg = new CareGiver(result.getInt(1), result.getString(2),
                    result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7));
            list.add(cg);
        }
        return list;
    }

    /**
     * generates a <code>UPDATE</code>-Statement for a given care giver
     * @param careGiver for which a specific update is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getUpdateStatementString(CareGiver careGiver) {
        return String.format("UPDATE caregiver SET firstname = '%s', surname = '%s', phonenumber = '%s' , username = '%s' WHERE cgid = %d",
                careGiver.getFirstName(), careGiver.getSurname(), careGiver.getPhonenumber(), careGiver.getUsername(),
                careGiver.getCgid());
    }


    /**
     * generates a <code>delete</code>-Statement for a given key
     * @param key for which a specific DELETE is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM caregiver WHERE cgid=%d", key);
    }

    /**
     * generates a <code>delete</code>-Statement for a given key
     * @param careGiver for which a specific DELETE is to be created
     * @return <code>String</code> with the generated SQL.
     */
    public void updateNurseLocked(CareGiver careGiver, String new_locked) throws SQLException {
        String query = String.format("UPDATE caregiver SET LOCKED = '%s' WHERE cgid = %d",
                new_locked,
                careGiver.getCgid());
        Statement st = conn.createStatement();
        st.executeUpdate(query);
    }
}
