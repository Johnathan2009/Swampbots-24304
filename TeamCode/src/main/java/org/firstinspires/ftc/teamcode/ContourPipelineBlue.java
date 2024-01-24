package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;



public class ContourPipelineBlue extends OpenCvPipeline {  //Our class.  Extends OpenCvPipeline
    Scalar BLUE = new Scalar(196, 0, 255);//Sets a YRB value for the color.  Since this is blue, it is 0 red 255 blue

    //                     Y      Cr     Cb    (Do not change Y)
    public static Scalar scalarLowerYCrCb = new Scalar(0.0, 150.0, 120.0);  //Creates a lower value for detection
    public static Scalar scalarUpperYCrCb = new Scalar(255.0, 255.0, 255.0);  //Creates a higher value for detection

    // Yellow, freight or ducks!
    //public static Scalar scalarLowerYCrCb = new Scalar(0.0, 100.0, 0.0);
    //public static Scalar scalarUpperYCrCb = new Scalar(255.0, 170.0, 120.0);

    // Green                                             Y      Cr     Cb
    // public static Scalar scalarLowerYCrCb = new Scalar(  0.0, 0.0, 0.0);
    // public static Scalar scalarUpperYCrCb = new Scalar(255.0, 120.0, 120.0);

    // Use this picture for you own color https://github.com/PinkToTheFuture/OpenCV_FreightFrenzy_2021-2022/blob/main/YCbCr.jpeg
    // Note that the Cr and Cb values range between 0-255. this means that the origin of the coordinate system is (128,128)

    // Volatile because accessed by OpMode without sync
    public volatile boolean error = false;  //Creates a value that is set to true when something goes wrong, and displays an error
    public volatile Exception debug;  //Creates an exception for errors

    private double borderLeftX;     //fraction of pixels from the left side of the cam to skip
    private double borderRightX;    //fraction of pixels from the right of the cam to skip
    private double borderTopY;      //fraction of pixels from the top of the cam to skip
    private double borderBottomY;   //fraction of pixels from the bottom of the cam to skip

    private int CAMERA_WIDTH;  // the width of the Camera
    private int CAMERA_HEIGHT;  // the length of the Camera

    private int loopCounter = 0;
    private int pLoopCounter = 0;

    private final Mat mat = new Mat();  //Creates a Matrix value
    private final Mat processed = new Mat();  //Creates another Matrix value

    private Rect maxRect = new Rect(600,1,1,1);  //Creates a Rect

    private double maxArea = 0;  //Sets the maximum area
    private boolean first = false;

    private final Object sync = new Object();  //Creates an Object

    public ContourPipelineBlue(double borderLeftX, double borderRightX, double borderTopY, double borderBottomY) {  //Creates a method called ContourPipelineBlue that ties directly to the class(forgot what it was called but I think it is a Constructor method) and grabs the boarder variables
        this.borderLeftX = borderLeftX;  //Sets the boarder in the method to the variable initialized at the start
        this.borderRightX = borderRightX;  //Sets the boarder in the method to the variable initialized at the start
        this.borderTopY = borderTopY;  //Sets the boarder in the method to the variable initialized at the start
        this.borderBottomY = borderBottomY;  //Sets the boarder in the method to the variable initialized at the start
    }
    public void configureScalarLower(double y, double cr, double cb) {  //Creates a method that configures the lower values
        scalarLowerYCrCb = new Scalar(y, cr, cb);  //creates a scalar that grabs the lower values initialized from the method
    }
    public void configureScalarUpper(double y, double cr, double cb) {  //Creates a method that configures the upper values
        scalarUpperYCrCb = new Scalar(y, cr, cb);  //creates a scalar that grabs the upper values initialized from the method
    }
    public void configureScalarLower(int y, int cr, int cb) {  //Grabs int values instead of double values
        scalarLowerYCrCb = new Scalar(y, cr, cb);  //Grabs int values instead of double values
    }
    public void configureScalarUpper(int y, int cr, int cb) {  //Grabs int values instead of double values
        scalarUpperYCrCb = new Scalar(y, cr, cb);  //Grabs int values instead of double values
    }
    public void configureBorders(double borderLeftX, double borderRightX, double borderTopY, double borderBottomY) {  //Creates a method that configures the boarders
        this.borderLeftX = borderLeftX;  //Configures a boarder
        this.borderRightX = borderRightX;  //Configures a boarder
        this.borderTopY = borderTopY;  //Configures a boarder
        this.borderBottomY = borderBottomY;  //Configures a boarder
    }

    @Override  //Overrides certain code
    public Mat processFrame(Mat input) {  //Creates a public Matrix method that awaits a Matrix input.
        CAMERA_WIDTH = input.width();  //Grabs the inputs width and sets it to the Camera Width
        CAMERA_HEIGHT = input.height();  //Grabs the inputs height and sets it to the Camera Height
        try {  //Creates a try method to try and catch certain errors
            // Process Image
            Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2YCrCb);
            Core.inRange(mat, scalarLowerYCrCb, scalarUpperYCrCb, processed);
            // Remove Noise
            Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_OPEN, new Mat());
            Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, new Mat());
            // GaussianBlur
            Imgproc.GaussianBlur(processed, processed, new Size(5.0, 15.0), 0.00);
            // Find Contours
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(processed, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            // Draw Contours
            Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0));

            // Lock this up to prevent errors when outside threads access the max rect property.
            synchronized (sync) {
                // Loop Through Contours
                for (MatOfPoint contour : contours) {
                    Point[] contourArray = contour.toArray();

                    // Bound Rectangle if Contour is Large Enough
                    if (contourArray.length >= 15) {
                        MatOfPoint2f areaPoints = new MatOfPoint2f(contourArray);
                        Rect rect = Imgproc.boundingRect(areaPoints);

                        if (                        rect.area() > maxArea
                                && rect.x + (rect.width / 2.0)  > (borderLeftX * CAMERA_WIDTH)
                                && rect.x + (rect.width / 2.0)  < CAMERA_WIDTH - (borderRightX * CAMERA_WIDTH)
                                && rect.y + (rect.height / 2.0) > (borderTopY * CAMERA_HEIGHT)
                                && rect.y + (rect.height / 2.0) < CAMERA_HEIGHT - (borderBottomY * CAMERA_HEIGHT)

                                || loopCounter - pLoopCounter   > 6
                                && rect.x + (rect.width / 2.0)  > (borderLeftX * CAMERA_WIDTH)
                                && rect.x + (rect.width / 2.0)  < CAMERA_WIDTH - (borderRightX * CAMERA_WIDTH)
                                && rect.y + (rect.height / 2.0) > (borderTopY * CAMERA_HEIGHT)
                                && rect.y + (rect.height / 2.0) < CAMERA_HEIGHT - (borderBottomY * CAMERA_HEIGHT)
                        ){
                            maxArea = rect.area();
                            maxRect = rect;
                            pLoopCounter++;
                            loopCounter = pLoopCounter;
                            first = true;
                        }
                        else if(loopCounter - pLoopCounter > 10){
                            maxArea = new Rect().area();
                            maxRect = new Rect();
                        }

                        areaPoints.release();
                    }
                    contour.release();
                }
                if (contours.isEmpty()) {
                    maxRect = new Rect(600,1,1,1);
                }
            }
            // Draw Rectangles If Area Is At Least 500
            if (first && maxRect.area() > 500) {
                Imgproc.rectangle(input, maxRect, new Scalar(0, 255, 0), 2);
            }
            // Draw Borders
            Imgproc.rectangle(input, new Rect(
                    (int) (borderLeftX * CAMERA_WIDTH),
                    (int) (borderTopY * CAMERA_HEIGHT),
                    (int) (CAMERA_WIDTH - (borderRightX * CAMERA_WIDTH) - (borderLeftX * CAMERA_WIDTH)),
                    (int) (CAMERA_HEIGHT - (borderBottomY * CAMERA_HEIGHT) - (borderTopY * CAMERA_HEIGHT))
            ), BLUE, 2);

            // Display Data
            Imgproc.putText(input, "Area: " + getRectArea() + " Midpoint: " + getRectMidpointXY().x + " , " + getRectMidpointXY().y, new Point(5, CAMERA_HEIGHT - 5), 0, 0.6, new Scalar(255, 255, 255), 2);

            loopCounter++;
        } catch (Exception e) {
            debug = e;
            error = true;
        }
        return input;
    }
    /*
    Synchronize these operations as the user code could be incorrect otherwise, i.e a property is read
    while the same rectangle is being processed in the pipeline, leading to some values being not
    synced.
     */

    public int getRectHeight() {
        synchronized (sync) {
            return maxRect.height;
        }
    }
    public int getRectWidth() {
        synchronized (sync) {
            return maxRect.width;
        }
    }
    public int getRectX() {
        synchronized (sync) {
            return maxRect.x;
        }
    }
    public int getRectY() {
        synchronized (sync) {
            return maxRect.y;
        }
    }
    public double getRectMidpointX() {
        synchronized (sync) {
            return getRectX() + (getRectWidth() / 2.0);
        }
    }
    public double getRectMidpointY() {
        synchronized (sync) {
            return getRectY() + (getRectHeight() / 2.0);
        }
    }
    public Point getRectMidpointXY() {
        synchronized (sync) {
            return new Point(getRectMidpointX(), getRectMidpointY());
        }
    }
    public double getAspectRatio() {
        synchronized (sync) {
            return getRectArea() / (CAMERA_HEIGHT * CAMERA_WIDTH);
        }
    }
    public double getRectArea() {
        synchronized (sync) {
            return maxRect.area();
        }
    }
}
