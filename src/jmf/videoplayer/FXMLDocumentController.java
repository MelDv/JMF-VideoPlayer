/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmf.videoplayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    private MediaPlayer mediaPlayer;
    private String filePath;
    private final String LABEL_STRING = "JMF Media Player";

    @FXML
    private MediaView mediaView;

    @FXML
    private Label mainLabel;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider videoSlider;

    @FXML
    private void openVideoFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select an mp4-file.", "*.mp4");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            filePath = file.toURI().toString();
            initializeFile();
        }
    }

    @FXML
    private void openExampleFile(ActionEvent event) {
        String path = "src/media/hamsuJAmansikka.mp4";
        filePath = new File(path).toURI().toString();
        initializeFile();
    }

    private void initializeFile() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }

        if (!filePath.isEmpty()) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            // Set video to fit window
            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            volumeSlider.setMin(0.0);
            volumeSlider.setMax(1.0);
            volumeSlider.setValue(mediaPlayer.getVolume());
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue());
                }
            });

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    videoSlider.setValue(newValue.toSeconds());
                }
            });

            videoSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(videoSlider.getValue()));
                }
            });

            changeLabel();
            mediaPlayer.play();
        }
    }

    // Adds file name to label
    private void changeLabel() {
        String[] videoTitle = filePath.split("/");
        String temp[] = videoTitle[videoTitle.length - 1].split("\\.");
        String newLabel = temp[0];
        JMFVideoPlayer.getStage().setTitle(LABEL_STRING + " - " + newLabel);
    }

    @FXML
    private void pause(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void play(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(1);
            mediaPlayer.play();
        }
    }

    @FXML
    private void stop(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @FXML
    private void fastForward(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(1.5);
        }
    }

    @FXML
    private void extraFF(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(2);
        }
    }

    @FXML
    private void slow(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(.75);
        }
    }

    @FXML
    private void extraSlow(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(.5);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
