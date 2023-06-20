package controller;

import datastorage.CareGiverDAO;
import datastorage.DAOFactory;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.CareGiver;
import model.Patient;
import model.Treatment;
import utils.DateConverter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class NewTreatmentController {
    @FXML
    private Label lblSurname;
    @FXML
    private Label lblFirstname;
    @FXML
    private TextField txtBegin;
    @FXML
    private TextField txtEnd;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextArea taRemarks;
    @FXML
    private DatePicker datepicker;
    @FXML
    private ComboBox<String> comboBoxPfleger;

    private AllTreatmentController controller;
    private Patient patient;
    private CareGiver careGiver;
    private Stage stage;
    private ObservableList<String> myComboBoxCareGiverData =
            FXCollections.observableArrayList();
    private ArrayList<CareGiver> careGiversList;

    public void initialize(AllTreatmentController controller, Stage stage, Patient patient, CareGiver careGiver) {
        this.controller= controller;
        this.patient = patient;
        this.stage = stage;
        comboBoxPfleger.setItems(myComboBoxCareGiverData);
        comboBoxPfleger.getSelectionModel().select(careGiver.getSurname());
        showPatientData();
        createComboBoxCareGiverData();
    }

    private void showPatientData(){
        this.lblFirstname.setText(patient.getFirstName());
        this.lblSurname.setText(patient.getSurname());
    }

    private CareGiver searchInListCareGiver(String surname) {
        for (int i =0; i<this.careGiversList.size();i++){
            if(this.careGiversList.get(i).getSurname().equals(surname)){
                return this.careGiversList.get(i);
            }
        }
        return null;
    }
    private void createComboBoxCareGiverData(){
        CareGiverDAO dao = DAOFactory.getDAOFactory().createCareGiverDAO();
        try {
            careGiversList = (ArrayList<CareGiver>) dao.readAll();
            for (CareGiver careGiver: careGiversList) {
                this.myComboBoxCareGiverData.add(careGiver.getSurname());
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAdd(){
        LocalDate date = this.datepicker.getValue();
        String s_begin = txtBegin.getText();
        LocalTime begin = DateConverter.convertStringToLocalTime(txtBegin.getText());
        LocalTime end = DateConverter.convertStringToLocalTime(txtEnd.getText());
        String description = txtDescription.getText();
        String remarks = taRemarks.getText();
        String pfleger_name = this.comboBoxPfleger.getSelectionModel().getSelectedItem();
        CareGiver caregiver = searchInListCareGiver(pfleger_name);
        Treatment treatment = new Treatment(patient.getPid(), caregiver.getCgid(), date,
                begin, end, description, remarks, "n");
        createTreatment(treatment);
        controller.readAllAndShowInTableView();
        stage.close();
    }

    private void createTreatment(Treatment treatment) {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        try {
            dao.create(treatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel(){
        stage.close();
    }
}