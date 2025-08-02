module com.se.philips {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires sqlite.jdbc4;
    requires java.desktop;
    requires mysql.connector.j;


    opens com.se.philips to javafx.fxml;
    exports com.se.philips;
    exports com.se.philips.Admin1;
    opens com.se.philips.Admin1 to javafx.fxml;
    exports com.se.philips.Admin;
    opens com.se.philips.Admin to javafx.fxml;
    exports com.se.philips.AdminDashboard;
    opens com.se.philips.AdminDashboard to javafx.fxml;
    exports com.se.philips.AdminDashboard.Payment.AdBox;
    opens com.se.philips.AdminDashboard.Payment.AdBox to javafx.fxml;
    exports com.se.philips.AdminDashboard.Payment;
    opens com.se.philips.AdminDashboard.Payment to javafx.fxml;
    opens com.se.philips.STUDENT to javafx.fxml;
    exports com.se.philips.STUDENT;
    opens com.se.philips.TEACHER to javafx.fxml;
    exports com.se.philips.TEACHER;


}