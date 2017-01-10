package scoreRecognition;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Created by ev50063 on 2016/11/11.
 */
public class SymbolsRecognizer {
    //private Mat coloredMat;
    private Mat staveRemovedMat;
    private Mat detectedMat;
    private Mat[] roiMat;
    private Rect rects[];
    
    SymbolsRecognizer(Mat invertedMat) {
        ImageViewer viewer = new ImageViewer();
        
        StaveProcessor staveProcessor = new StaveProcessor(invertedMat, true);
        viewer.show(staveProcessor.getLinedMat());
        
        this.staveRemovedMat = staveProcessor.getRemovedMat();
        viewer.show(staveRemovedMat);
        
        Labeler labeler = new Labeler(staveRemovedMat);
        SpecificSymbolsRecognizer specificsRecognition = new SpecificSymbolsRecognizer();
        NotesRecognizer notesRecognizer = new NotesRecognizer(staveProcessor.getLineSpace());
        AbstractSymbolsRecognizer abstractsRecognition = new AbstractSymbolsRecognizer();
        
        //labeler.coloringLabels();
        //coloredMat = labeler.getColoredMat();
        //labeler.sortLabels();
        labeler.detectLabels(labeler.getInputMat());
        this.detectedMat = labeler.getDetectedMat();
        this.roiMat = labeler.getRoiMat();
        this.rects = labeler.getROIRects();
        notesRecognizer.recognizeNotes(roiMat[3]);
        /*for (int i = 1; i < roiMat.length; i++) {
            notesRecognizer.recognizeNotes(roiMat[i]);
        }*/
        labeler.drawLabelRect(detectedMat);
    }
    
    /*public Mat getColoredMat() {
        return coloredMat;
    }*/
    
    public Mat getDetectedMat() {
        return detectedMat;
    }
    
    public Mat[] getRoiMat() {
        return  roiMat;
    }
    
    public void viewVisibleDetect() {
        
    }
}