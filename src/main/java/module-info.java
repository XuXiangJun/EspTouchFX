module EspTouch {
    requires javafx.controls;
    requires javafx.fxml;

    opens xuxiangjun.esptouch.fx to javafx.fxml;
    exports xuxiangjun.esptouch.fx;
}