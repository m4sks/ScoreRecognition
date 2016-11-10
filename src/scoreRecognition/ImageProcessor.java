package scoreRecognition;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by ev50063 on 2016/10/25.
 */
public class ImageProcessor {
    private Mat grayscaledMat;
    private Mat binarizedMat;
    
    ImageProcessor() {
        grayscaledMat = new Mat();
        binarizedMat = new Mat();
    }
    
    public void grayScale(Mat image) {
        try {
            Imgproc.cvtColor(image, grayscaledMat, Imgproc.COLOR_RGB2GRAY);
        }catch (CvException e) {
            throw new CvException("Failed to make gray Scale. Exception thrown: " + e);
        }
        System.out.println("GrayScale - " + image);
    }
    
    public void binarize(Mat image) {
        try {
            Imgproc.threshold(image, binarizedMat, Imgproc.THRESH_BINARY, 255, Imgproc.THRESH_OTSU);
            //Imgproc.adaptiveThreshold(image, outputMat, Imgproc.THRESH_BINARY, 255, Imgproc.THRESH_OTSU, 11, 7);
        }catch (CvException e) {
            throw new CvException("Failed to make binary image. Exception thrown: " + e);
        }
        System.out.println("Binarize - " + image);
    }
    
    public Mat getGrayscaledMat() {
        return grayscaledMat;
    }
    
    public Mat getBinarizedMat() {
        return binarizedMat;
    }
}