package ap404.xclone.Client.Utils;

import ap404.xclone.Shared.Models.Media;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.List;

public class MediaUtil
{
    public static void showPreview(FlowPane pane, List<Media> media)
    {
        pane.getChildren().clear();
        int count = Math.min(media.size(), 4);

        if (count == 0) return;
        if (count == 1)
        {
            pane.getChildren().add(createItem(media.get(0), media, pane, 650, 420, true));
        }
        else if (count == 2)
        {
            HBox r = new HBox(5);
            r.getChildren().add(createItem(media.get(0), media, pane, 322,  420, false));
            r.getChildren().add(createItem(media.get(1), media, pane, 322,  420, false));
            pane.getChildren().add(r);
        }
        else if (count == 3)
        {
            VBox root = new VBox(5);
            root.getChildren().add(createItem(media.get(0), media, pane, 650, 210, false));

            HBox r = new HBox(5);
            r.getChildren().add(createItem(media.get(1), media, pane, 322, 205, false));
            r.getChildren().add(createItem(media.get(2), media, pane, 322, 205, false));

            root.getChildren().add(r);
            pane.getChildren().add(root);
        }
        else
        {
            VBox root = new VBox(5);

            HBox r1 = new HBox(5);
            HBox r2 = new HBox(5);

            r1.getChildren().add(createItem(media.get(0), media, pane, 322, 205, false));
            r1.getChildren().add(createItem(media.get(1), media, pane, 322, 205, false));

            r2.getChildren().add(createItem(media.get(2), media, pane, 322, 205, false));
            r2.getChildren().add(createItem(media.get(3), media, pane, 322, 205, false));

            root.getChildren().addAll(r1, r2);

            pane.getChildren().add(root);
        }
    }

    private static StackPane createItem(Media media, List<Media> mediaList, FlowPane pane, double w, double h, boolean p)
    {
        ImageView imageView = new ImageView(new Image(new File(media.getMediaUrl()).toURI().toString()));
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        imageView.setPreserveRatio(p);

        Button remove = new Button("✕");

        remove.setStyle("""
            -fx-background-color:transparent;
            -fx-text-fill:white;
            -fx-background-radius:50;
            """);

        StackPane stackPane = new StackPane(imageView, remove);
        StackPane.setAlignment(remove, javafx.geometry.Pos.TOP_RIGHT);

        remove.setOnAction(e -> {
            mediaList.remove(media);
            showPreview(pane, mediaList);
        });

        return stackPane;
    }

    public static void showTweet(FlowPane pane, List<Media> media)
    {
        pane.getChildren().clear();
        int count = Math.min(media.size(), 4);

        if (count == 0) return;
        if (count == 1)
        {
            pane.getChildren().add(createTweetItem(media.get(0), 650, 420, true));
        }
        else if (count == 2)
        {
            HBox row = new HBox(5);
            row.getChildren().add(createTweetItem(media.get(0), 322, 420, false));
            row.getChildren().add(createTweetItem(media.get(1), 322, 420, false));
            pane.getChildren().add(row);
        }
        else if (count == 3)
        {
            VBox root = new VBox(5);
            root.getChildren().add(createTweetItem(media.get(0), 650, 210, false));

            HBox r = new HBox(5);
            r.getChildren().add(createTweetItem(media.get(1), 322, 205, false));
            r.getChildren().add(createTweetItem(media.get(2), 322, 205, false));

            root.getChildren().add(r);
            pane.getChildren().add(root);
        }
        else
        {
            VBox root = new VBox(5);

            HBox r1 = new HBox(5);
            HBox r2 = new HBox(5);

            r1.getChildren().add(createTweetItem(media.get(0), 322, 205, false));
            r1.getChildren().add(createTweetItem(media.get(1), 322, 205, false));

            r2.getChildren().add(createTweetItem(media.get(2), 322, 205, false));
            r2.getChildren().add(createTweetItem(media.get(3), 322, 205, false));

            root.getChildren().addAll(r1, r2);
            pane.getChildren().add(root);
        }
    }

    private static StackPane createTweetItem(Media media, double w, double h, boolean p)
    {
        ImageView imageView = new ImageView(new Image(new File(media.getMediaUrl()).toURI().toString()));

        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        imageView.setPreserveRatio(p);
        imageView.setOnMouseClicked(e -> openImage(media));

        return new StackPane(imageView);
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
            mediaList.add(new Media(file.getAbsolutePath(), "image", mediaList.size()));
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

        stage.setScene(scene);
        stage.show();
    }
}

