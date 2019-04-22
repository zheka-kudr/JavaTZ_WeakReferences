import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.WeakHashMap;

public class SlajderController extends Application {
    public Button chooseFolderButoon;
    public Button statrButton;
    public Button previousButton;
    public Button nextButton;

    public ImageView imageLeft;
    public ImageView imageRight;

    private Stage stage;

    private File[] imageList;
    private int counterImage = 0;
    private WeakHashMap<File, WeakReference<Image>> wHP = new WeakHashMap<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws RuntimeException, OutOfMemoryError, IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Slajder.fxml"));
            Scene scene = new Scene(root, 836, 500);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            this.stage = primaryStage;
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }


    public void onClickFolderButton(MouseEvent mouseEvent) throws NullPointerException {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            imageList = directoryChooser.showDialog(this.stage).listFiles();
        } catch (NullPointerException e) {
            System.out.println(e.toString());
        }
    }


    public void onStartButton(MouseEvent mouseEvent) {
        try {
            initModule();
            setImage();
        } catch (RuntimeException e){
            System.out.println(e.toString());
        }

    }

    private void initModule() {
        if (counterImage == 0) {
            previousButton.setDisable(true);
        }

    }

    public void onPreviousButton(MouseEvent mouseEvent) {
        try {
            if (counterImage == 0) {
                previousButton.setDisable(true);
            } else {
                counterImage--;
            }

            if (counterImage < imageList.length - 3) {
                nextButton.setDisable(false);
            }
            setImage();
        } catch (RuntimeException e){
            System.out.println(e.toString());
        }
    }

    public void onNextButton(MouseEvent mouseEvent) {
        try {
            if (counterImage == imageList.length - 2) {
                nextButton.setDisable(true);
            } else {
                counterImage++;
            }

            if (counterImage > 1) {
                previousButton.setDisable(false);
            }
            setImage();
        }catch (RuntimeException e){
            System.out.println(e.toString());
        }
    }

    private void addImage(File file) throws FileNotFoundException, OutOfMemoryError {
        FileInputStream fIS = new FileInputStream(file);
        Image image = new Image(fIS);
        WeakReference<Image> imageWeakReference = new WeakReference<>(image);
        wHP.put(file, imageWeakReference);
        //imageLeft.setImage(image);

    }

    private void setImage() throws RuntimeException, OutOfMemoryError{
        WeakReference<Image> imageWeakReferenceLeft = wHP.get(imageList[counterImage]);
        WeakReference<Image> imageWeakReferenceRight = wHP.get(imageList[counterImage + 1]);
        try {
            if (imageWeakReferenceLeft.get() != null && imageWeakReferenceRight.get() != null) {
                imageLeft.setImage(imageWeakReferenceLeft.get());
                imageRight.setImage(imageWeakReferenceRight.get());
            }
        } catch (NullPointerException e) {
            try {
                addImage(imageList[counterImage]);
                addImage(imageList[counterImage + 1]);
                setImage();
            } catch (FileNotFoundException e1) {
                System.out.println(e1.toString());
            } catch (OutOfMemoryError e1) {
                System.out.println(e1.toString());
                System.gc();
                setImage();
            }
        } catch (RuntimeException e1) {
            System.out.println(e1.toString());
        }

    }
}
