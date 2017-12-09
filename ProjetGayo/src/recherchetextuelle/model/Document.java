package recherchetextuelle.model;


import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Document {

	    private final StringProperty path;
	    private final FloatProperty score;
	    

	    /**
	     * Default constructor.
	     */
	    public Document() {
	        this(null, null);
	    }

	    public Document(String path, Float score) {
	        this.path = new SimpleStringProperty(path);
	        this.score = new SimpleFloatProperty(score);
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

	    public Float getScore() {
	        return score.get();
	    }

	    public void setScore(Float score) {
	        this.score.set(score);
	    }

	    public FloatProperty scoreProperty() {
	        return score;
	    }

	  
	
}
