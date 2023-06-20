package controller;

import datastorage.CareGiverDAO;
import datastorage.PatientDAO;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CareGiver;
import model.Patient;
import model.Treatment;
import datastorage.DAOFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllTreatmentController {
    @FXML
    private TableView<Treatment> tableView;
    @FXML
    private TableColumn<Treatment, Integer> colID;
    @FXML
    private TableColumn<Treatment, Integer> colPid;
    @FXML
    private TableColumn<Treatment, Integer> colCGid;
    @FXML
    private TableColumn<Treatment, String> colDate;
    @FXML
    private TableColumn<Treatment, String> colBegin;
    @FXML
    private TableColumn<Treatment, String> colEnd;
    @FXML
    private TableColumn<Treatment, String> colDescription;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ComboBox<String> comboBoxPfleger;
    @FXML
    private Button btnNewTreatment;
    @FXML
    private Button btnDelete;

    private ObservableList<Treatment> tableviewContent =
            FXCollections.observableArrayList();
    private TreatmentDAO dao;
    private ObservableList<String> myComboBoxData =
            FXCollections.observableArrayList();
    private ObservableList<String> myComboBoxCareGiverData =
            FXCollections.observableArrayList();
    private ArrayList<CareGiver> careGiversList;
    private ArrayList<Patient> patientList;
    private Main main;

    public void initialize() {
        readAllAndShowInTableView();
        comboBox.setItems(myComboBoxData);
        comboBox.getSelectionModel().select(0);
        comboBoxPfleger.setItems(myComboBoxCareGiverData);
        comboBoxPfleger.getSelectionModel().select(0);
        this.main = main;

        this.colID.setCellValueFactory(new PropertyValueFactory<Treatment, Integer>("tid"));
        this.colPid.setCellValueFactory(new PropertyValueFactory<Treatment, Integer>("pid"));
        this.colCGid.setCellValueFactory(new PropertyValueFactory<Treatment, Integer>("cgid"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<Treatment, String>("date"));
        this.colBegin.setCellValueFactory(new PropertyValueFactory<Treatment, String>("begin"));
        this.colEnd.setCellValueFactory(new PropertyValueFactory<Treatment, String>("end"));
        this.colDescription.setCellValueFactory(new PropertyValueFactory<Treatment, String>("description"));
        this.tableView.setItems(this.tableviewContent);
        createComboBoxData();
        createComboBoxCareGiverData();
    }

    public void readAllAndShowInTableView() {
        this.tableviewContent.clear();
        comboBox.getSelectionModel().select(0);
        comboBoxPfleger.getSelectionModel().select(0);
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> allTreatments;
        try {
            allTreatments = dao.readAll();
            this.tableviewContent.addAll(allTreatments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createComboBoxData(){
        PatientDAO dao = DAOFactory.getDAOFactory().createPatientDAO();
        try {
            patientList = (ArrayList<Patient>) dao.readAll();
            this.myComboBoxData.add("alle");
            for (Patient patient: patientList) {
                this.myComboBoxData.add(patient.getSurname());
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void createComboBoxCareGiverData(){
        CareGiverDAO dao = DAOFactory.getDAOFactory().createCareGiverDAO();
        try {
            careGiversList = (ArrayList<CareGiver>) dao.readAll();
            this.myComboBoxCareGiverData.add("alle");
            for (CareGiver careGiver: careGiversList) {
                this.myComboBoxCareGiverData.add(careGiver.getSurname());
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleComboBox(){
        String p = this.comboBox.getSelectionModel().getSelectedItem();
        String pfleger_surname = this.comboBoxPfleger.getSelectionModel().getSelectedItem();
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> allTreatments;
        if(p.equals("alle")){
            try {
                allTreatments= this.dao.readAll();
                for (Treatment treatment : allTreatments) {
                    this.tableviewContent.add(treatment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Patient patient = searchInList(p);
        CareGiver pfleger = searchInListCareGiver(pfleger_surname);
        if(patient != null){
            try {
                if (pfleger != null) {
                    allTreatments = dao.readTreatmentsByPidUndNid(patient.getPid(), pfleger.getCgid());
                    for (Treatment treatment : allTreatments) {
                        this.tableviewContent.add(treatment);
                    }
                } else {
                    allTreatments = dao.readTreatmentsByPid(patient.getPid());
                    for (Treatment treatment : allTreatments) {
                        this.tableviewContent.add(treatment);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleCareGiverComboBox(){
        String p = this.comboBoxPfleger.getSelectionModel().getSelectedItem();
        String patient_surname = this.comboBox.getSelectionModel().getSelectedItem();
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> allTreatments;
        if(p.equals("alle")){
            try {
                allTreatments= this.dao.readAll();
                for (Treatment treatment : allTreatments) {
                    this.tableviewContent.add(treatment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        CareGiver careGiver = searchInListCareGiver(p);
        Patient patient = searchInList(patient_surname);
        if(careGiver != null){
            try {
                if (patient != null) {
                    allTreatments = dao.readTreatmentsByPidUndNid(patient.getPid(), careGiver.getCgid());
                    for (Treatment treatment : allTreatments) {
                        this.tableviewContent.add(treatment);
                    }
                } else {
                    allTreatments = dao.readTreatmentsByCareGiverid(careGiver.getCgid());
                    for (Treatment treatment : allTreatments) {
                        this.tableviewContent.add(treatment);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Patient searchInList(String surname){
        for (int i =0; i<this.patientList.size();i++){
            if(this.patientList.get(i).getSurname().equals(surname)){
                return this.patientList.get(i);
            }
        }
        return null;
    }

    private CareGiver searchInListCareGiver(String surname){
        for (int i =0; i<this.careGiversList.size();i++){
            if(this.careGiversList.get(i).getSurname().equals(surname)){
                return this.careGiversList.get(i);
            }
        }
        return null;
    }

    @FXML
    public void handleDelete() throws SQLException {
        int index = this.tableView.getSelectionModel().getSelectedIndex();
        Treatment treatment = this.tableviewContent.get(index);
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        boolean checkValidityBehandlung = dao.checkValidityTreatment(treatment);
        Treatment t = this.tableviewContent.remove(index);
        if (checkValidityBehandlung) {
            // Behandlung sperren
            try {
                dao.lockTreatment(treatment);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Verfahren ist erfolgreich abgeschlossen");
                alert.setContentText("Behandlung ist gesperrt!");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Behandlung loeschen
            try {
                dao.deleteById(t.getTid());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Verfahren ist erfolgreich abgeschlossen");
                alert.setContentText("Behandlung ist gelöscht!");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleNewTreatment() {
        try {
            String p = this.comboBox.getSelectionModel().getSelectedItem();
            String cg = this.comboBoxPfleger.getSelectionModel().getSelectedItem();
            Patient patient = searchInList(p);
            CareGiver caregiver = searchInListCareGiver(cg);
            newTreatmentWindow(patient, caregiver);
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient/Pfleger für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Patienten und einen Pfleger aus!");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleMouseClick(){
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedItem() != null)) {
                int index = this.tableView.getSelectionModel().getSelectedIndex();
                Treatment treatment = this.tableviewContent.get(index);

                treatmentWindow(treatment);
            }
        });
    }

    public void newTreatmentWindow(Patient patient, CareGiver caregiver){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/NewTreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            //da die primaryStage noch im Hintergrund bleiben soll
            Stage stage = new Stage();

            NewTreatmentController controller = loader.getController();
            controller.initialize(this, stage, patient, caregiver);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void treatmentWindow(Treatment treatment){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/TreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            //da die primaryStage noch im Hintergrund bleiben soll
            Stage stage = new Stage();
            TreatmentController controller = loader.getController();

            controller.initializeController(this, stage, treatment);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}