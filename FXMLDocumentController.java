/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phrasebuilderv2;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.UnaryOperator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import javax.swing.JOptionPane;

/*********************************************************************************************************/

public class FXMLDocumentController implements Initializable 
{
    @FXML
    private AnchorPane mainPane;
    @FXML
    private RadioButton manualSelectRdo;
    @FXML
    private RadioButton randomSelectRdo;
    @FXML
    private ToggleGroup phraseOptions;
    @FXML
    private TextField phraseCountInput;
    @FXML
    private Button selectPhrasesButton;
    @FXML
    private Button keywordFileButton;
    @FXML
    private Button clearKeywords;
    @FXML
    private TextField fileOutputName;
    @FXML
    private TextField destinationText;
    @FXML
    private Button fileBrowser;
    @FXML
    private Button submitButton;
    @FXML
    private TextField finalNameText;
    @FXML
    private Button generatePhraseButton;
    
    @FXML
    private ListView<String> phraseSelectListview;
    private final ObservableList<String> phraseSelectList = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> phrasesSelectedListview;
    private final ObservableList<String> phrasesSelectedList = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> kwFileSelectListview;
    private final ObservableList<String> kwFileSelectList = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> kwListView;
    private final ObservableList<String> kwList = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> kwFilesSelectedListview;
    private final ObservableList<String> kwFilesSelectedList = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> generatedPhrasesListview;
    private final ObservableList<String> generatedPhrasesList = FXCollections.observableArrayList();
    
    private ObservableList<String> defaultKeywordFileList = FXCollections.observableArrayList();
    private ObservableList<String> defaultPhrases = FXCollections.observableArrayList();
    private ObservableList<String> existingPhrases = FXCollections.observableArrayList();
    private ObservableList<String> duplicatePhrases = FXCollections.observableArrayList();
    private ObservableList<String> finalPhrases = FXCollections.observableArrayList();
    private int phraseCount = 0;
    private String errors;
    private String outName;
    private String kw = "KW";
    private String defaultSaveLocation = System.getProperty("user.home") + File.separator + "OneDrive\\Desktop";
    @FXML
    private Button clearPhrases;
    @FXML
    private ListView<String> duplicateListview;
    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;
    @FXML
    private Button keywordClearButton;

/*********************************************************************************************************/    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        destinationText.setText(defaultSaveLocation);
        updateSaveName();
        readPhrases();
        readKeywordFiles();
        
        phraseSelectList.setAll(defaultPhrases);
        kwFileSelectList.setAll(defaultKeywordFileList);
        phraseSelectListview.setItems(phraseSelectList);
        kwFileSelectListview.setItems(kwFileSelectList);
    }    

/*********************************************************************************************************/    
    
    @FXML
    private void unfocusList(MouseEvent event) 
    {
        mainPane.requestFocus();
    }
    
/*********************************************************************************************************/    

    @FXML
    private void focusSelectList(MouseEvent event) 
    {
        phraseSelectListview.requestFocus();
    }
    
/*********************************************************************************************************/    

    @FXML
    private void selectPhrase(MouseEvent event) 
    {
        if(phraseSelectListview.getSelectionModel().getSelectedItem() != null)
        {
            modifyLists(phraseSelectListview, phrasesSelectedList, phraseSelectList);
        
            phraseSelectListview.setItems(phraseSelectList);
            phrasesSelectedListview.setItems(phrasesSelectedList);
        }
    }

/*********************************************************************************************************/    

    @FXML
    private void removePhrase(MouseEvent event) 
    {
        if(phrasesSelectedListview.getSelectionModel().getSelectedItem() != null)
        {
            modifyLists(phrasesSelectedListview, phraseSelectList, phrasesSelectedList);
            
            phraseSelectListview.setItems(phraseSelectList);
            phrasesSelectedListview.setItems(phrasesSelectedList);
        }
        
    }
    
/*********************************************************************************************************/    

    @FXML
    private void manualButtonSelected(MouseEvent event) 
    {
        phraseCountInput.clear();
        phraseCountInput.setBorder(null);
        phraseCountInput.setDisable(true);
        selectPhrasesButton.setDisable(true);
        phraseSelectListview.setDisable(false);
    }
    
/*********************************************************************************************************/    

    @FXML
    private void randomButtonSelected(MouseEvent event) 
    {
        phraseSelectListview.setDisable(true);
        phraseCountInput.setDisable(false);
    }
    
/*********************************************************************************************************/    

    @FXML
    private void focusSelectedList(MouseEvent event) 
    {
        phrasesSelectedListview.requestFocus();
    }
        
/*********************************************************************************************************/    

    @FXML
    private void phraseCountChanged(KeyEvent event) 
    {
        UnaryOperator<Change> integerFilter = change ->
        {
            String input = change.toString();
            
            if(input.matches("([1-9][0-9]*)?"))
            {
                return change;
            }
            
            return null;
        };
        
        phraseCountInput.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        
        phraseCountInput.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> obs, String oldv, String newv) 
            {
                try
                {
                    phraseCountInput.getTextFormatter().getValueConverter().fromString(newv);
                    
                    int value = Integer.parseInt(newv);
                    if(value > 0 && value <= phraseCount )
                    {
                        phraseCountInput.setBorder(null);
                        selectPhrasesButton.setDisable(false);
                    }
                    else
                        throw new NumberFormatException();
                    
                } 
                catch (NumberFormatException e) 
                {       
                    selectPhrasesButton.setDisable(true);
                    
                    phraseCountInput.setBorder(new Border(new BorderStroke(Color.RED, 
                            BorderStrokeStyle.SOLID, new CornerRadii(3), 
                            new BorderWidths(2), new Insets(-2,-2,-2,-2))));
                }       
            }
        });
    }
    
/*********************************************************************************************************/    

    @FXML
    private void selectRandomPhrases(ActionEvent event) 
    {
        phraseSelectList.setAll(defaultPhrases);
        phraseSelectListview.setItems(phraseSelectList);
        
        phrasesSelectedList.clear();
        phrasesSelectedListview.setItems(phrasesSelectedList);
        
        try
        {
            int count = Integer.parseInt(phraseCountInput.getText());
            
            for(int i = 0; i < count; i++)
            {
                int randomNum = ThreadLocalRandom.current().nextInt(0, phraseCount - i);
                String phrase = phraseSelectList.get(randomNum);
                
                phrasesSelectedList.add(phrase);
                phraseSelectList.remove(randomNum);   
            }
        }
        catch(NumberFormatException e)
        {
            System.out.println(e);
        }
    }
    
/*********************************************************************************************************/    

    @FXML
    private void clearSelectedPhrases(ActionEvent event) 
    {
        phrasesSelectedList.clear();
        phraseSelectList.setAll(defaultPhrases);
        
        phrasesSelectedListview.setItems(phrasesSelectedList); 
        phraseSelectListview.setItems(phraseSelectList);
    }
    
/*********************************************************************************************************/    

    @FXML
    private void selectKeywordFile(MouseEvent event) 
    {
        if(kwFileSelectListview.getSelectionModel().getSelectedItem() != null)
        {
            modifyLists(kwFileSelectListview, kwFilesSelectedList, kwFileSelectList);
        
            kwFilesSelectedListview.setItems(kwFilesSelectedList);
            kwFileSelectListview.setItems(kwFileSelectList);
        }
        
        if(kwFilesSelectedList.size() > 0)
        {
            populateKeywordList();
            keywordFileButton.setDisable(false);
            keywordFileButton.setBorder(new Border(new BorderStroke(Color.LIMEGREEN, 
                            BorderStrokeStyle.SOLID, new CornerRadii(3), 
                            new BorderWidths(2), new Insets(-2,-2,-2,-2))));
        }
        else
        {
            keywordFileButton.setDisable(true);
            keywordFileButton.setBorder(null);
        }
    }
    
/*********************************************************************************************************/    

    @FXML
    private void removeKeyword(MouseEvent event) 
    {
        kwList.remove(kwListView.getSelectionModel().getSelectedItem());
        kwListView.getSelectionModel().clearSelection();
        kwListView.setItems(kwList); 
    }
    
/*********************************************************************************************************/    

    @FXML
    private void populateKeywords(ActionEvent event) 
    {
        kwList.clear();
        kwListView.setItems(kwList);
        
        for(String name : kwFilesSelectedList)
        {
            try
            {
                Path dir = Paths.get("kwFiles//" + name);
                String n = dir.toString();
                File f = new File(n);
                Scanner reader = new Scanner(f);
            
                while(reader.hasNextLine())
                {
                    String data = reader.nextLine();
                    kwList.add(data);
                }
                
                reader.close();
                kwListView.setItems(kwList);
            }
            catch(FileNotFoundException e)
            {
                System.out.println(e);
                System.out.println("An error occured locating the file.");
            }
        }
    }
    
/*********************************************************************************************************/    

    @FXML
    private void removeKeywordFile(MouseEvent event) 
    {
        if(kwFilesSelectedListview.getSelectionModel().getSelectedItem() != null)
        {
            modifyLists(kwFilesSelectedListview, kwFileSelectList, kwFilesSelectedList);
        
            kwFilesSelectedListview.setItems(kwFilesSelectedList);
            kwFileSelectListview.setItems(kwFileSelectList);
        }
        
        if(kwFilesSelectedList.size() > 0)
            keywordFileButton.setDisable(false);
        else
        {
            keywordFileButton.setDisable(true);
            keywordFileButton.setBorder(null);
        }
        populateKeywordList();
    }
    
/*********************************************************************************************************/    

    @FXML
    private void clearKeywords(ActionEvent event) 
    {
        kwFilesSelectedList.clear();
        kwFilesSelectedListview.setItems(kwFilesSelectedList);
        kwFileSelectListview.setItems(defaultKeywordFileList);
        keywordFileButton.setDisable(true);
        keywordFileButton.setBorder(null);
    }
    
/*********************************************************************************************************/    

    @FXML
    private void clearKeywordList(ActionEvent event) 
    {
        kwList.clear();
        kwListView.setItems(kwList);
    }
    
/*********************************************************************************************************/    

    @FXML
    private void browseFiles(ActionEvent event) 
    {
        Desktop desktop = Desktop.getDesktop();
        Stage browser = new Stage();
        browser.setTitle("Choose Output Location");
        
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog((Window)browser);
        
        if (selectedDirectory != null)
        {
            destinationText.setText(selectedDirectory.getAbsolutePath()); 
            updateSaveName();
        }
    }
    
/*********************************************************************************************************/    

    @FXML
    private void removeFinalPhrase(MouseEvent event) 
    {
        String phrase = generatedPhrasesListview.getSelectionModel().getSelectedItem();
        
        finalPhrases.remove(phrase);
        
        generatedPhrasesListview.setItems(finalPhrases);
    }
    
/*********************************************************************************************************/    

    @FXML
    private void savePhrases(ActionEvent event) 
    {
        if(verifyInput())
        {
            updateSaveName();
            comparePhrases();
            createNewFile();
        }
        else
        {
            String message = "Please correct the following errors:\n" + errors;
            
            JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
            submitButton.setDisable(true);
        } 
    }
    
/*********************************************************************************************************/    
    
    @FXML
    private void generatePhrases(ActionEvent event) 
    {
        if(verifyInput())
        {
            replaceKeywords();
            submitButton.setDisable(false);
        }
        else
        {
            String message = "Please correct the following errors:\n" + errors;
            
            JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
            submitButton.setDisable(true);
        }
    }
    
/*********************************************************************************************************/

    private void readPhrases()
    {
        try
        {
            File f = new File("phrases.txt");
            try (Scanner reader = new Scanner(f)) {
                while(reader.hasNextLine())
                {
                    String data = reader.nextLine();
                    defaultPhrases.add(data);
                    phraseCount++;
                }
            }
            
            phraseCountInput.setPromptText("Enter a number 1-" + phraseCount);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("An error occured locating the file.");
        }  
    }
    
/*********************************************************************************************************/    
    
    private void readKeywordFiles()
    {        
        Path dir = Paths.get("kwFiles");
        
        try 
        {
            Files.walk(dir).forEach(path -> readKeywordName(path.toFile()));
        } 
        catch (IOException ex) 
        {
            System.out.println(ex);
        }        
    }
    
/*********************************************************************************************************/

    private void readKeywordName(File file)
    {
        if(!file.isDirectory())
        {
            defaultKeywordFileList.add(file.getName());
        } 
    }
    
/*********************************************************************************************************/

    private void modifyLists(ListView<String> selectedItem,
                             ObservableList<String> addTo,
                             ObservableList<String> removeFrom)
    {
        String phrase = selectedItem.getSelectionModel().getSelectedItem();
        
        selectedItem.getSelectionModel().clearSelection();
        
        removeFrom.remove(phrase);
        addTo.add(phrase);
    }
    
/*********************************************************************************************************/
    
    @FXML
    private void updateSaveName()
    {
        String dest = destinationText.getText();
        String name = fileOutputName.getText();
            
        outName = dest + "\\" + name + ".csv";
        finalNameText.setText(outName);
    }
    
/*********************************************************************************************************/

    private boolean verifyInput()
    {
        boolean status = false;
        
        errors = "";
        
        checkDest();
        checkOutputName();
        checkKwFile();
        checkPhrases();
        
        if(errors.length() == 0)
        {
            status = true;
            updateSaveName();
        }
        
        return status;
    }
    
/*********************************************************************************************************/

    private void checkDest()
    {        
        if (destinationText.getText().length() < 1)
            errors += "\nSave location is empty.";
    }

/*********************************************************************************************************/

    private void checkOutputName()
    {
        if(this.fileOutputName.getText().length() < 1)
            errors += "\nOutput name is empty.";
    }

/*********************************************************************************************************/

    private void checkKwFile()
    {
        if(kwList.size() < 1)
            errors += "\nNo keywords selected.";
    }

/*********************************************************************************************************/    

    private void checkPhrases()
    {
        if(this.phrasesSelectedList.size() < 1)
            errors += "\nNo phrases were selected.";
    }  

/*********************************************************************************************************/
    
    private void replaceKeywords()
    {
        for (String s : phrasesSelectedList)
        {
            for (String k : kwList)
            {
                finalPhrases.add(s.replace(kw,k));
            }
        }
        generatedPhrasesListview.setItems(finalPhrases);
    }

/*********************************************************************************************************/
    
    @FXML
    private void clearPhrases(ActionEvent event) 
    {
        generatedPhrasesList.clear();
        duplicatePhrases.clear();
        generatedPhrasesListview.setItems(generatedPhrasesList);
        duplicateListview.setItems(duplicatePhrases);
        submitButton.setDisable(true);
    }
    
/*********************************************************************************************************/    
    
    private void getExistingPhrases()
    {
        existingPhrases.clear();
        updateSaveName();
        Path existingFile = Paths.get(outName, new String[0]);
        File oldPhrases = existingFile.toFile();
        
        System.out.println("Checking for existing file. " + oldPhrases.toString());
        if(Files.exists(existingFile, new java.nio.file.LinkOption[0]))
        {
            System.out.println("File exists");
            try (BufferedReader in = Files.newBufferedReader(Paths.get(oldPhrases.toString())))
            {
                String delim = ",";
                String line = in.readLine();
                
                while(line != null)
                {
                    String s = Arrays.toString(line.split(delim));
                    String finalS = s.substring(3, s.length() - 1);
                    existingPhrases.add(finalS);
                    line = in.readLine();
                }
            }
            catch(IOException e)
            {
                System.out.println(e);
            }                    
        }
    }
    
/*********************************************************************************************************/
    
    private void comparePhrases()
    {
        duplicatePhrases.clear();
        getExistingPhrases();
        
        if(existingPhrases.size() > 0)
        {
            ObservableList<String> tmp = FXCollections.observableArrayList();
            tmp.setAll(finalPhrases);
            for (String newPhrase : finalPhrases)
            {
                for(String oldPhrase : existingPhrases)
                {
                    if(oldPhrase.equals(newPhrase))
                    {
                        duplicatePhrases.add(newPhrase);
                        tmp.remove(newPhrase);
                    }
                }
            }
            finalPhrases.setAll(tmp);
       }
       
       if(duplicatePhrases.size() > 0)
       {
           duplicateListview.setItems(duplicatePhrases);
       }
    }
    
/*********************************************************************************************************/
    
    private void createNewFile()
    {
        try 
        {
            Path out = Paths.get(outName);
            File myObj = out.toFile();
            
            if (!myObj.exists())
                if (myObj.createNewFile()) 
                {
                    System.out.println("File created: " + myObj.getName());
                } 
                else 
                {
                    System.out.println("Error Creating File.");
                }  
        } 
        catch (IOException e) 
        {
            System.out.println(e);
            e.printStackTrace();
        } 
        
        try 
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outName, true));
            
            for (String s : finalPhrases) 
            {
                bw.write("," + s + "\n");
            } 
      
            bw.close();
            
            if(duplicatePhrases.size() > 0)
            {
                int size = duplicatePhrases.size();
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("Duplicate phrases exist.");
                a.setContentText(size + " phrases already exist in " 
                                 + fileOutputName.getText() + ".\n"
                                 + "You may review the duplicates on the"
                                 + " main page.");
                a.showAndWait();
            }
      
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Finished");
            alert.setContentText("The task completed successfully.");
            alert.showAndWait();
        }
        catch (IOException e) 
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());
            alert.showAndWait();
        } 
    }

/*********************************************************************************************************/

    @FXML
    private void generatePhrasesList(Event event) 
    {
        if(tab3.isSelected())
        {
            if(verifyInput())
            {
                replaceKeywords();
                submitButton.setDisable(false);
            }
            else
            {
                String message = "Please correct the following errors:\n" + errors;
            
                JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
                submitButton.setDisable(true);
            }
        }
    }

/*********************************************************************************************************/    
    
    private void populateKeywordList()
    {
        kwList.clear();
        kwListView.setItems(kwList);
        
        for(String name : kwFilesSelectedList)
        {
            try
            {
                Path dir = Paths.get("kwFiles//" + name);
                String n = dir.toString();
                File f = new File(n);
                Scanner reader = new Scanner(f);
            
                while(reader.hasNextLine())
                {
                    String data = reader.nextLine();
                    kwList.add(data);
                }
                
                reader.close();
                kwListView.setItems(kwList);
            }
            catch(FileNotFoundException e)
            {
                System.out.println(e);
                System.out.println("An error occured locating the file.");
            }
        }
    }
}
