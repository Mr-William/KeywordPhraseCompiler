package phrasebuilderv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author William Gibbs
 */
public class PhraseBuilderV2 extends Application 
{

    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Phrase Builder 2.0");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
        launch(args);
        }
        catch (Throwable t)
        {
            JOptionPane.showMessageDialog(null, t.getClass().getSimpleName() 
                                          + ": " + t.getMessage());
            
            throw t;
        }
    }
    
}
