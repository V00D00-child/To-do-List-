package com.bowmanidris.ToDoList;

import com.bowmanidris.ToDoList.dataModel.ToDoData;
import com.bowmanidris.ToDoList.dataModel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogControllerEdit {
    @FXML
    private TextField editShortDescriptionField;

    @FXML
    private TextArea editDetailArea;

    @FXML
    private DatePicker editDealLinePicker;

    public void initialize(){


    }//end initialize()

    public void setEditShortDescriptionField(String editShortDescriptionField) {
        this.editShortDescriptionField.setText(editShortDescriptionField);
    }

    public void setEditDetailArea(String editDetailArea) {
        this.editDetailArea.setText(editDetailArea);
    }


    public ToDoItem editResults(){
        String shortDescription = editShortDescriptionField.getText().trim();
        String details = editDetailArea.getText().trim();
        LocalDate deadLineValue = editDealLinePicker.getValue();

        //add the new data to the todo list
        ToDoItem newItem = new ToDoItem(shortDescription,details,deadLineValue);
        ToDoData.getInstance().editTodoItem(newItem);
        return newItem;

    }//end processResults()


    public void emptyCheck(){
        

    }//end emptyCheck()

}//end DialogController()
