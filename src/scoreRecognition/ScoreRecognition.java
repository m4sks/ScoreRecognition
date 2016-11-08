package scoreRecognition;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ScoreRecognition {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
	public static void main(String[] args) {
        ImageViewer viewer = new ImageViewer();
        
        //load image
        ImageLoader loader = new ImageLoader("./pictures/clairdelune_fixed.png");
        //loader.loadImage("./pictures/clairdelune_fixed.png");
        //viewer.show(loader.getInputMat());
        
        //image processing
        ImageProcessor processor = new ImageProcessor();
        processor.grayScale(loader.getInputMat());
        processor.binarize(processor.getGrayscaledMat());
        
        
        /*ImageLoader loader1 = new ImageLoader("./pictures/rest.png");
        ImageProcessor processor1 = new ImageProcessor();
        processor1.grayScale(loader1.getInputMat());
        processor1.binarize(processor1.getBinarizedMat());
        */
        
        //image recognition
        // - labeling
        Labeler labeler = new Labeler(processor.getBinarizedMat());
        //labeler.setInputMat(processor1.getBinarizedMat());
        
        //viewer.show(labeler.getInputMat());
        System.out.println(labeler.getLabelNum());
        
        
        //viewer.show(processor.getBinarizedMat());
    }
}