package scoreRecognition;
import org.opencv.core.Core;

public class ScoreRecognition {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
	public static void main(String[] args) {
        TestClass test = new TestClass();
        ImageViewer viewer = new ImageViewer();
        
        //Load image
        ImageLoader loader = new ImageLoader("./pictures/quaterNote01.jpg");
        //ImageLoader loader = new ImageLoader("./pictures/clairdelune_fixed.png");
        
        //image processing
        ImageProcessor processor = new ImageProcessor();
        processor.grayScale(loader.getInputMat());
        processor.binarize(processor.getGrayscaledMat());
        //viewer.show(processor.getGrayscaledMat());
        
        //image recognition
        // - staves recognition and processing
        //StavesProcessor stavesProcessor = new StavesProcessor(processor.getBinarizedMat());
        //test.matInfo(stavesProcessor.getLinesMat());
        //test.matInfo(stavesProcessor.getLinedMat());
        //viewer.show(stavesProcessor.getLinedMat());
        //viewer.show(processor.getBinarizedMat());
        
        // - labeling and labeled symbol recognition
        SymbolsRecognizer symbolsRecognizer = new SymbolsRecognizer(processor.getBinarizedMat());
    }
}