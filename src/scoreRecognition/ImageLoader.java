package scoreRecognition;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Created by ev50063 on 2016/10/25.
 */
public class ImageLoader {
    private String inputPath;
    private Mat inputMat;
    
    ImageLoader(String picPath){
        inputPath = picPath;
        inputMat = Imgcodecs.imread(inputPath);
        System.out.println("Load image from " + inputPath);
    }
    
    public String getInputPath() {
        return inputPath;
    }
    
    public Mat getInputMat() {
        return inputMat;
    }
    
    public void saveMat(Mat saveMat, String savePath) {
        Imgcodecs.imwrite(savePath, saveMat);
    }
}