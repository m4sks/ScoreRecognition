package scoreRecognition;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.lang.Math;

/**
 * Created by ev50063 on 2016/11/08.
 */
public class StaffProcessor {
    private Mat linesMat;
    private Mat edgesMat;
    private double rho = 5;
    private double theta = Math.PI / 180.0 * 90;
    private int threshold = 200;
    private double srn = 0;
    private double stn = 0;
    private double minTheta = 80;
    private double maxTheta = 100;
    
    //private int minLineLength = 30;
    //private int maxLineGap = 5;
    
    private Mat cannyedMat;
    private Mat outputMat;
    
    StaffProcessor(Mat inputMat) {
        //Imgproc.HoughLines();
        //Imgproc.HoughLines(inputMat, lines, rho, theta, threshold, srn, stn, minTheta, maxTheta);
        
        //Imgproc.HoughLines(inputMat, lines, rho, theta, threshold);
        //Imgproc.HoughLinesP(inputMat, lines, rho, theta, threshold, minLineLength, maxLineGap);
        
        edgesMat = new Mat();
        linesMat = new Mat();
        outputMat = new Mat();
        Imgproc.Canny(inputMat, edgesMat, 150,100 );
        //Imgproc.HoughLinesP(edgesMat, linesMat, 5, Math.PI/180, 200, 30, 5);
        Imgproc.HoughLines(edgesMat, linesMat, rho, theta, threshold, srn, stn, minTheta, maxTheta);
        System.out.println("HoughLines by rho:" + rho + ", theta:" + theta + ", threshold:" + threshold + ", srn:" + srn + ", stn:" + stn + ", minTheta:" + minTheta + ", maxTheta:" + maxTheta);
        
        /*int[] l = new int[4];
        for (int a=0; a < linesMat.cols(); ++a) {
            linesMat.get(0,  a, l);
            Imgproc.line(outputMat, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 255, 0), 2);
        }*/
    }
    
    public Mat getLinesMat() {
        return linesMat;
    }
    
    public Mat getLinedMat() {
        return outputMat;
    }
}