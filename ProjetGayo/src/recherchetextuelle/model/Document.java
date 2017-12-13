package recherchetextuelle.model;


import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Document {

	private final StringProperty i;
	private final StringProperty path;
	/*private final FloatProperty score;*/
	    

	    
		/**
	     * Default constructor.
	     */

	  /*
    public Document() {
        this(null, null, null);
    }*/
    
    
    public Document(String i, String path) {
	        this.i = new SimpleStringProperty(i);
	    	this.path = new SimpleStringProperty(path);

		}
	    



		public String getPath() {
	        return path.get();
	    }

	    public void setPath(String path) {
	        this.path.set(path);
	    }

	    public StringProperty pathProperty() {
	        return path;
	    }

	    /*
	    public FloatProperty scoreProperty() {
	        return score;
	    }
	    public Float getScore() {
	        return scoreProperty().get();
	    }

	    public void setScore(Float score) {
	        this.scoreProperty().set(score);
	    }
	    */

	    public StringProperty iProperty() {
	        return i;
	    }
	    public String getI() {
	        return iProperty().get();
	    }

	    public void setI(String i) {
	        this.iProperty().set(i);
	    }
	
}
