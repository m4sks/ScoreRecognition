package scoreRecognition;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * Created by ev50063 on 2016/11/10.
 */
public class NotesRecognizer {
    /*
    private char c = 'C';
    private char d = 'D';
    private char e = 'E';
    private char f = 'F';
    private char g = 'G';
    private char a = 'A';
    private char h = 'H';
    */
    /*
    private String Whole = "Whole-note";
    private String Half = "Half-note";
    private String Quater = "Quater-note";
    private String Eighth = "8th-note";
    private String Sixteenth = "16th-note";
    */
    /*
    private Mat whiteResultMat;
    private Mat blackResultMat;
    */
    private double[] stavePos;
    private double staffSpace;
    private double staveWidth;
    private double noteWidth;
    
    private boolean isOneNote = false;
    private boolean isBlackNote = false;
    private boolean isUpHead = false;
    
    private int subUpPixel = 0;
    private int subBottomPixel = 0;
    private int subUpCentPixel = 0;
    private int subBottomCentPixel = 0;
    
    private double brackNotePixRate = 0.7;
    private double whiteNotePixRate = 0.4;
    private double subBlackPixRate = 0.95;
    private double subWhitePixRate = 0.2;
    private double oneNoteRate = 2.5;
    private double notePitchPosThreshold;
    private int maxAssistLine = 5;
    
    private Rect[] headDetectRects;
    
    private int num;
    
    NotesRecognizer(double[] stavePos, double staffSpace, double staveWidth) {
        this.stavePos = stavePos;
        this.staffSpace = staffSpace;
        this.staveWidth = staveWidth;
        //this.noteWidth = staffSpace * 1.3;
        this.notePitchPosThreshold = staffSpace * 0.2;
    }
    
    public String recognizeNotes(Mat roiMat, int num) {
        String output = "";
        this.isOneNote = isOneNote(roiMat);
        this.noteWidth = staffSpace * 1.3;
        this.num = num;
        if (noteWidth > roiMat.width()) {
            System.out.println("noteWidth: " + noteWidth + ", " + roiMat.width());
            noteWidth = roiMat.width();
        }
        if (detectHead(roiMat)) {
            output = keyToString(recognizeKey(true));
            System.out.println(output);
            detectHeadVisualize(roiMat);
        }else {
            
        }
        return output;
    }
    
    private boolean detectHead(Mat inputRoiMat) {
        boolean output = false;
        double noteCandidateRectArea = noteWidth * staffSpace;
        double subRectArea = staffSpace * staffSpace / 9;
        double shiftWidth = 2;
        int posGap = 1;
        Rect subUpRect = new Rect(0, 0, (int)noteWidth, (int)staffSpace);
        Rect subBottomRect = new Rect(0, inputRoiMat.rows() - (int)staffSpace, (int)noteWidth, (int)staffSpace);
        Rect subUpCentRect = new Rect((int)noteWidth / 3, (int)staffSpace / 3, (int)noteWidth / 3, (int)staffSpace / 3);
        Rect subBottomCentRect = new Rect((int)noteWidth / 3, inputRoiMat.rows() - (int)staffSpace * 2/3, (int) noteWidth / 3, (int)staffSpace / 3);
        
        if (inputRoiMat.rows() < staffSpace || inputRoiMat.cols() < staffSpace) {
            //System.out.println("is too small");
            return output;
        }
        
        if (inputRoiMat.height() > staveWidth * 1.1) {
            //System.out.println("is too large");
            return output;
        }
        
        //System.out.println("col: " + inputRoiMat.cols() + ", " + staffSpace * oneNoteRate);
         
        if (isOneNote) {
            System.out.println("is one note");
            headDetectRects = new Rect[1];
            Mat subUp = inputRoiMat.submat(subUpRect);
            subUpPixel = 0;
            for (int i = 0; i < subUp.rows(); i++) {
                for (int j = 0; j < subUp.cols(); j++) {
                    if (subUp.get(i, j)[0] == 255.0) {
                        subUpPixel++;
                    }
                }
            }
            System.out.println("subUpPixel: " + subUpPixel + " / " + noteCandidateRectArea);
            
            if (subUpPixel > noteCandidateRectArea * whiteNotePixRate) {
                Mat subUpCent = inputRoiMat.submat(subUpCentRect);
                subUpCentPixel = 0;
                for (int i = 0; i < subUpCent.rows(); i++) {
                    for (int j = 0; j < subUpCent.cols(); j++) {
                        if (subUpCent.get(i, j)[0] == 255.0) {
                            subUpCentPixel++;
                        }
                    }
                }
                System.out.println("subUpCentPixel: " + subUpCentPixel + " / " + subRectArea + " = " + subUpCentPixel / subRectArea);
                if (subUpCentPixel < subRectArea * subWhitePixRate) {
                    output = true;
                    headDetectRects[0] = subUpRect;
                    isBlackNote = false;
                    isUpHead = true;
                    return output;
                }else if (subUpCentPixel > subRectArea * subBlackPixRate) {
                    if (subUpPixel > noteCandidateRectArea * brackNotePixRate) {
                        output = true;
                        headDetectRects[0] = subUpRect;
                        isBlackNote = true;
                        isUpHead = true;
                        return output;
                    }
                }
            }
            
            Mat subBottom = inputRoiMat.submat(subBottomRect);
            boolean doneBottom = false;
            double bottomShift = 0;
            
            while(!doneBottom) {
                subBottomRect = new Rect((int)bottomShift, inputRoiMat.rows() - (int)staffSpace - posGap, (int)noteWidth, (int)staffSpace);
                
                //System.out.println();
                subBottom = inputRoiMat.submat(subBottomRect);
                bottomShift += shiftWidth;
                subBottomPixel = 0;
                for (int i = 0; i < subBottom.rows(); i++) {
                    for (int j = 0; j < subBottom.cols(); j++) {
                        if (subBottom.get(i, j)[0] == 255.0) {
                            subBottomPixel++;
                        }
                    }
                }
                System.out.println("subBottomPixel: " + subBottomPixel + " / " + noteCandidateRectArea + "=" + subBottomPixel / noteCandidateRectArea);
                
                subBottomCentRect = new Rect((int)(noteWidth / 3 + shiftWidth), inputRoiMat.rows() - (int)staffSpace * 2/3, (int) noteWidth / 3, (int)staffSpace / 3);
                if (subBottomPixel > noteCandidateRectArea * whiteNotePixRate) {
                    Mat subBottomCent = inputRoiMat.submat(subBottomCentRect);
                    for (int i = 0; i < subBottomCent.rows(); i++) {
                        for (int j = 0; j < subBottomCent.cols(); j++) {
                            if (subBottomCent.get(i, j)[0] == 255.0) {
                                subBottomCentPixel++;
                            }
                        }
                    }
                    if (subBottomCentPixel < subRectArea * subWhitePixRate) {
                        output = true;
                        headDetectRects[0] = subBottomRect;
                        isBlackNote = false;
                        isUpHead = false;
                        return output;
                    } else if (subBottomCentPixel > subRectArea * subBlackPixRate) {
                        if (subBottomPixel > noteCandidateRectArea * brackNotePixRate) {
                            output = true;
                            headDetectRects[0] = subBottomRect;
                            isBlackNote = true;
                            isUpHead = false;
                            return output;
                        }
                    }
                }
    
                bottomShift += shiftWidth;
                if (inputRoiMat.cols() - bottomShift - subBottom.cols() < shiftWidth) {
                    doneBottom = true;
                }
            }
        }else {
            System.out.println("is not one note");
        }
        System.out.println("is not note");
        return output;
        
        /*
        //ImageLoader loader = new ImageLoader("./pictures/wholeNote.jpg");
        ImageLoader loader = new ImageLoader("./pictures/wholeNote2.png");
        //Mat templateMat = new Mat();
        //test.matInfo(loader.getInputMat());
        Mat loadedMat = loader.getInputMat();
        Mat resizedMat = new Mat();
    
        ImageViewer viewer = new ImageViewer();
        viewer.show(inputRoiMat);
        System.out.println(lineSpace + ", " + inputRoiMat.rows() + ", " +  inputRoiMat.cols());
    
        Imgproc.resize(loadedMat, resizedMat, new Size(lineSpace, lineSpace));//loadedMat.size().width * 0.1, loadedMat.size().height * 0.1));
        ImageProcessor processor = new ImageProcessor();
        processor.grayScale(resizedMat);
        processor.binarize(processor.getGrayscaledMat());
        processor.invert(processor.getBinarizedMat());
        Mat templateMat = processor.getInvertedMat().clone();
        
        try {
            //Imgproc.matchTemplate(inputRoiMat, templateMat, whiteResultMat, Imgproc.TM_CCOEFF_NORMED);
            Imgproc.matchTemplate(inputRoiMat, templateMat, whiteResultMat, Imgproc.TM_CCORR_NORMED);
        }catch (CvException e) {
            throw new CvException("Failed to matchTemplate. Exception thrown: " + e);
        }
        
        Imgproc.cvtColor(inputRoiMat, inputRoiMat, Imgproc.COLOR_GRAY2BGR);
        Core.MinMaxLocResult maxr = Core.minMaxLoc(whiteResultMat);
        Point maxp = maxr.maxLoc;
        Point pt2 = new Point(maxp.x + templateMat.width(), maxp.y + templateMat.height());
        Imgproc.rectangle(inputRoiMat, maxp, pt2, new Scalar(0, 0, 255));
        ImageViewer viewer2 = new ImageViewer();
        viewer2.show(templateMat);
        Imgproc.putText(inputRoiMat, "Head", new Point(maxp.x, maxp.y + templateMat.height() + 20.0), Core.FONT_HERSHEY_COMPLEX, 0.7, new Scalar(0, 0, 255), 2);
        */
    }
    
    private boolean isOneNote(Mat roiMat) {
        boolean output = false;
        if (roiMat.cols() < staffSpace * oneNoteRate) {
            output = true;
        }
        return output;
    }
    
    
    private double recognizeKey(boolean isOneStave) {
        //String output = "";
        double key = 0;
        for (int i = 0; i < headDetectRects.length; i++) {
            System.out.println("recognize key " + (i+1));
        }
        //for (int i = 0; i < headDetectRects.length; i++) {
            //double centHeadX = headDetectRects[i].x + headDetectRects[i].width/2;
            double centHeadY = headDetectRects[0].y + headDetectRects[0].height/2;
            double stave5 = stavePos[4];
            if (!isOneStave) {
                //stave4 += staveSpace+staveWidth;
            }
            double minDist;
            double dist;
            int minNum;
            boolean decidedKey;
            /*if (centHeadY < stave5) {
                decidedKey = false;
                //while(!decidedKey) {
                    minDist = (stavePos[0] - centHeadY) * (stavePos[0] - centHeadY);
                    minNum = 0;
                    for (int j = 0; j < stavePos.length; i++) {
                        dist = (stavePos[j] - centHeadY) * (stavePos[j] - centHeadY);
                        if (minDist > dist) {
                            minDist = dist;
                            minNum = j;
                        }
                    }
                    if (minDist < notePitchPosThreshold) {
                        key = minNum;
                        //decidedKey = true;
                        break;
                    }else {
                        double cand;
                        if (minNum == 0) {
                            cand = stavePos[0];
                            for (int j = 0; j < maxAssistLine; j++) {
                                cand -= staffSpace/2;
                                key -= 0.5;
                                if ((stavePos[j] - centHeadY) * (stavePos[j] - centHeadY) < notePitchPosThreshold) {
                                    //decidedKey = true;
                                    return key;
                                }
                            }
                            
                        }else  {
                            
                        }
                    }
                //}
            }*/
            
            //for (int j = 0; j <= 7; i++) {
               /* if ((stave - centHeadY) * (stavePos[j] - centHeadY) < notePitchPosThreshold * notePitchPosThreshold) {
                    
                }*/
            //}
            
        //}
        /*dist = 0;
        minDist = (stavePos[0] - centHeadY) * (stavePos[0] - centHeadY);
        minNum = 0;
        for (int j = 0; j < stavePos.length; j++) {
            dist = (stavePos[j] - centHeadY) * (stavePos[j] - centHeadY);
            if (minDist > dist) {
                minDist = dist;
                minNum = j;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (minNum == i) {
                if (dist < notePitchPosThreshold * notePitchPosThreshold) {
                    
                }
            }
        }
        */
        
        if (num == 3) {
            key = 7;
        }else  if (num == 4) {
            key = 6.5;
        }else  if (num == 5) {
            key = 6;
        }else  if (num == 6) {
            key = 5.5;
        }else  if (num == 8) {
            key = 5;
        }else  if (num == 9) {
            key = 4.5;
        }else  if (num == 10) {
            key = 4;
        }else  if (num == 11) {
            key = 3.5;
        }
        
        return key;
    }
    
    private String keyToString(double key) {
        String output = "";
        double dif = 0.5;
        double tempKey = 7;
        //double temp = 0;
        double Oct = 6;
        String outputTemp[] = new String[7];
        outputTemp[0] = " C";
        outputTemp[1] = " D";
        outputTemp[2] = " E";
        outputTemp[3] = " F";
        outputTemp[4] = " G";
        outputTemp[5] = " A";
        outputTemp[6] = " B";
        
        boolean done = false;
        /*while (!done) {
            if (key == tempKey) {
                for (int i = 0; i < outputTemp.length; i++) {
                    int difTemp = (int)((7 - key) * 2)%7;
                    if (i == difTemp) {
                        output = outputTemp[i];
                        if (difTemp > 6) {
                            output += "6";
                        }
                    }
                }
                //if (tempKey)
                done = true;
            }else {
                done = false;
                temp++;
                tempKey -= dif;
            }
        }*/
        int temp = 0;
        /*for (double i = 7; i >= 3.5; i -= 0.5) {
            temp = 0;
            if (key == i) {
                output = outputTemp[temp];
                if (i == 4) {
                    output = outputTemp[0] + "6";
                }
            }
            temp++;
        }*/
        if (key == 7) {
            output = outputTemp[0];
        }else if (key == 6.5) {
            output = outputTemp[1];
        }else if (key == 6) {
            output = outputTemp[2];
        }else if (key == 5.5) {
            output = outputTemp[3];
        }else if (key == 5) {
            output = outputTemp[4];
        }else if (key == 4.5) {
            output = outputTemp[5];
        }else if (key == 4) {
            output = outputTemp[6];
        }else if (key == 3.5) {
            output = outputTemp[0]+"6";
        }
        return output;
    }
    
    private void detectHeadVisualize(Mat roiMat) {
        if (roiMat.channels() != 3) {
            Imgproc.cvtColor(roiMat, roiMat, Imgproc.COLOR_GRAY2BGR);
            //System.out.println("Gray to BGR");
        }
        Scalar color = new Scalar(0, 255, 0);
        Point p1, p2;
        for (int i = 0; i < headDetectRects.length; i++) {
            p1 = headDetectRects[i].tl();
            p2 = headDetectRects[i].br();
            Imgproc.rectangle(roiMat, p1, p2, color);
        }
    }
}
