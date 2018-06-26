package com.bowmanidris.ToDoList;

import com.bowmanidris.ToDoList.dataModel.ToDoData;
import com.bowmanidris.ToDoList.dataModel.ToDoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    private List<ToDoItem> toDoItems; //List of to do items

    @FXML
    private ListView<ToDoItem> todoListView; //create listView to match the UI listView

    @FXML
    private TextArea itemDetailTextArea;

    @FXML
    private Label deadLineLabel;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private ToggleButton filterToggleButton;

    private FilteredList<ToDoItem> filteredList;

    private Predicate<ToDoItem> wantAllItems;
    private Predicate<ToDoItem> wantTodaysItems;


    public void initialize(){

        // initialize the contextMenu when the application starts
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");


        //Override setOnAction() to delete  the selected item
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //get item currently selected in the list and call deleteItem()
                ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        //Override setOnAction() to edit  the selected item
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //get item currently selected in the list and call deleteItem()
                ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
                editItem(item);
            }
        });


        //add the menuItems to the listContext menu
        listContextMenu.getItems().addAll(deleteMenuItem);
        listContextMenu.getItems().addAll(editMenuItem);


        //when the application start display the first todo item to the user
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
               if (newValue != null){
                   ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
                   itemDetailTextArea.setText(item.getDetails());

                   //format the date
                   DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                   deadLineLabel.setText(df.format(item.getDeadLine()));
               }//end if statement
            }
        });

        //initialize the two Predicates
        wantAllItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return true;
            }
        };

        wantTodaysItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
            //show items that has current deadline date
                return (toDoItem.getDeadLine().equals(LocalDate.now()));
            }
        };

        //use filtered list to control what items the user can see
        filteredList = new FilteredList<ToDoItem>(ToDoData.getInstance().getToDoItems(),wantAllItems);

        // Add todo items to a sortedList the sort the items by the deadline date
        SortedList<ToDoItem>  sortedList =  new SortedList<ToDoItem>(filteredList,
                new Comparator<ToDoItem>() {
                    @Override
                    public int compare(ToDoItem o1, ToDoItem o2) {
                        //return 0 if items are equal
                        //if o1 less than o2 return a negative value
                        ////if o1 greater than o2 return a postive value
                        return o1.getDeadLine().compareTo(o2.getDeadLine());

                    }//end compare()
                });

        //add the ArrayList of items to the listView UI
        todoListView.setItems(sortedList);//add the sorted items to the listView
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);//select one item at a time
        todoListView.getSelectionModel().selectFirst();//when the application start show the first todo item

        // Highlight todo item with red if deadline date is current date
        //create a custom cell factory
        todoListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                ListCell<ToDoItem> cell = new ListCell<ToDoItem>(){
                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                        }else{
                            setText(item.getShortDescription());
                            if (item.getDeadLine().isBefore(LocalDate.now().plusDays(1))){
                                setTextFill(Color.RED);//highlight red if deadline is current date or in the past

                            }else if (item.getDeadLine().equals(LocalDate.now().plusDays(3))){
                                setTextFill(Color.GREEN); //highlight brown if deadline is 1 day from current date
                            }

                        }//end if else
                    }
                };

                    cell.emptyProperty().addListener(
                            (abs,wasEmpty, isNowEmpty) ->{
                                if (isNowEmpty){
                                    cell.setContextMenu(null);
                                }else {
                                    cell.setContextMenu(listContextMenu);
                                }
                            }
                            );

                    return cell;
            }
        });

}//end initialize()

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());//java video add dialogPane
        dialog.setTitle("Add new Todo item");
        dialog.setHeaderText("Use this dialog to create a new item");

        //add this to allow us to capture the users data entered in the dialog window
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        //Use a try catch block to load the fxml file that has the UI to add a new todo item
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        }catch (IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;

        }//end catch

        //add the ok and cancel buttons using the Dialog class
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            //when the ok button is pressed process new item results that the user entered
            DialogController controller = fxmlLoader.getController();
            ToDoItem newItem = controller.processResults();//call the processResults method in the DialogController class
            todoListView.getSelectionModel().select(newItem);//show the new item in the UI listView

        }//end if

    }//end showNewItemDialog()

    @FXML
    public void editItem(ToDoItem item){

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());//java video add dialogPane
        dialog.setTitle("Edit Todo item");
        dialog.setHeaderText("Use this dialog to edit selected item");

        //add this to allow us to capture the users data entered in the dialog window
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialogEdit.fxml"));

        //Use a try catch block to load the fxml file that has the UI to add a new todo item
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        }catch (IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;

        }//end catch

        //add the ok and cancel buttons using the Dialog class
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //create controller and set the inforamtion to the selected item
        DialogControllerEdit controller = fxmlLoader.getController();

        controller.setEditShortDescriptionField(item.getShortDescription());
        controller.setEditDetailArea(item.getDetails());

        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //before processing results check to make sure all fields are not empty

            //when the ok button is pressed process new item results that the user entered
            ToDoData.getInstance().deleteTodoItem(item);//delete the selected item
            ToDoItem newItem = controller.editResults();//call the processResults method in the DialogController class
            todoListView.getSelectionModel().select(newItem);//show the new item in the UI listView

        }//end if

    }//end showEditItemDialog()

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        ToDoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            //check to see if user pressed key
            if (keyEvent.getCode().equals(KeyCode.DELETE));
            deleteItem(selectedItem);
        }//end id


    }//end handleKeyPressed()


    /*
    This method displays the content of the todo item that is clicked on the  listView UI
     */
    @FXML
    public void handleClickListView(){

        //create a ToDoItem object and set it equal to the object that is selected in the ListView when clicked
        ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
        itemDetailTextArea.setText(item.getDetails());//add the clicked listView item to the TextArea
        deadLineLabel.setText(item.getDeadLine().toString());//add the clicked listView items deadline to the dealLine label


    }//end handleClickListView()

    public void deleteItem(ToDoItem item){

        //confirm  deletion with user before deleting the selected item
        Alert alter = new Alert(Alert.AlertType.CONFIRMATION);

        //set context for Alert
        alter.setTitle("Delete Todo Item");
        alter.setTitle("Delete item: " + item.getShortDescription());
        alter.setContentText("Are your sure? Press OK to confirm, or cancel to Back out");

        Optional<ButtonType> results = alter.showAndWait();

        //if the user presses the OK button
        if (results.isPresent() && results.get() == ButtonType.OK){
            ToDoData.getInstance().deleteTodoItem(item);
        }//end if

    }//end deleteItem()

    @FXML
    public void handleFilterButton(){
        ToDoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();

        if (filterToggleButton.isSelected()){
            filteredList.setPredicate(wantTodaysItems);

            if (filteredList.isEmpty()){
                itemDetailTextArea.clear();
                deadLineLabel.setText("");
            }else if (filteredList.contains(selectedItem)){
                todoListView.getSelectionModel().select(selectedItem);
            }else {
                todoListView.getSelectionModel().selectFirst();
            }//end nested if block

        }else {
            filteredList.setPredicate(wantAllItems);
            todoListView.getSelectionModel().select(selectedItem);

        }//end if

    }//end handleFilterButton()

    @FXML
    public void handleExit(){
        Platform.exit();

    }//end handleExit()



}//end Controller class
