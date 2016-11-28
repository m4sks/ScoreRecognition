package scoreRecognition;

import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by ev50063 on 2016/10/25.
 */
public class ImageViewer {
    private JLabel imageView;
    
    public void show(Mat image){
        show(image, "");
    }
    
    public void show(Mat image, String windowName) {
        show(image, windowName, image.width() + 5, image.height() + 5);
    }
    
    public void show(Mat image, String windowName, int width, int height) {
        try {
            setSystemLookAndFeel();
            JFrame frame = createJFrame(windowName, width, height);
            Image loadedImage = toBufferedImage(image);
            imageView.setIcon(new ImageIcon(loadedImage));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            setPrint(image);
        }catch (Exception e) {
            System.out.println("Failed to show " + image.toString() + ": " + e);
        }
    }
    
    private JFrame createJFrame(String windowName, int width, int height) {
        JFrame frame = new JFrame(windowName);
        imageView = new JLabel();
        final JScrollPane imageScrollPane = new JScrollPane(imageView);
        imageScrollPane.setPreferredSize(new Dimension(width, height)/*(300, 480)/*(1280, 960)*//*(640, 480)*/);
        frame.add(imageScrollPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }
    
    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel
                    (UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    
    public Image toBufferedImage(Mat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( matrix.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
        byte [] buffer = new byte[bufferSize];
        matrix.get(0, 0, buffer); // get all the pixels
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
    
    private void setPrint(Mat image) {
        System.out.println("View - " + image);
    }
}