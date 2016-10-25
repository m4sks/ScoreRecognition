package scoreRecognition;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ScoreRecognition {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

	public static void main(String[] args) {
        ImageLoader loader = new ImageLoader();
        ImageViewer viewer = new ImageViewer();
        loader.loadImage("./pictures/clairdelune_fixed.png");
        viewer.show(loader.getInputMat());

    }
}
