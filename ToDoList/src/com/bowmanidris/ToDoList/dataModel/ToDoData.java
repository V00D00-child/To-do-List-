package com.bowmanidris.ToDoList.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ToDoData {

    private static ToDoData instance = new ToDoData();
    private static String fileName = "TodoListItems.txt";
    private ObservableList<ToDoItem> toDoItems;
    private DateTimeFormatter formatter;

    public static ToDoData getInstance(){
        return instance;
    }

    private ToDoData(){
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void addTodoItem(ToDoItem item){
        toDoItems.add(item);

    }

    public void loadTodoItems()throws IOException{
        toDoItems = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);

        BufferedReader br = Files.newBufferedReader(path);
        String input;

        try {
            while ((input = br.readLine()) != null){

                //create a array that will hold the loaded data
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);//formate the data
                ToDoItem toDoItem = new ToDoItem(shortDescription,details,date);//cretae the todo item
                toDoItems.add(toDoItem);//add the todoItem to the todolist

            }//end while loop

        }finally {
            if (br != null){
                br.close();
            }//end if

        }//end finally block


    }//end loadTodoItems()

    public void storeTodoItems()throws IOException{
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try {
            Iterator<ToDoItem> iter = toDoItems.iterator();
            while (iter.hasNext()){
                ToDoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadLine().format(formatter)));
                bw.newLine();
            }//end while loop block

        }finally {
            if (bw != null){
                bw.close();
            }//end if
        }//end finally block

    }//end storeTodoItems

    public void deleteTodoItem(ToDoItem item){
        toDoItems.remove(item);
    }//end deleteTodoItem()

    public void editTodoItem(ToDoItem item){
        toDoItems.remove(item);
        toDoItems.add(item);
    }//end editTodoItem()



}//end class ToDoData
