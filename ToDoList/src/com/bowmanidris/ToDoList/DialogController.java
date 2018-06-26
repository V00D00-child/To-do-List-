package com.bowmanidris.ToDoList;

import com.bowmanidris.ToDoList.dataModel.ToDoData;
import com.bowmanidris.ToDoList.dataModel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea detailArea;

    @FXML
    private DatePicker dealLinePicker;



    public void initialize(){


    }//end initialize()


    public ToDoItem processResults(){
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailArea.getText().trim();
        LocalDate deadLineValue = dealLinePicker.getValue();

        //add the new data to the todo list
        ToDoItem newItem = new ToDoItem(shortDescription,details,deadLineValue);
        ToDoData.getInstance().addTodoItem(newItem);
        return newItem;


    }//end processResults()



}//end DialogController()
