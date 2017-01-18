package scoreRecognition;

import java.util.Random;

import org.opencv.core.*;
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
    private Mat detectedMat;
    private Rect[] rects;
    private Mat[] roiMat;
    
    Labeler(Mat binarizedMat) {
        inputMat = binarizedMat.clone();
        labeledMat = new Mat(inputMat.size(), CvType.CV_32S);
        stats = new Mat();
        centroids = new Mat();
        labeling();
    }
    
    public void labeling() {
        labelNum = Imgproc.connectedComponentsWithStats(inputMat, labeledMat, stats, centroids, 8, CvType.CV_32S);
        //labelNum = Imgproc.connectedComponentsWithStats(inputMat, labeledMat, stats, centroids, 4, CvType.CV_32S);
        //labelNum = Imgproc.connectedComponents(inputMat, labeledMat, 8, CvType.CV_32S);
        System.out.println("Labeling - " + labelNum + ", " + labeledMat);
    }
    
    public void sortLabels() {
        Mat[] statTemp = new Mat[labelNum];
        Mat[] centTemp = new Mat[labelNum];
        for (int i = 1; i < labelNum - 1; i++) {
            for (int j = 1; j < labelNum - i; j++) {
                if (stats.get(j, 0)[0] > stats.get(j + 1, 0)[0]) {
                    statTemp[j] = stats.row(j).clone();
                    centTemp[j] = centroids.row(j).clone();
                    for (int k = 0; k < stats.cols(); k++) {
                        //System.out.print("temp" + j + " - " + k + ": " + statTemp[j].get(0, k)[0]);
                        //System.out.println("swap " + j + ": " + stats.get(j, k)[0] + ", " + j+1 + ": " + stats.get(j + 1, k)[0]);
                        stats.put(j, k, stats.get(j + 1, k)[0]);
                        stats.put(j + 1, k, statTemp[j].get(0, k)[0]);
                    }
                    for (int k = 0; k < centroids.cols(); k++) {
                        centroids.put(j, k, stats.get(j + 1, k)[0]);
                        centroids.put(j + 1, k, centTemp[j].get(0, k)[0]);
                    }
                    for (int k = 0; k < labeledMat.rows(); k++) {
                        for (int l = 0; l < labeledMat.cols(); l++) {
                            if (labeledMat.get(k, l)[0] == j) {
                                labeledMat.put(k, l, j + 1);
                            }else if (labeledMat.get(k, l)[0] == j + 1) {
                                labeledMat.put(k, l, j);
                            }
                        }
                    }
                }
            }
        }
        /*for (int i = 1; i < stats.rows(); i++) {
            System.out.println(stats.get(i, 0)[0]);
        }*/
        System.out.println("SortLabels");
    }
    
    public void detectLabels(Mat inputMat) {
        detectedMat = inputMat.clone();
        //int rectsNum = stats.rows();
        double left, top, width, height;
        //Scalar color = new Scalar(0, 255, 0);
        //String label = "";
        rects = new Rect[labelNum];
        roiMat = new Mat[labelNum];
        for (int i = 1; i < labelNum ; i++) {
            left = stats.get(i, 0)[0];
            top = stats.get(i, 1)[0];
            width = stats.get(i, 2)[0];
            height = stats.get(i, 3)[0];
            Point pt1 = new Point(left, top);
            Point pt2 = new Point(left + width, top + height);
            rects[i] = new Rect(pt1, pt2);
            //roiMat[i] = inputMat.clone();
            roiMat[i] = new Mat(inputMat, rects[i]);
            /*for (int k = 0; k < roiMat[i].rows(); k++) {
                for (int l = 0; l < roiMat[i].cols(); l++) {
                    if (labeledMat.get(k, l)[0] != i) {
                        roiMat[i].put(k, l, 0, 0, 0);
                    }
                }
            }*/
            //Imgproc.rectangle(detectedMat, pt1, pt2, color);
            //Imgproc.putText(detectedMat, label + i, new Point(left + 5, top + 20), Core.FONT_HERSHEY_COMPLEX, 0.7, new Scalar(0, 255, 255), 2);
            //Imgproc.putText(detectedMat, label + i, new Point(left + 5, top + 20), Core.FONT_ITALIC, 0.7, new Scalar(0, 255, 255), 2);
        }
        System.out.println("Detect Labels - " + detectedMat);
    }
    
    public void drawLabelRect(Mat inputMat) {
        if (inputMat.channels() != 3) {
            Imgproc.cvtColor(inputMat, inputMat, Imgproc.COLOR_GRAY2BGR);
            //System.out.println("Gray to BGR");
        }
        Point p1, p2;
        Scalar color = new Scalar(0, 255, 0);
        String label = "";
        for (int i = 1; i < labelNum ; i++) {
            p1 = rects[i].tl();
            p2 = rects[i].br();
            Imgproc.rectangle(inputMat, p1, p2, color);
            Imgproc.putText(inputMat, label + i, new Point(p1.x + 5, p1.y + 20), Core.FONT_HERSHEY_COMPLEX, 0.7, new Scalar(0, 255, 255), 2);
                //Imgproc.putText(detectedMat, label + i, new Point(left + 5, top + 20), Core.FONT_ITALIC, 0.7, new Scalar(0, 255, 255), 2);
        }
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
            //System.out.println(i + ": " + r + ", " + g + ", " + b);
        }
        
        System.out.print("Coloring Labels start...");
        Size size = labeledMat.size();
        //int area = (int)size.area();
        
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
        System.out.print("end - ");
        System.out.println(coloredMat);
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
    
    public Mat getDetectedMat() {
        return detectedMat;
    }
    
    public Mat[] getRoiMat() {
        return roiMat;
    }
    
    public Rect[] getROIRects() {
        return rects;
    }
}