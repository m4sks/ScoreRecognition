package scoreRecognition;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.opencv.imgproc.Imgproc.connectedComponentsWithStats;

/**
 * Created by ev50063 on 2016/10/25.
 */
public class Labeler {
    private Mat inputMat;
    private Mat labeledMat;
    private Mat stats;
    private Mat centroids;
    private int labelNum;


    Labeler(Mat input) {
        inputMat = input;
        labeledMat = new Mat(input.size(), CvType.CV_32S);
        stats = new Mat();
        centroids = new Mat();
        labelNum = connectedComponentsWithStats(input, labeledMat, stats, centroids);
        //labelNum = connectedComponentsWithStats(input, labeledMat, stats, centroids, 8, 4);
        //labelNum = connectedComponents(input, labeledMat, 8, 4);
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
        //Vector3 col = new Vector3();
    }
}
