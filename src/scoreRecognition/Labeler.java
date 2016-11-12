package scoreRecognition;

import java.util.Random;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by ev50063 on 2016/10/25.
 */
public class Labeler {
    private Mat inputMat;
    private Mat labeledMat;
    private Mat stats;
    private Mat centroids;
    private int labelNum;
    private Mat coloredMat;
    
    //Labeler() {}
    
    Labeler(Mat input) {
        inputMat = input.clone();
        labeledMat = new Mat(inputMat.size(), CvType.CV_32S);
        stats = new Mat();
        centroids = new Mat();
        labelNum = Imgproc.connectedComponents(inputMat, labeledMat);
        labeling();
    }
    
    /*public void setInputMat(Mat input) {
        inputMat = input.clone();
        labeledMat = new Mat(inputMat.size(), CvType.CV_32S);
        stats = new Mat();
        centroids = new Mat();
        //labelNum = Imgproc.connectedComponents(inputMat, labeledMat);
    }*/
    
    public void labeling() {
        labelNum = Imgproc.connectedComponentsWithStats(inputMat, labeledMat, stats, centroids, 8, CvType.CV_32S);
        //labelNum = Imgproc.connectedComponents(inputMat, labeledMat, 8, CvType.CV_32S);
        System.out.println("Labeling - " + labelNum);
    }
    
    public Mat getInputMat() {
        return inputMat;
    }
    
    public Mat getLabeledMat() {
        return labeledMat;
    }
    
    public int getLabelNum() {
        return labelNum;
    }
    
    public Mat getStatsMat() {
        return stats;
    }
    
    public Mat getCentroidsMat() {
        return centroids;
    }
    
    public void coloringLabels() {
        coloredMat = inputMat.clone();
        double r = 0.0;
        double g = 0.0;
        double b = 0.0;
        double[][] color = new double[labelNum][3];
        
        Random rand = new Random();
        for (int i = 0; i < labelNum; i++) {
            r = (double) rand.nextInt(256);
            g = (double) rand.nextInt(256);
            b = (double) rand.nextInt(256);
            color[i][0] = b;
            color[i][1] = g;
            color[i][2] = r;
            System.out.println(i + ": " + r + ", " + g + ", " + b);
        }
        
        for (int i = 0; i < labeledMat.rows(); i++) {
            for (int j = 0; j < labeledMat.cols(); j++) {
                for (double label = 1.0; label < labelNum; label++) {
                    if (labeledMat.get(i, j)[0] == label) {
                        coloredMat.put(i, j, color[(int)label][0], color[(int)label][1], color[(int)label][2]);
                    }
                }
            }
        }
    }
    
    public Mat getColoredMat() {
        return coloredMat;
    }
}