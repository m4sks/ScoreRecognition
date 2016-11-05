package scoreRecognition;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Imgproc.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ev50063 on 2016/10/25.
 */
public class Labeler {
    private Mat inputMat;
    private Mat labeledMat;
    private Mat stats;
    private Mat centroids;
    private int labelNum;

    Labeler() {}
    
    Labeler(Mat input) {
        inputMat = input;
        labeledMat = new Mat(input.size(), CvType.CV_32S);
        stats = new Mat();
        centroids = new Mat();
        labelNum = Imgproc.connectedComponents(inputMat, labeledMat);
        labeling();
    }

    public void setInputMat(Mat input) {
        inputMat = input;
        labeledMat = new Mat(input.size(), CvType.CV_32S);
        stats = new Mat();
        centroids = new Mat();
        labelNum = Imgproc.connectedComponents(inputMat, labeledMat);
    }

    public void labeling() {
        //labelNum = Imgproc.connectedComponentsWithStats(inputMat, labeledMat, stats, centroids);
        //labelNum = connectedComponentsWithStats(input, labeledMat, stats, centroids, 8, 4);
        //labelNum = connectedComponents(input, labeledMat, 8, 4);
        Imgproc.connectedComponents(inputMat, labeledMat, 8, CvType.CV_32S);
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

    public void coloringLabel() {
        //int[][] colors = new ArrayList<>
    }
}
