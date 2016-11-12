package scoreRecognition;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by ev50063 on 2016/11/11.
 */
public class SymbolsRecognizer {
    private Mat labeledMat;
    private Mat coloredMat;
    
    SymbolsRecognizer(Mat inputMat) {
        Labeler labeler = new Labeler(inputMat);
        labeledMat = labeler.getLabeledMat().clone();
        
        SpecificSymbolsRecognizer specificsRecognition = new SpecificSymbolsRecognizer();
        NotesRecognizer notesRecognizer = new NotesRecognizer();
        AbstractSymbolsRecognizer abstractsRecognition = new AbstractSymbolsRecognizer();
        
        labeler.coloringLabels();
        coloredMat = labeler.getColoredMat().clone();
    }
    
    public Mat getLabeledMat() {
        return labeledMat;
    }
    
    public Mat getColoredMat() {
        return coloredMat;
    }
}
