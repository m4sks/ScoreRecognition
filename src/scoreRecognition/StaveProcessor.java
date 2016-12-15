package scoreRecognition;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.lang.Math;
//import java.util.Random;

/**
 * Created by ev50063 on 2016/11/08.
 */
public class StaveProcessor {
    private Mat linesMat;
    //private Mat edgesMat;
    
    private double rho = 3.0;
    private double theta = Math.PI / 180.0 * 90.0;
    private int threshold = 300;
    //private double srn = 0.0;
    //private double stn = 0.0;
    //private double minTheta = 80.0;
    //private double maxTheta = 100.0;
    
    private int minLineLength = 0;
    private int maxLineGap = 3;
    
    //private Mat cannyedMat;
    private Mat linedMat;
    private Mat removedMat;
    
    private int linesNum;
    private double[] yDif;
    private double[] meanY;
    private double meanYDif;
    private double lineSpace;
    private double linesWidth;
    //private double staveSpace;
    private Mat spacePosLines;
    private int[] lineNumber;
    
    StaveProcessor(Mat inputMat) {
        TestClass test = new TestClass();
        //edgesMat = new Mat();
        linesMat = new Mat();
        linedMat = inputMat.clone();
        removedMat = inputMat.clone();
        searchStave(inputMat);
        sortStave();
        drawLines();
        test.matChannel(linesMat, 4);
        test.matInfo(linedMat, "linedMat");
        calcWidth();
        sortYDif();
        clusteringStave();
        removeStave();
    }
    
    private void searchStave(Mat inputMat) {
        //Imgproc.HoughLines();
        //Imgproc.HoughLines(inputMat, lines, rho, theta, threshold, srn, stn, minTheta, maxTheta);
    
        //Imgproc.HoughLines(inputMat, linesMat, rho, theta, threshold);
        //Imgproc.HoughLinesP(inputMat, lines, rho, theta, threshold, minLineLength, maxLineGap);
    
        //Imgproc.Canny(inputMat, edgesMat, 150,100);
        //Imgproc.Canny(inputMat, edgesMat, 80, 100);
        //ImageViewer viewer = new ImageViewer();
        //viewer.show(edgesMat, "edgesMat");
        //Imgproc.HoughLinesP(edgesMat, linesMat, 5, Math.PI/180, 200, 30, 5);
    
        //Imgproc.HoughLinesP(edgesMat, linesMat, 1, Math.PI / 180 , 50, 100 ,10);
        //Imgproc.HoughLines(edgesMat, linesMat, 5, Math.PI/180, threshold);
        //Imgproc.HoughLines(edgesMat, linesMat, rho, theta, threshold, srn, stn, minTheta, maxTheta);
        //Imgproc.HoughLines(inputMat, linesMat, rho, theta, threshold, srn, stn, minTheta, maxTheta);
        //Imgproc.HoughLinesP(inputMat, linesMat, rho, theta, threshold);
        
        minLineLength = (int)(inputMat.cols() * 0.6);
        Imgproc.HoughLinesP(inputMat, linesMat, rho, theta, threshold, minLineLength, maxLineGap);
        //System.out.println("HoughLines - " + inputMat + "\n   by rho:" + rho + ", theta:" + theta + ", threshold:" + threshold + ", srn:" + srn + ", stn:" + stn + ", minTheta:" + minTheta + ", maxTheta:" + maxTheta);
        linesNum = linesMat.rows();
        System.out.println("HoughLinesP - " + inputMat);
        System.out.println("    by rho:" + rho + ", theta:" + theta + ", threshold:" + threshold + ", minLineLength:" + minLineLength + ", maxLineGap:" + maxLineGap);
        //TestClass test = new TestClass();
        //test.matInfo(linesMat, "linesMat");
        //test.matChannel(linesMat, 4);
    }
    
    private void sortStave() {
        double tempLX, tempLY, tempRX, tempRY;
        double mean1, mean2;
        for (int i = 0; i < linesNum - 1; i++) {
            for (int j = 0; j < linesNum - i - 1; j++) {
                mean1 = (linesMat.get(j, 0)[1] + linesMat.get(j, 0)[3]) / 2;
                mean2 = (linesMat.get(j + 1, 0)[1] + linesMat.get(j + 1, 0)[3]) / 2;
                if (mean1 > mean2) {
                    tempLX = linesMat.get(j, 0)[0];
                    tempLY = linesMat.get(j, 0)[1];
                    tempRX = linesMat.get(j, 0)[2];
                    tempRY = linesMat.get(j, 0)[3];
                    linesMat.put(j, 0, linesMat.get(j + 1, 0)[0], linesMat.get(j + 1, 0)[1], linesMat.get(j + 1, 0)[2], linesMat.get(j + 1, 0)[3]);
                    linesMat.put(j + 1, 0, tempLX, tempLY, tempRX, tempRY);
                }
            }
        }
        System.out.println("Sorted stave");
    }
    
    private void calcWidth() {  //think again later
        meanY = new double[linesNum];
        yDif = new double[linesNum - 1];
        double sumYDif = 0;
        for (int i = 0; i < linesNum; i++) {
            meanY[i] = (linesMat.get(i, 0)[1] + linesMat.get(i, 0)[3]) / 2;
            if (i > 0) yDif[i - 1] = meanY[i] - meanY[i - 1];
        }
        for (int i = 0; i < yDif.length; i++) {
            sumYDif += yDif[i];
        }
        meanYDif = sumYDif / yDif.length;
        System.out.println("meanYDif = " + meanYDif);
        for (int i = 0; i < linesNum - 1; i++) {
            if (linesMat.get(i + 1, 0)[1] - linesMat.get(i, 0)[1] < meanYDif) {
                System.out.println("line: " + (i+1));
            }
        }
        linesWidth = 3.0;
    }
    
    private void sortYDif() {
        double yDifTemp;
        lineNumber = new int[yDif.length];
        int lineNumberTemp;
        
        for (int i = 0; i < yDif.length; i++) {
            lineNumber[i] = i;
        }
        for (int i = 1; i < yDif.length - 1; i++) {
            for (int j = 0; j < yDif.length - i; j++) {
                if (yDif[j] > yDif[j + 1]) {
                    yDifTemp = yDif[j];
                    yDif[j] = yDif[j + 1];
                    yDif[j + 1] = yDifTemp;
                    lineNumberTemp = lineNumber[j];
                    lineNumber[j] = lineNumber[j + 1];
                    lineNumber[j + 1] = lineNumberTemp;
                }
            }
        }
    }
    
    private void clusteringStave() {
        int[] cluster3 = new int[yDif.length];
        cluster3 = kmeans(3);
        /*for (int i = 0; i < yDif.length; i++) {
            cluster3[i] = kmeans(3)[i];
        }*/
        
        for (int i = 0; i < yDif.length; i++) {
            //System.out.println("cluster: " + kmeans(3)[i] + ", yDif" + yDif[i]);
            System.out.println("cluster: " + cluster3[i] + ", lineNumber: " + lineNumber[i] + ", yDif: " + yDif[i]);
        }
    }
    
    private int[] kmeans(int centNum) {  //k-means for yDif[]
        double[] centroid = new double[centNum];
        int[] cluster = new int[yDif.length];
        int[] clusterNum = new int[centNum];
        double[][] clusterDist = new double[yDif.length][centNum];
        double[] clusterDifSum = new double[centNum];
        
        boolean done = false;
        
        //Random rand = new Random();
        for (int i = 0; i < yDif.length; i++) {
            if (i == 0) cluster[i] = 0;
            else if (i == cluster.length - 1) cluster[i] = centNum - 1;
            for (int j = 1; j < centNum - 1; j++) {
                if (i < j * (yDif.length - 2) / centNum) {
                    cluster[i] = j;
                }else cluster[i] = centNum - 1;
                //if (i == 0) meanDistTemp[j] = 0;
            }
        }
        while (!done) {
            //initialize
            for (int i = 0; i < centNum; i++) {
                clusterNum[i] = 0;
                clusterDifSum[i] = 0;
                centroid[i] = 0;
            }
            
            //calculate
            for (int i = 0; i < centNum; i++) {
                for (int j = 0; j < yDif.length; j++) {
                    if (cluster[j] == i) clusterNum[i]++;
                }
                for (int j = 0; j < yDif.length; j++) {
                    if (cluster[j] == i) clusterDifSum[i] += yDif[j];
                }
                centroid[i] = clusterDifSum[i] / clusterNum[i];
                for (int j = 0; j < yDif.length; j++) {
                    clusterDist[j][i] = (centroid[i] - yDif[j]) * (centroid[i] - yDif[j]);
                }
            }
            
            //update
            done = true;
            for (int i = 0; i < yDif.length; i++) {
                for (int j = 0; j < cluster[i]; j++) {
                    if (cluster[i] != j) {
                        if (clusterDist[i][cluster[i]] > clusterDist[i][j]) {
                            cluster[i] = cluster[j];
                            done = false;
                        }
                    }
                }
            }
        }
        return cluster;
    }
    
    private void setSpacePosLines() {
        spacePosLines = new Mat();
        
    }
    
    private void drawLines() {
        //double[] lines = new double[4];
        Imgproc.cvtColor(linedMat, linedMat, Imgproc.COLOR_GRAY2BGR);
        Scalar color = new Scalar(0, 255, 0);
        for (int i = 0; i < linesMat.rows(); i++) {
            //lines[i%4] = linesMat.get(i, 0)[0];
            Imgproc.line(linedMat, new Point(linesMat.get(i, 0)[0], linesMat.get(i, 0)[1]), new Point(linesMat.get(i, 0)[2], linesMat.get(i, 0)[3]), color);
            String lineNum = "";
            Imgproc.putText(linedMat, lineNum+i, new Point(linesMat.get(i, 0)[0], linesMat.get(i, 0)[1]), Core.FONT_HERSHEY_COMPLEX, 0.7, new Scalar(0, 255, 255), 2);
        }
        System.out.println("Draw lines for detected stave");
    }
    
    private void removeStave() {
        Mat linesRoi;
        Point left;
        Point right;
        double dif = linesWidth * 2/3;   //2;
        int pixNum;
        for (int i = 0; i < linesMat.rows(); i++) {
            left = new Point(linesMat.get(i, 0)[0], linesMat.get(i, 0)[1] - dif);
            right = new Point(linesMat.get(i, 0)[2], linesMat.get(i, 0)[3] + dif);
            linesRoi = removedMat.submat(new Rect(left, right));
            //linesRoi = new Mat(removedMat, new Rect(left, right));
            //pixNum = 0;
            //TestClass test = new TestClass();
            //test.mat(linesRoi);
            for (int k = 0; k < linesRoi.cols(); k++) {
                pixNum = 0;
                for (int l = 0; l < linesRoi.rows(); l++) {
                    if (linesRoi.get(l, k)[0] == 255.0) pixNum++;
                }
                if (pixNum <= linesWidth) {
                    for (int l = 0; l < linesRoi.rows(); l++) {
                        linesRoi.put(l, k, 0.0);
                    }
                }
            }
        }
    }
    
    public Mat getLinesMat() {
        return linesMat;
    }
    
    public Mat getLinedMat() {
        return linedMat;
    }
    
    public Mat getRemovedMat() {
        return removedMat;
    }
    
    
}