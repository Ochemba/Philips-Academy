package com.se.philips.AdminDashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

public class DashboardController {

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label MonthLabel;
    @FXML private GridPane calandarGrid;
    @FXML private HBox StudentBtn;
    @FXML private HBox TeacherBtn;
    @FXML private HBox CourseBtn;
    @FXML private HBox FinanceBtn;


    @FXML
    void Students(MouseEvent event) throws IOException {
        Stage stage = (Stage) StudentBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/StudentCreate.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Students");
        stage.setScene(scene);
    }

    @FXML
    void Teacher(MouseEvent event) throws IOException {
        Stage stage = (Stage) TeacherBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/TeacherCreate.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Teacher");
        stage.setScene(scene);
    }
    @FXML
    void Course(MouseEvent event) throws IOException {
        Stage stage = (Stage) CourseBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/CourseDashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Course");
        stage.setScene(scene);
    }
    @FXML
    void Finance(MouseEvent event) throws IOException {
        Stage stage = (Stage) FinanceBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/FinanceDashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Finance");
        stage.setScene(scene);
    }

    private YearMonth currentYearMonth;

    private final String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.now();

        prevButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        nextButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        updateCalendar();
    }

    private void updateCalendar() {
        calandarGrid.getChildren().clear(); // clear previous items
        MonthLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());

        // Add weekday headers
        for (int i = 0; i < 7; i++) {
            Text dayName = new Text(dayNames[i]);
            calandarGrid.add(dayName, i, 0); // row 0
        }

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 (Mon) - 7 (Sun)

        // JavaFX GridPane starts columns from 0 (Sunday = 0)
        int col = dayOfWeek % 7;  // shift Sunday from 7 to 0
        int row = 1;

        for (int day = 1; day <= daysInMonth; day++) {
            Text dayText = new Text(String.valueOf(day));
            calandarGrid.add(dayText, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }


}
