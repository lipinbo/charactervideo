/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chanct.charactervideo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import it.sauronsoftware.jave.FFMPEGLocator;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

/**
 *
 * @author lipinbo
 */
public class VideoEdeitUtils {
    //FFmpeg全路径

    private static final String FFMPEG_PATH = "D:\\ffmpeg\\bin\\ffmpeg.exe";
    //音频保存路径
    private static final String TMP_PATH = "E:\\Work\\VideoEdit\\video\\";

    /**
     * 从视频中提取音频信息
     *
     * @param videoUrl
     * @return
     */
    public static String videoToAudio(String videoUrl) {

        String aacFile = "";
        try {
            String ffmpegPath=  new File(videoUrl).getParentFile().getParent() + "\\lib\\" + "ffmpeg\\bin\\ffmpeg.exe";
            aacFile = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + ".m4a";
            String command = ffmpegPath + " -i " + videoUrl + " -vn -y -acodec copy " + aacFile;
            System.out.println("video to audio command : " + command);
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return aacFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        try {
            videoToAudio("E:\\Work\\VideoEdit\\video\\aaa.mp4");
            String videoInputPath = "E:\\Work\\VideoEdit\\charvideo\\aaa.mp4";
            String audioInputPath = "E:\\Work\\VideoEdit\\video\\aaa.m4a";
            String videoOutPath = "E:\\Work\\VideoEdit\\charvideo\\aaa2.mp4";
            combineVideoAndAudio(videoInputPath, audioInputPath, videoOutPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("---------获取音频文件成功！-----------");

    }

    /**
     * @param videoInputPath 原视频的全路径
     * @param audioInputPath 音频的全路径
     * @param videoOutPath 视频与音频结合之后的视频的路径
     * @throws Exception
     */
    public static void combineVideoAndAudio(String videoInputPath, String audioInputPath, String videoOutPath)
            throws Exception {
        Process process = null;
        try {
            String ffmpegPath=  new File(videoInputPath).getParentFile().getParent() + "\\lib\\" + "ffmpeg\\bin\\ffmpeg.exe";
            String command = ffmpegPath + " -i " + videoInputPath + " -i " + audioInputPath + " -vcodec copy -acodec copy -y "
                    + videoOutPath;
            System.out.println("video to audio  combine command:" + command);
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 使用这种方式会在瞬间大量消耗CPU和内存等系统资源，所以这里我们需要对流进行处理
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = br.readLine()) != null) {
        }
        if (br != null) {
            br.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }

    }

    public static void grabberVideoFramer(String videofile, String pictureSavePath) {
       
        //Frame对象
        Frame frame = null;
        //标识
        int flag = 0;
        /*
            获取视频文件
         */
        FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(videofile);

        try {
            fFmpegFrameGrabber.start();
            /*
            .getFrameRate()方法：获取视频文件信息,总帧数
             */
            int ftp = fFmpegFrameGrabber.getLengthInFrames();
//            System.out.println(fFmpegFrameGrabber.grabKeyFrame());
            System.out.println("时长 " + ftp / fFmpegFrameGrabber.getFrameRate() / 60);

            BufferedImage bImage = null;
            System.out.println("开始运行视频提取帧，耗时较长");

            while (flag <= ftp) {
                //文件绝对路径+名字
                String fileName = pictureSavePath + String.valueOf(flag) + ".jpg";
                //文件储存对象
                //System.out.println("---filename:" + fileName);
                File outPut = new File(fileName);
                if (!outPut.exists()) {
                    outPut.createNewFile();
                }
                //获取帧
                frame = fFmpegFrameGrabber.grabImage();
//                System.out.println(frame);
                if (frame != null) {
                    ImageIO.write(FrameToBufferedImage(frame), "jpg", outPut);
                }
                flag++;
            }
            System.out.println("============运行结束============");
            fFmpegFrameGrabber.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage FrameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

}
