package datastorage;

import model.Treatment;
import utils.DateConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO extends DAOimp<Treatment> {

    public TreatmentDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected String getCreateStatementString(Treatment treatment) {
        return String.format("INSERT INTO treatment (pid, cgid, treatment_date, begin, end, description, remarks) VALUES " +
                "(%d, %d, '%s', '%s', '%s', '%s', '%s')", treatment.getPid(), treatment.getCgid(), treatment.getDate(),
                treatment.getBegin(), treatment.getEnd(), treatment.getDescription(),
                treatment.getRemarks());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM treatment WHERE tid = %d AND locked = 'n'", key);
    }

    @Override
    protected Treatment getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
        LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
        LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
        Treatment m = new Treatment(result.getLong(1), result.getLong(2),
                date, begin, end, result.getString(6), result.getString(7), result.getString(9));
        return m;
    }

    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM treatment WHERE locked = 'n'";
    }


    public List<Treatment> readTreatmentsByCareGiverid(long cgid) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOneCareGiverByPid(cgid));
        list = getListFromResultSet(result);
        return list;
    }

    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment t = null;
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
            LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(5));
            LocalTime end = DateConverter.convertStringToLocalTime(result.getString(6));
            t = new Treatment(result.getLong(1), result.getLong(2), result.getLong(3),
                    date, begin, end, result.getString(7), result.getString(8), result.getString(9));
            list.add(t);
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(Treatment treatment) {
        return String.format("UPDATE treatment SET pid = %d, treatment_date ='%s', begin = '%s', end = '%s'," +
                "description = '%s', remarks = '%s' WHERE tid = %d", treatment.getPid(), treatment.getDate(),
                treatment.getBegin(), treatment.getEnd(), treatment.getDescription(), treatment.getRemarks(),
                treatment.getTid());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM treatment WHERE tid= %d", key);
    }

    public List<Treatment> readTreatmentsByPid(long pid) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOnePatientByPid(pid));
        list = getListFromResultSet(result);
        return list;
    }

    public List<Treatment> readTreatmentsByPidUndNid(long pid, long pfleger_id) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfByPatientPidAndNurseId(pid, pfleger_id));
        list = getListFromResultSet(result);
        return list;
    }

    private String getReadAllTreatmentsOfOnePatientByPid(long pid){
        return String.format("SELECT * FROM treatment WHERE pid = %d AND locked = 'n'", pid);
    }

    private String getReadAllTreatmentsOfByPatientPidAndNurseId(long pid, long nid){
        return String.format("SELECT * FROM treatment WHERE pid = %d AND cgid = %d AND locked = 'n'", pid, nid);
    }

    private String getReadAllTreatmentsOfOneCareGiverByPid(long pid){
        return String.format("SELECT * FROM treatment WHERE cgid = %d  AND locked = 'n'", pid);
    }

    public void deleteByPid(long key) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("Delete FROM treatment WHERE pid= %d", key));
    }

    // Delete treatments with nurse ID
    public void deleteTreatmentsByNurseId(long cgid) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("Delete FROM treatment WHERE cgid = %d", cgid));
    }

    // true (Behandlung ist in der letzeten 10 Jahren) -> sperren  || false (Behandlung ist mehr als 10 Jahren duchgefuehrt)-> loeschen
    public boolean checkValidityTreatment(Treatment tr) throws SQLException {
        String query = "SELECT COUNT(*) FROM treatment WHERE CAST(DATEDIFF(CURDATE(), TREATMENT.TREATMENT_DATE)/365.25 as int ) <= 10 AND tid= %d ";
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format(query, tr.getTid()));
        result.next();
        if (result.getInt(1) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void lockTreatment(Treatment tr) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("UPDATE treatment SET locked = 'y' WHERE tid = %d",
                tr.getTid()));
    }

    // Delete invalid treatments (Validity date 10 years)
    public void deleteInvalidTreatments() throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("Delete FROM treatment WHERE CAST(DATEDIFF(CURDATE(), TREATMENT.TREATMENT_DATE)/365.25 as int ) > 10 AND locked = locked = 'y' "));
    }
}