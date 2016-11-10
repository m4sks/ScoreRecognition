package scoreRecognition;

import org.opencv.core.Mat;

/**
 * Created by ev50063 on 2016/11/10.
 */
public class TestClass {
    public void matInfo(Mat mat) {
        System.out.println(mat + ", rows:" + mat.rows() + ", cols:" + mat.cols() + ", elementsize:" + mat.elemSize());
    }
}
