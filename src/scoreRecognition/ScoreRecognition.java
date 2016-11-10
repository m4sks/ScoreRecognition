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
        //ImageLoader loader = new ImageLoader("./pictures/clairdelune_fixed.png");
        ImageLoader loader = new ImageLoader("./pictures/quaterNote01.jpg");
        //loader.loadImage("./pictures/clairdelune_fixed.png");
        //viewer.show(loader.getInputMat());
        
        //image processing
        ImageProcessor processor = new ImageProcessor();
        processor.grayScale(loader.getInputMat());
        processor.binarize(processor.getGrayscaledMat());
        viewer.show(processor.getGrayscaledMat());
        
        /*ImageLoader loader1 = new ImageLoader("./pictures/rest.png");
        ImageProcessor processor1 = new ImageProcessor();
        processor1.grayScale(loader1.getInputMat());
        processor1.binarize(processor1.getBinarizedMat());
        */
        
        //image recognition
        // - labeling
        Labeler labeler = new Labeler(processor.getBinarizedMat());
        //labeler.setInputMat(processor1.getBinarizedMat());
        
        // - Staves recognition and procession
        StavesProcessor stavesProcessor = new StavesProcessor(processor.getBinarizedMat());
        test.matInfo(stavesProcessor.getLinesMat());
        test.matInfo(stavesProcessor.getLinedMat());
        //viewer.show(stavesProcessor.getLinedMat());
        //viewer.show(processor.getBinarizedMat());
    }
}