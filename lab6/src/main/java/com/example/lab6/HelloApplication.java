package com.example.lab6;

import com.example.lab6.controller.LoginPageController;
import com.example.lab6.domain.validators.MessageValidator;
import com.example.lab6.domain.validators.PrietenieValidator;
import com.example.lab6.domain.validators.UtilizatorValidator;
import com.example.lab6.repository.database.MessageRepoDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.example.lab6.repository.Repository0;
import com.example.lab6.repository.database.PrietenieRepoDB;
import com.example.lab6.repository.database.UtilizatorDB;
import com.example.lab6.service.Service;

import java.io.IOException;

public class HelloApplication extends Application{

    @Override
    public void start(Stage stage) throws IOException{
        String username = "postgres";
        String password = "postgres";
        String url = "jdbc:postgresql://localhost:5433/socialNetwork";

        Repository0 repoPr = new PrietenieRepoDB(url, username, password, new PrietenieValidator());
        Repository0 repoU = new UtilizatorDB(url, username, password, new UtilizatorValidator());
        Repository0 repoM = new MessageRepoDB(url,username, password, new MessageValidator());

        //Service service = new Service(repoU ,repoPr);
        Service service = new Service(repoU,repoPr,repoM);

        FXMLLoader loader = new FXMLLoader();
        //System.out.println(getClass().getResource("loginPage.fxml"));
        loader.setLocation(getClass().getResource("/com/example/lab6/loginPage.fxml"));
        AnchorPane root = loader.load();

        LoginPageController controller = loader.getController();
        controller.setService(service);

        Scene scene = new Scene(root, 432, 377);
        stage.setTitle("Social Network");
        stage.setScene(scene);
        stage.show();

    }

    /*private void initView(Stage stage) throws IOException {

        // FXMLLoader fxmlLoader = new FXMLLoader();
        //fxmlLoader.setLocation(getClass().getResource("com/example/guiex1/views/UtilizatorView.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/UtilizatorView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));

        UtilizatorController userController = fxmlLoader.getController();
        userController.setUtilizatorService(service);

    }*/

    public static void main(String[] args) {
        launch(args);
    }
}