package scoreRecognition;

import org.opencv.core.Mat;

/**
 * Created by ev50063 on 2016/11/10.
 */
public class NotesRecognizer {
    /*
    private char c = 'C';
    private char d = 'D';
    private char e = 'E';
    private char f = 'F';
    private char g = 'G';
    private char a = 'A';
    private char h = 'H';
    
    private String Cis = "Cis";
    private String Dis = "Dis";
    private String Eis = "Eis";
    private String Fis = "Fis";
    private String Gis = "Gis";
    private String Ais = "Ais";
    private String His = "His";
    
    private String Ces = "Ces";
    private String Des = "Des";
    private String Es = "Es";
    private String Ges = "Ges";
    private String As = "As";
    private String B = "B";
    */
    
    private String Whole = "Whole-note";
    private String Half = "Half-note";
    private String Quater = "Quater-note";
    private String Eighth = "8th-note";
    private String Sixteenth = "16th-note";
    
    NotesRecognizer() {
        
    }
    
    public String recognizeNotes() {
        String output = "";
        return output;
    }
    
    public boolean judgeBlackNote() {
        boolean output = true;
        return output;
    }
    
    public String recognizePitch() {
        String output = "";
        return output;
    }
}
