package xuxiangjun.esptouch.fx;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.util.TextUtils;
import com.espressif.iot.esptouch.util.TouchNetUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class MainController extends Controller {
    @FXML
    TextField ssidText;
    @FXML
    TextField bssidText;
    @FXML
    PasswordField passwordText;
    @FXML
    Text ipText;
    @FXML
    Text messageText;
    @FXML
    Button confirmBtn;
    @FXML
    ListView<String> listView;

    private EsptouchTask mEspTouchTask;
    private ObservableList<String> mResults;

    public void init() {
        mResults = FXCollections.observableArrayList();
        listView.setItems(mResults);

        InetAddress localAddress = TouchNetUtil.getLocalInetAddress();
        ipText.setText(localAddress == null ? "" : localAddress.getHostAddress());

        ssidText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                bssidText.requestFocus();
            }
        });
        bssidText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordText.requestFocus();
            }
        });
        passwordText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                confirmBtn.fire();
            }
        });

        confirmBtn.setOnAction(event -> {
            messageText.setText("");
            String ssidStr = ssidText.getText();
            String bssidStr = bssidText.getText();
            String passwordStr = passwordText.getText();

            if (TextUtils.isEmpty(ssidStr)) {
                messageText.setText(getRes().getString("message_ssid_empty"));
                return;
            }
            if (TextUtils.isEmpty(bssidStr)) {
                messageText.setText(getRes().getString("message_bssid_empty"));
                return;
            }
            byte[] bssid;
            try {
                bssid = bssidString2Bytes(bssidStr);
            } catch (Exception e) {
                messageText.setText(getRes().getString("message_bssid_invalid"));
                return;
            }
            byte[] ssid = ssidStr.getBytes(StandardCharsets.UTF_8);
            byte[] password = passwordStr.getBytes(StandardCharsets.UTF_8);

            HBox hBox = new HBox(16);
            hBox.setPadding(new Insets(16, 16, 16, 16));
            hBox.setAlignment(Pos.CENTER);
            hBox.setPrefSize(240, 160);
            ProgressIndicator progressIndicator = new ProgressIndicator(-1);
            Text text = new Text(getRes().getString("configuring"));
            hBox.getChildren().addAll(progressIndicator, text);
            Alert alert = new Alert(Alert.AlertType.NONE);
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(hBox);
            alert.setDialogPane(dialogPane);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.setOnHiding(dialogEvent -> {
                System.out.println("Dialog hidden");
                if (mEspTouchTask != null) {
                    mEspTouchTask.interrupt();
                    mEspTouchTask = null;
                }
            });
            alert.show();
            mResults.clear();

            // Execute EspTouch task
            mEspTouchTask = new EsptouchTask(ssid, bssid, password);
            mEspTouchTask.setEsptouchListener(result -> {
                String resultStr = String.format("BSSID: %s, Address: %s", result.getBssid(),
                        result.getInetAddress().getHostAddress());
                System.out.println("EspTouch ResultListener: " + resultStr);
                runOnUiThread(() -> {
                    mResults.add(resultStr);
                });
            });
            new Thread(() -> {
                mEspTouchTask.executeForResults(-1);
                runOnUiThread(() -> {
                    mEspTouchTask = null;
                    alert.close();
                });
            }).start();
        });
    }

    @Override
    public void release() {
        if (mEspTouchTask != null) {
            mEspTouchTask.interrupt();
            mEspTouchTask = null;
        }
    }

    private byte[] bssidString2Bytes(String string) {
        byte[] result = new byte[6];
        if (string.contains(":")) {
            String[] splits = string.split(":");
            for (int i = 0; i < 6; ++i) {
                result[i] = (byte) Integer.parseInt(splits[i], 16);
            }
        } else {
            for (int i = 0; i < 6; ++i) {
                int offset = i * 2;
                result[i] = (byte) Integer.parseInt(string.substring(offset, offset + 2), 16);
            }
        }
        return result;
    }
}
