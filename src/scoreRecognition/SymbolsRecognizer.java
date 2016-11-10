package scoreRecognition;

import org.opencv.core.Mat;

/**
 * Created by ev50063 on 2016/11/11.
 */
public class SymbolsRecognizer {
    SymbolsRecognizer(Mat inputMat) {
        Labeler labeler = new Labeler(inputMat);
        
        SpecificSymbolsRecognition specificsRecognition = new SpecificSymbolsRecognition();
        NotesRecognizer notesRecognizer = new NotesRecognizer();
        AbstractSymbolsRecognition abstractsRecognition = new AbstractSymbolsRecognition();
        
        
    }
}
