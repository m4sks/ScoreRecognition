package scoreRecognition;

import org.opencv.core.Mat;

/**
 * Created by ev50063 on 2016/11/10.
 */
public class TestClass {
    public void matInfo(Mat mat) {
        System.out.println("Test - matInfo" + ": " + mat + ", rows:" + mat.rows() + ", cols:" + mat.cols() + ", elementsize:" + mat.elemSize());
    
    }
    
    public void matInfo(Mat mat, String matName) {
        System.out.println("Test - matInfo."+ matName + ": " + mat + ", rows:" + mat.rows() + ", cols:" + mat.cols() + ", elementsize:" + mat.elemSize());
    }
    
    public void mat(Mat mat) {
        System.out.println("Test - mat.");
        for (int i = 0; i < mat.cols(); i++) {
            for (int j = 0; j < mat.rows(); j++) {
                System.out.print(mat.get(j, i)[0] + ", ");
            }
            System.out.println("");
        }
    }
    
    public void mat(Mat mat, int num) {
        System.out.println("Test - mat.");
        for (int i = 0; i < mat.cols(); i++) {
            for (int j = 0; j < mat.rows(); j++) {
                System.out.print(mat.get(j, i)[0] + ", ");
                if ((j+1) % num == 0) System.out.println("");
            }
            System.out.println("");
        }
    }
    
    public void matChannel(Mat mat, int channel) {
        System.out.println("Test - matChannel");
        for (int i = 0; i < mat.rows(); i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < channel; j++) {
                System.out.print(mat.get(i, 0)[j] + ", ");
            }
            System.out.println("");
        }
    }
}
