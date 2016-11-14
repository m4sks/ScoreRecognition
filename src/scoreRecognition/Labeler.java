package scoreRecognition;

import java.util.Random;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
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
        //labelNum = Imgproc.connectedComponents(inputMat, labeledMat);
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
    
    public void coloringLabels() {
        coloredMat = new Mat();
        Imgproc.cvtColor(inputMat, coloredMat, Imgproc.COLOR_GRAY2BGR);
        double r = 0.0;
        double g = 0.0;
        double b = 0.0;
        double[][] color = new double[labelNum][3];
        
        Random rand = new Random();
        for (int i = 1; i < labelNum; i++) {
            r = (double) rand.nextInt(256);
            g = (double) rand.nextInt(256);
            b = (double) rand.nextInt(256);
            color[i][0] = b;
            color[i][1] = g;
            color[i][2] = r;
            System.out.println(i + ": " + r + ", " + g + ", " + b);
        }
        
        System.out.print("Start Coloring Labels ... ");
        Size size = labeledMat.size();
        int area = (int)size.area();
        
        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                for (double label = 1.0; label < labelNum; label++) {
                    if (labeledMat.get(i, j)[0] == label) {
                        coloredMat.put(i, j, color[(int)label][0], color[(int)label][1], color[(int)label][2]);
                    }
                }
                //if (j % 150 == 0) System.out.print("|");
            }
            //int progress = (int)((i+1) * size.width);
            //if ((i + 1) % 5 == 0) System.out.println(" "+ progress + " / " + area + " :" +  (float)progress/(float)area*100 + "%");
        }
        System.out.println("End Coloring Labels");
        System.out.println(coloredMat);
    }
    
    public void detectLabels() {
        
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
    
    public Mat getColoredMat() {
        return coloredMat;
    }
}