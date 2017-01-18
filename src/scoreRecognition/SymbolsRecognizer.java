package scoreRecognition;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Created by ev50063 on 2016/11/11.
 */
public class SymbolsRecognizer {
    //private Mat coloredMat;
    private Mat staveRemovedMat;
    private Mat labeledMat;
    private Mat[] roiMat;
    private Rect rects[];
    
    private double[] stavePos;
    private double staffSpace;
    private double staveWidth;
    
    SymbolsRecognizer(Mat invertedMat) {
        ImageViewer viewer = new ImageViewer();
        
        StaveProcessor staveProcessor = new StaveProcessor(invertedMat, true);
        viewer.show(staveProcessor.getLinedMat());
        this.staveRemovedMat = staveProcessor.getRemovedMat();
        this.stavePos = staveProcessor.getStavePos();
        this.staffSpace = staveProcessor.getLineSpace();
        this.staveWidth = staveProcessor.getStaveWidth();
        viewer.show(staveRemovedMat);
        
        Labeler labeler = new Labeler(staveRemovedMat);
        SpecificSymbolsRecognizer specificsRecognition = new SpecificSymbolsRecognizer();
        NotesRecognizer notesRecognizer = new NotesRecognizer(stavePos, staffSpace, staveWidth);
        AbstractSymbolsRecognizer abstractsRecognition = new AbstractSymbolsRecognizer();
        
        
        //** labeling *********************
        //labeler.coloringLabels();
        //coloredMat = labeler.getColoredMat();
        labeler.sortLabels();
        labeler.detectLabels(labeler.getInputMat());
        this.labeledMat = labeler.getDetectedMat();
        this.roiMat = labeler.getRoiMat();
        this.rects = labeler.getROIRects();
        labeler.drawLabelRect(labeledMat);
        viewer.show(labeler.getLabeledMat());
        
        //** recognizing *********************
        System.out.println("start recognize note");
        //notesRecognizer.recognizeNotes(roiMat[3]);
        for (int i = 1; i < roiMat.length; i++) {
            System.out.println("* recognize roi " + i);
            notesRecognizer.recognizeNotes(roiMat[i]);
            viewer.show(roiMat[i], ""+i);
        }
        System.out.println("end recognize note");
        /*for (int i = 1; i < roiMat.length; i++) {
            notesRecognizer.recognizeNotes(roiMat[i]);
        }*/
        
    }
    
    /*public Mat getColoredMat() {
        return coloredMat;
    }*/
    
    public Mat getLabeledMat() {
        return labeledMat;
    }
    
    public Mat[] getRoiMat() {
        return  roiMat;
    }
    
    /*public void viewVisibleDetect() {
        
    }*/
}