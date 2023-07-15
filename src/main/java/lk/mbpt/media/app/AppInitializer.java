package lk.mbpt.media.app;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class AppInitializer extends Application {

    private static Stage stgVideoPlayer;
    private Slider sldVolume;
    private Slider slider;
    private MediaView mediaView;
    private Label lblPlay;
    private ImageView imgViewPause;
    private Label lblTime;
    private MediaPlayer videoPlayer;
    private final SimpleBooleanProperty isMute = new SimpleBooleanProperty(false);
    private File videoFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stgVideoPlayer  = primaryStage;
        primaryStage.setTitle("Video Player");
        videoPlayerScene();
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    private void videoPlayerScene() {
        Image imgPlay = new Image(getClass().getResource("/image/play-button.png").toString(), 48, 48, true, true);
        Image imgPause = new Image(getClass().getResource("/image/pause.png").toString(), 48, 48, true, true);
        Image imgReplay = new Image(getClass().getResource("/image/replay.png").toString(), 48, 48, true, true);
        Image imgStop = new Image(getClass().getResource("/image/stop-button.png").toString(), 48, 48, true, true);
        Image imgOpen = new Image(getClass().getResource("/image/open.png").toString(), 48, 48, true, true);
        Image imgSpeaker = new Image(getClass().getResource("/image/speaker-filled-audio-tool.png").toString(), 48, 48, true, true);
        Image imgMute = new Image(getClass().getResource("/image/mute-speaker.png").toString(), 48, 48, true, true);

        ImageView imgViewPlay = new ImageView(imgPlay);

        imgViewPause = new ImageView(imgPause);
        ImageView imgViewReplay = new ImageView(imgReplay);
        ImageView imgViewStop = new ImageView(imgStop);
        ImageView imgViewOpen = new ImageView(imgOpen);
        ImageView imgViewSpeaker = new ImageView(imgSpeaker);
        ImageView imgViewMute = new ImageView(imgMute);


        lblPlay = new Label("", imgViewPlay);
        Label lblStop = new Label("", imgViewStop);
        Label lblReplay = new Label("", imgViewReplay);
        Label lblOpen = new Label("", imgViewOpen);
        Label lblSpeaker = new Label("", imgViewSpeaker);

        lblPlay.setTooltip(new Tooltip("Play"));
        lblStop.setTooltip(new Tooltip("Stop"));
        lblReplay.setTooltip(new Tooltip("Replay"));
        lblOpen.setTooltip(new Tooltip("Open a video file to play"));
        lblSpeaker.setTooltip(new Tooltip("Mute"));

        sldVolume = new Slider(0, 1, 0.7);

        slider = new Slider();
        slider.setMaxWidth(Double.MAX_VALUE);
        slider.setDisable(true);

        lblTime = new Label("");
        lblTime.setTextFill(Color.LIGHTGRAY);
        lblTime.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        HBox hBoxSliderAndTime = new HBox(slider, lblTime);

        HBox hBox = new HBox(lblPlay, lblStop, lblReplay, lblOpen, lblSpeaker, sldVolume);

        VBox vBox = new VBox(hBoxSliderAndTime,hBox);

        Image imgBackground = new Image(getClass().getResource("/image/play.png").toString(), 100, 100, true, true);
        ImageView imgViewBackground = new ImageView(imgBackground);
        Label lblBackground = new Label("", imgViewBackground);

        lblBackground.setAlignment(Pos.CENTER);
        lblBackground.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);


        mediaView = new MediaView();
        mediaView.setPreserveRatio(true);

        StackPane stackPane = new StackPane(lblBackground, mediaView);
        stackPane.setBackground(Background.fill(Color.BLACK));

        AnchorPane root = new AnchorPane(stackPane, vBox);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);

        AnchorPane.setTopAnchor(stackPane, 0.0);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 89.0);

        hBox.setSpacing(15);
        hBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20, 10, 20, 10));

        vBox.setSpacing(10);
        HBox.setHgrow(slider,Priority.ALWAYS);

        vBox.setBackground(Background.fill(Color.rgb(52,81,125)));

        HBox.setHgrow(lblOpen, Priority.ALWAYS);
        lblOpen.setMaxWidth(Double.MAX_VALUE);
        lblOpen.setAlignment(Pos.CENTER);

        for (Node control : hBox.getChildren()) {
            if (control instanceof Label) {
                Label lbl = (Label) control;
                lbl.setCursor(Cursor.HAND);
                lbl.setOnMouseEntered(event -> {
                    lbl.getGraphic().setOpacity(0.8);
                    ScaleTransition st = new ScaleTransition(Duration.millis(100), lbl);
                    st.setFromX(1);
                    st.setFromY(1);
                    st.setToX(1.1);
                    st.setToY(1.1);
                    st.play();
                });
                lbl.setOnMouseExited(event -> {
                    lbl.getGraphic().setOpacity(1);
                    ScaleTransition st = new ScaleTransition(Duration.millis(100), lbl);
                    st.setFromX(1.1);
                    st.setFromY(1.1);
                    st.setToX(1);
                    st.setToY(1);
                    st.play();
                });
            }
        }

        Scene scene = new Scene(root);
        stgVideoPlayer.setScene(scene);
        stgVideoPlayer.setMinWidth(400);
        stgVideoPlayer.setWidth(600);
        stgVideoPlayer.setMinHeight(400);

        mediaView.fitWidthProperty().bind(stackPane.widthProperty());
        mediaView.fitHeightProperty().bind(stackPane.heightProperty());

        lblOpen.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.flv"));
            fileChooser.setTitle("Open a video file");

            videoFile = fileChooser.showOpenDialog(stgVideoPlayer);
            if (videoFile != null) {
                playVideo();
            }
        });

        lblStop.setOnMouseClicked(event -> {
            MediaPlayer videoPlayer = mediaView.getMediaPlayer();

            if (videoPlayer != null) {
                videoPlayer.stop();
                mediaView.setMediaPlayer(null);
                lblBackground.toFront();

                lblPlay.setGraphic(imgViewPlay);
                lblPlay.setTooltip(new Tooltip("Play"));
            }
        });

        lblPlay.setOnMouseClicked(event -> {
            MediaPlayer videoPlayer = mediaView.getMediaPlayer();
            if (videoPlayer == null) return;

            if (videoPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                videoPlayer.pause();
                lblPlay.setGraphic(imgViewPlay);
                lblPlay.setTooltip(new Tooltip("Play"));
            } else {
                videoPlayer.play();
                lblPlay.setGraphic(imgViewPause);
                lblPlay.setTooltip(new Tooltip("Pause"));
            }
        });

        lblSpeaker.setOnMouseClicked(event -> {
            isMute.set(!isMute.get());
            if (isMute.get()) {
                lblSpeaker.setGraphic(imgViewMute);
                lblSpeaker.setTooltip(new Tooltip("Unmute"));
            } else {
                lblSpeaker.setGraphic(imgViewSpeaker);
                lblSpeaker.setTooltip(new Tooltip("Mute"));
            }
        });

        stgVideoPlayer.setOnCloseRequest(event -> {
            MediaPlayer videoPlayer = mediaView.getMediaPlayer();
            if (videoPlayer != null) videoPlayer.stop();
            mediaView.setMediaPlayer(null);
            stgVideoPlayer = null;
        });

        lblReplay.setOnMouseClicked(event -> {
            if(videoFile != null){
                videoPlayer.stop();
                playVideo();
            }
        });

    }

    private void playVideo(){
        Media media = new Media(videoFile.toURI().toString());
        videoPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(videoPlayer);
        mediaView.toFront();
        videoPlayer.play();
        slider.setDisable(false);
        lblPlay.setGraphic(imgViewPause);
        lblPlay.setTooltip(new Tooltip("Pause"));

        videoPlayer.volumeProperty().bind(sldVolume.valueProperty());

        videoPlayer.muteProperty().bind(isMute);

        DoubleProperty sliderValue = new SimpleDoubleProperty();

        // Bind the slider value to the slider value property
        slider.valueProperty().bindBidirectional(sliderValue);

        // Add a listener to the media player's current time property to change Slider value and display video playing time
        videoPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!slider.isValueChanging()) {
                double duration = videoPlayer.getTotalDuration().toMillis();
                double currentTime = newValue.toMillis();
                lblTime.setText(String.format("%.2f / %.2f", currentTime / (1000 * 60), duration / (1000 * 60)));
                sliderValue.set((currentTime / duration) * 100.0);
                if(Math.round(sliderValue.getValue())==100) {
                    slider.setDisable(true);
                    lblTime.setText("");
                }
            }
        });

        // Add a listener to the slider value property to change the video playing time
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (slider.isValueChanging()) {
                double duration = videoPlayer.getTotalDuration().toMillis();
                double newTime = duration * newValue.doubleValue() / 100.0;
                videoPlayer.seek(new Duration(newTime));
            }
        });
    }

}
