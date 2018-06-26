package com.bowmanidris.ToDoList;

import com.bowmanidris.ToDoList.dataModel.ToDoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        //before the program runs the start method load the saved data from the text file
        try {
            ToDoData.getInstance().loadTodoItems();

        }catch(IOException e){
            System.out.println(e.getMessage());

        }//end catch

    }//end init()

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("To Do List");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }//end start()


    public static void main(String[] args) {
        launch(args);
    }//end main method


    @Override
    public void stop() throws Exception {

        //when the program stops save the data to the text file
        try {
            ToDoData.getInstance().storeTodoItems();

        }catch(IOException e){
            System.out.println(e.getMessage());

        }//end catch

    }//end stop()
}//end Main class
