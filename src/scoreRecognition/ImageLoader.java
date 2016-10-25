package scoreRecognition;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Created by ev50063 on 2016/10/25.
 */
public class ImageLoader {
    String inputPath;
    Mat inputMat;

    ImageLoader(){}

    public void loadImage(String picPath) {
        inputPath = picPath;
        inputMat = Imgcodecs.imread(inputPath);
    }

    public String getInputPath() {
        return inputPath;
    }

    public Mat getInputMat() {
        return inputMat;
    }

    /*public void displayInputImage() {
        if(inputMat.dataAddr()==0){
            System.out.println("Couldn't open file " + inputPath);
        } else{
            ImageViewer imageViewer = new ImageViewer();
            imageViewer.show(inputMat, "Loaded image");
        }
    }*/

    public void saveMat(Mat saveMat, String savePath) {
        Imgcodecs.imwrite(savePath, saveMat);
    }
}
