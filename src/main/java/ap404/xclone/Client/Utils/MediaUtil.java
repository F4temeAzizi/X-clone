package ap404.xclone.Client.Utils;

import ap404.xclone.Shared.Models.Media;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.File;
import java.util.List;

public class MediaUtil
{
    private static MediaPlayer currentPlayer;

    public static void showPreview(FlowPane pane, List<Media> media)
    {
        pane.getChildren().clear();
        int count = Math.min(media.size(), 4);

        if (count == 0) return;
        if (count == 1)
        {
            pane.getChildren().add(rounded(createItem(media.get(0), media, pane, 650, 420, true)));
        }
        else if (count == 2)
        {
            HBox r = new HBox(1);
            r.getChildren().add(createItem(media.get(0), media, pane, 324,  420, false));
            r.getChildren().add(createItem(media.get(1), media, pane, 324,  420, false));
            pane.getChildren().add(rounded(r));
        }
        else if (count == 3)
        {
            VBox root = new VBox(1);
            root.getChildren().add(createItem(media.get(0), media, pane, 649, 210, false));

            HBox r = new HBox(1);
            r.getChildren().add(createItem(media.get(1), media, pane, 324, 205, false));
            r.getChildren().add(createItem(media.get(2), media, pane, 324, 205, false));

            root.getChildren().add(r);
            pane.getChildren().add(rounded(root));
        }
        else
        {
            VBox root = new VBox(1);

            HBox r1 = new HBox(1);
            HBox r2 = new HBox(1);

            r1.getChildren().add(createItem(media.get(0), media, pane, 324, 205, false));
            r1.getChildren().add(createItem(media.get(1), media, pane, 324, 205, false));

            r2.getChildren().add(createItem(media.get(2), media, pane, 324, 205, false));
            r2.getChildren().add(createItem(media.get(3), media, pane, 324, 205, false));

            root.getChildren().addAll(r1, r2);

            pane.getChildren().add(rounded(root));
        }
    }

    private static StackPane createItem(Media media, List<Media> mediaList, FlowPane pane, double w, double h, boolean p)
    {
        StackPane content = new StackPane();

        if (media.getMediaType().equals("Image"))
        {
            ImageView imageView = new ImageView(new Image(new File(media.getMediaUrl()).toURI().toString()));
            imageView.setFitWidth(w);
            imageView.setFitHeight(h);
            imageView.setPreserveRatio(p);

            imageView.setOnMouseClicked(e -> openImage(media));

            content.getChildren().add(imageView);
        }
        else if (media.getMediaType().equals("Video"))
        {
            MediaPlayer previewPlayer = new MediaPlayer(new javafx.scene.media.Media
                    (new File(media.getMediaUrl()).toURI().toString()));

            MediaView mediaView = new MediaView(previewPlayer);

            mediaView.setFitWidth(w);
            mediaView.setFitHeight(h);
            mediaView.setPreserveRatio(p);

            previewPlayer.setMute(true);
            previewPlayer.setAutoPlay(true);

            StackPane videoPane = new StackPane(mediaView);
            Label play = new Label("▶");

            play.setStyle("""
            -fx-background-color: rgba(0,0,0,0.5);
            -fx-text-fill: white;
            -fx-font-size: 36;
            -fx-padding: 10;
            -fx-background-radius: 100;
            """);

            StackPane.setAlignment(play, Pos.CENTER);
            videoPane.getChildren().add(play);

            videoPane.setOnMouseClicked(e -> openVideo(media));
            content.getChildren().add(videoPane);
        }

        Button remove = new Button("✕");

        remove.setStyle("""
                    -fx-background-color:transparent;
                    -fx-text-fill:white;
                    -fx-background-radius:50;
                    """);

        remove.setOnAction(e -> {
            mediaList.remove(media);
            showPreview(pane, mediaList);
        });

        StackPane stackPane = new StackPane(content, remove);
        StackPane.setAlignment(remove, javafx.geometry.Pos.TOP_RIGHT);

        return stackPane;
    }

    public static void showTweet(FlowPane pane, List<Media> media)
    {
        pane.getChildren().clear();
        int count = Math.min(media.size(), 4);

        if (count == 0) return;
        if (count == 1)
        {
            pane.getChildren().add(rounded(createTweetItem(media.get(0), 650, 420, true)));
        }
        else if (count == 2)
        {
            HBox r = new HBox(1);
            r.getChildren().add(createTweetItem(media.get(0), 324, 420, false));
            r.getChildren().add(createTweetItem(media.get(1), 324, 420, false));
            pane.getChildren().add(rounded(r));
        }
        else if (count == 3)
        {
            VBox root = new VBox(1);
            root.getChildren().add(createTweetItem(media.get(0), 649, 210, false));

            HBox r = new HBox(1);
            r.getChildren().add(createTweetItem(media.get(1), 324, 205, false));
            r.getChildren().add(createTweetItem(media.get(2), 324, 205, false));

            root.getChildren().add(r);
            pane.getChildren().add(rounded(root));
        }
        else
        {
            VBox root = new VBox(1);

            HBox r1 = new HBox(1);
            HBox r2 = new HBox(1);

            r1.getChildren().add(createTweetItem(media.get(0), 324, 205, false));
            r1.getChildren().add(createTweetItem(media.get(1), 324, 205, false));

            r2.getChildren().add(createTweetItem(media.get(2), 324, 205, false));
            r2.getChildren().add(createTweetItem(media.get(3), 324, 205, false));

            root.getChildren().addAll(r1, r2);
            pane.getChildren().add(rounded(root));
        }
    }

    private static StackPane createTweetItem(Media media, double w, double h, boolean p)
    {
        StackPane content = new StackPane();

        if (media.getMediaType().equals("Image"))
        {
            ImageView imageView = new ImageView(new Image(new File(media.getMediaUrl()).toURI().toString()));

            imageView.setFitWidth(w);
            imageView.setFitHeight(h);
            imageView.setPreserveRatio(p);
            imageView.setOnMouseClicked(e -> openImage(media));

            content.getChildren().add(imageView);
        }
        else if (media.getMediaType().equals("Video"))
        {
            MediaPlayer previewPlayer = new MediaPlayer(new javafx.scene.media.Media
                    (new File(media.getMediaUrl()).toURI().toString()));

            MediaView mediaView = new MediaView(previewPlayer);

            mediaView.setFitWidth(w);
            mediaView.setFitHeight(h);
            mediaView.setPreserveRatio(p);

            previewPlayer.setMute(true);
            previewPlayer.setAutoPlay(true);

            StackPane videoPane = new StackPane(mediaView);
            Label play = new Label("▶");

            play.setStyle("""
            -fx-background-color: rgba(0,0,0,0.5);
            -fx-text-fill: white;
            -fx-font-size: 36;
            -fx-padding: 10;
            -fx-background-radius: 100;
            """);

            StackPane.setAlignment(play, Pos.CENTER);
            videoPane.getChildren().add(play);

            videoPane.setOnMouseClicked(e -> openVideo(media));
            content.getChildren().add(videoPane);
        }
        return content;
    }

    public static void addPhotos(FlowPane previewPane, List<Media> mediaList, Window owner)
    {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        List<File> files = chooser.showOpenMultipleDialog(owner);

        if (files == null) return;

        for (File file : files)
        {
            if (mediaList.size() == 4) break;
            mediaList.add(new Media(file.getAbsolutePath(), "Image", mediaList.size()));
            showPreview(previewPane, mediaList);
        }
        System.out.println(mediaList.size());
    }

    public static void openImage(Media media)
    {
        Stage stage = new Stage();
        ImageView imageView = new ImageView(new Image(new File(media.getMediaUrl()).toURI().toString()));

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(1000);
        imageView.setFitHeight(700);

        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void addVideos(FlowPane previewPane, List<Media> mediaList, Window owner)
    {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Videos", "*.mp4", "*.mov", "*.m4v", "*.avi"));

        List<File> files = chooser.showOpenMultipleDialog(owner);

        if (files == null) return;

        for (File file: files)
        {
            if (mediaList.size() == 4) break;
            mediaList.add(new Media(file.getAbsolutePath(),"Video", mediaList.size()));
            showPreview(previewPane, mediaList);
        }
        System.out.println(mediaList.size());
    }

    public static void openVideo(Media media)
    {
        if (currentPlayer != null)
        {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }

        Stage stage = new Stage();
        MediaPlayer player = new MediaPlayer(new javafx.scene.media.Media(new File(media.getMediaUrl()).toURI().toString()));
        currentPlayer = player;

        MediaView mediaView = new MediaView(player);

        mediaView.setPreserveRatio(true);
        mediaView.setFitWidth(1000);
        mediaView.setFitHeight(700);

        StackPane root = new StackPane(mediaView);
        Scene scene = new Scene(root);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);

        stage.setOnShown(e -> player.play());

        stage.setOnCloseRequest(e -> {
            player.stop();
            player.dispose();
            currentPlayer = null;
        });
        stage.showAndWait();
    }

    private static StackPane rounded(Pane pane)
    {
        StackPane stackPane = new StackPane(pane);

        Rectangle clip = new Rectangle();
        clip.setArcHeight(20);
        clip.setArcWidth(20);

        clip.widthProperty().bind(stackPane.widthProperty());
        clip.heightProperty().bind(stackPane.heightProperty());

        stackPane.setClip(clip);

        return stackPane;
    }
}

