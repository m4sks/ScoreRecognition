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
    
    private String Cis = "Cis";
    private String Dis = "Dis";
    private String Eis = "Eis";
    private String Fis = "Fis";
    private String Gis = "Gis";
    private String Ais = "Ais";
    private String His = "His";
    
    private String Ces = "Ces";
    private String Des = "Des";
    private String Es = "Es";
    private String Ges = "Ges";
    private String As = "As";
    private String B = "B";
    */
    
    private String Whole = "Whole-note";
    private String Half = "Half-note";
    private String Quater = "Quater-note";
    private String Eighth = "8th-note";
    private String Sixteenth = "16th-note";
    
    private Mat whiteResultMat;
    private Mat blackResultMat;
    
    NotesRecognizer() {
        
    }
    
    private boolean isWhiteNote(Mat inputRoiMat) {
        TestClass test = new TestClass();
        boolean output = true;
        whiteResultMat = new Mat();
        //ImageLoader loader = new ImageLoader("./pictures/wholeNote.jpg");
        ImageLoader loader = new ImageLoader("./pictures/wholeNote2.png");
        //Mat templateMat = new Mat();
        test.matInfo(loader.getInputMat());
        Mat loaderedMat = loader.getInputMat();
        Mat resizedMat = new Mat();
        Imgproc.resize(loaderedMat, resizedMat, new Size(loaderedMat.size().width * 0.25, loaderedMat.size().height * 0.25));
        ImageProcessor processor = new ImageProcessor();
        processor.grayScale(resizedMat);
        processor.binarize(processor.getGrayscaledMat());
        processor.invert(processor.getBinarizedMat());
        Mat templateMat = processor.getInvertedMat().clone();
        
        test.matInfo(inputRoiMat);
        test.matInfo(templateMat);
        try {
            Imgproc.matchTemplate(inputRoiMat, templateMat, whiteResultMat, Imgproc.TM_CCOEFF_NORMED);
            //Imgproc.matchTemplate(inputRoiMat, templateMat, whiteResultMat, Imgproc.TM_CCORR_NORMED);
        }catch (CvException e) {
            throw new CvException("Failed to matchTemplate. Exception thrown: " + e);
        }
        if (whiteResultMat == new Mat()) {
            output = false;
        }else {
            Imgproc.cvtColor(inputRoiMat, inputRoiMat, Imgproc.COLOR_GRAY2BGR);
            Core.MinMaxLocResult maxr = Core.minMaxLoc(whiteResultMat);
            Point maxp = maxr.maxLoc;
            Point pt2 = new Point(maxp.x + templateMat.width(), maxp.y + templateMat.height());
            Imgproc.rectangle(inputRoiMat, maxp, pt2, new Scalar(0, 0, 255));
            //ImageViewer viewer = new ImageViewer();
            //viewer.show(templateMat);
            Imgproc.putText(inputRoiMat, "White Note", new Point(maxp.x, maxp.y + templateMat.height() + 20.0), Core.FONT_HERSHEY_COMPLEX, 0.7, new Scalar(255, 255, 255), 2);
        }
        return output;
    }
    
    private boolean isBlackNote(Mat roiMat) {
        boolean output = true;
        return output;
    }
    
    public String recognizeNotes(Mat roiMat) {
        String output = "";
        if (isWhiteNote(roiMat)) {
            output = "White Note";
        }else if (isBlackNote(roiMat)) {
            output = "BlackNote";
        }
        return output;
    }
    
    public String recognizeKey() {
        String output = "";
        return output;
    }
}
