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
        ImageLoader loader = new ImageLoader();
        loader.loadImage("./pictures/quaterNote01.jpg");
        //loader.loadImage("./pictures/clairdelune_fixed.png");
        //viewer.show(loader.getInputMat());
        
        //image processing
        ImageProcessor processor = new ImageProcessor();
        processor.grayScale(loader.getInputMat());
        processor.binarize(processor.getGrayscaledMat());
        
        //image recognition
        // - labeling
        Labeler labeler = new Labeler(processor.getBinarizedMat());
        System.out.println(labeler.getLabelNum());
        
        
        viewer.show(processor.getBinarizedMat());
    }
}