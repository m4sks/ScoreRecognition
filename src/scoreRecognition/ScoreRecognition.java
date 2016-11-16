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
        //ImageLoader loader = new ImageLoader("./pictures/quaterNote01.jpg");
        ImageLoader loader = new ImageLoader("./pictures/dotted-notes.png");
        //ImageLoader loader = new ImageLoader("./pictures/rest.png");
        //ImageLoader loader = new ImageLoader("./pictures/dotted-rest.png");
        //ImageLoader loader = new ImageLoader("./pictures/clairdelune_fixed.png");
        
        //image processing
        ImageProcessor processor = new ImageProcessor();
        processor.grayScale(loader.getInputMat());
        processor.binarize(processor.getGrayscaledMat());
        processor.invert(processor.getBinarizedMat());
        //viewer.show(processor.getGrayscaledMat());
        
        //image recognition
        // - staves recognition and processing
        //StavesProcessor stavesProcessor = new StavesProcessor(processor.getBinarizedMat());
        //test.matInfo(stavesProcessor.getLinesMat());
        //test.matInfo(stavesProcessor.getLinedMat());
        //viewer.show(stavesProcessor.getLinedMat());
        //viewer.show(processor.getBinarizedMat());
        
        // - labeling and labeled symbol recognition
        SymbolsRecognizer symbolsRecognizer = new SymbolsRecognizer(processor.getInvertedMat());
        
        /*for (int i = 0; i < symbolsRecognizer.getLabeledMat().rows(); i++) {
            for (int j = 0; j < symbolsRecognizer.getLabeledMat().cols(); j++) {
                System.out.print((int)symbolsRecognizer.getLabeledMat().get(i, j)[0]);
            }
            System.out.println();
        }*/
        
        //viewer.show(loader.getInputMat());
        //viewer.show(processor.getInvertedMat());
        viewer.show(symbolsRecognizer.getDetectedMat());
        viewer.show(symbolsRecognizer.getRoiMat()[3]);
    }
}