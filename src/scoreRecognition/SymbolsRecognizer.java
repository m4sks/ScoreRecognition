package scoreRecognition;

import org.opencv.core.Mat;

/**
 * Created by ev50063 on 2016/11/11.
 */
public class SymbolsRecognizer {
    private Mat coloredMat;
    private Mat detectedMat;
    private Mat[] roiMat;
    
    SymbolsRecognizer(Mat invertedMat) {
        Labeler labeler = new Labeler(invertedMat);
        SpecificSymbolsRecognizer specificsRecognition = new SpecificSymbolsRecognizer();
        NotesRecognizer notesRecognizer = new NotesRecognizer();
        AbstractSymbolsRecognizer abstractsRecognition = new AbstractSymbolsRecognizer();
        
        labeler.coloringLabels();
        coloredMat = labeler.getColoredMat();
        labeler.sortLabels();
        labeler.detectLabels(coloredMat);
        detectedMat = labeler.getDetectedMat();
        roiMat = labeler.getRoiMat();
        notesRecognizer.recognizeNotes(roiMat[3]);
    }
    
    public Mat getColoredMat() {
        return coloredMat;
    }
    
    public Mat getDetectedMat() {
        return detectedMat;
    }
    
    public Mat[] getRoiMat() {
        return  roiMat;
    }
}
