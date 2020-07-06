/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chanct.charactervideo.util;

/**
 *
 * @author lpb
 */
import java.awt.image.BufferedImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.imageio.ImageIO;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class GetVideoFromPicture {

    public static void main(String[] args) throws Exception {
//        System.out.println("start...");
//        String saveMp4name = "E:\\Work\\VideoEdit\\charvideo\\charvideo.mp4"; //保存的视频名称
//        // 目录中所有的图片，都是jpg的，以1.jpg,2.jpg的方式，方便操作
//        String imagesPath = "E:\\Work\\VideoEdit\\text\\"; // 图片集合的目录
//        makeVideo(saveMp4name, imagesPath);
//        System.out.println("end...");

        double result = new BigDecimal((float) 299000 / 10300 ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println("----result:" + result);
        
    }

    public static void makeVideo(String saveMp4name, String imagesPath) throws Exception {
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(saveMp4name, 640, 480);
//		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_FLV1); // 28
//		recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4); // 13
        recorder.setFormat("mp4");
        //	recorder.setFormat("mov,mp4,m4a,3gp,3g2,mj2,h264,ogg,MPEG4");
        recorder.setFrameRate(20);
        recorder.setPixelFormat(0); // yuv420p
        recorder.start();
        //
        OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
        // 列出目录中所有的图片，都是jpg的，以1.jpg,2.jpg的方式，方便操作
        File file = new File(imagesPath);
        File[] flist = file.listFiles();
        // 循环所有图片
        for (int i = 1; i <= flist.length; i++) {
            String fname = imagesPath + i + ".jpg";
            IplImage image = cvLoadImage(fname); // 非常吃内存！！
            recorder.record(conveter.convert(image));
            // 释放内存？ cvLoadImage(fname); // 非常吃内存！！
            opencv_core.cvReleaseImage(image);
        }
        recorder.stop();
        recorder.release();
    }

    /**
     * 图片合成视频
     *
     * @param mp4SavePath 视频保存路径bai
     * @param imageDir 图片地址
     * @param rate 这个可以理解成视频每秒播放图片的数量, 播放速度默认值
     * @param duration 这个可以理解成视频的播放时长，单位毫秒。
     */
    public static boolean jpgToMp4(String mp4SavePath, String imageDir, double rate, Integer width, Integer height, Long duration) {
        FFmpegFrameRecorder recorder = null;
        boolean flag = true;
        try {
            File[] files = fileSort(imageDir);
            int[] widthArray = new int[files.length];
            int[] heightArray = new int[files.length];

            /**
             * 获取合成视频图片的最大宽高,避免图片比例不一致最终合成效果差
             */
            for (int i = 0; i < files.length; i++) {
                BufferedImage bufferedImage = ImageIO.read(files[i]);
                widthArray[i] = bufferedImage.getWidth();
                heightArray[i] = bufferedImage.getHeight();
            }

            /**
             * 这个方法主要是防止图片比例达不到视频合成比例的要求,如果达不到下面条件视频则会无法播放 图片宽：必须要被32整除
             * 图片高：必须要被2整除
             */
            //int[] maxWH = getImgMaxWH(widthArray, heightArray);
            recorder = new FFmpegFrameRecorder(mp4SavePath, width, height);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            /**
             * 视频质量：目前测试出来的是25-30最清晰,视频质量范围好像是0-40,具体可以自己慢慢测
             */
            recorder.setVideoQuality(25);
            recorder.setFormat("mp4");
            //double result = new BigDecimal((float) files.length * 1000/duration ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            rate = duration > 0 ? (new BigDecimal((float) files.length * 1000/duration ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()):rate;
            System.out.println("-----rate:" + rate + ", files.length:" + files.length + ", duration:" + duration);
            recorder.setFrameRate(rate);
            recorder.setPixelFormat(0);
            recorder.start();

            OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();

            /**
             * 合成视频
             */
            for (int i = 0; i < files.length; i++) {
                opencv_core.IplImage image = cvLoadImage(files[i].getPath());
                recorder.record(conveter.convert(image));
                opencv_core.cvReleaseImage(image);
            }
            System.out.println("合成成功");
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            System.out.println("合成失败");
        } finally {
            try {
                if (recorder != null) {
                    recorder.stop();
                    recorder.release();
                }
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static File[] fileSort(String imageDir) {
        File file = new File(imageDir);
        File[] flist = file.listFiles();

        ArrayList<File> list = new ArrayList<>();
        for (File file1 : flist) {
            list.add(file1);
        }

        Collections.sort(list, new Comparator<File>() {
            public int compare(File file1, File file2) {
                Integer fileId1 = Integer.valueOf(file1.getName().substring(0, file1.getName().lastIndexOf(".")));
                Integer fileId2 = Integer.valueOf(file2.getName().substring(0, file2.getName().lastIndexOf(".")));

                if (fileId1 > fileId2) {
                    return 1;
                } else if (fileId1 == fileId2) {
                    return 0;
                }
                return -1;
            }
        });

        File[] result = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
