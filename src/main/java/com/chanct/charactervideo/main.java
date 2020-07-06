/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chanct.charactervideo;

import com.chanct.charactervideo.util.JpgToAscaIIJpg;
import com.chanct.charactervideo.util.GetVideoFromPicture;
import com.chanct.charactervideo.util.VideoEdeitUtils;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import java.io.File;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

/**
 *
 * @author lpb
 */
public class main {

    public static void main(String[] args) {
        try {
            //视频文件路径
            String videofile = "E:\\Work\\VideoEdit\\VedioTest_git\\data\\aaa.mp4";

            //视频帧图片存储路径
            String pictureSavePath = new File(videofile).getParent() + "\\" + "videopicture" + "\\";
            if (!new File(pictureSavePath).exists()) {
                new File(pictureSavePath).mkdir();
            }
            String ascaiiPictureSavePath = new File(videofile).getParent() + "\\" + "text" + "\\";
            if (!new File(ascaiiPictureSavePath).exists()) {
                new File(ascaiiPictureSavePath).mkdir();
            }
            String tmpVideofile = new File(videofile).getParent() + "\\" + "tmp.mp4";
            
//            String pictureSavePath = "E:\\Work\\VideoEdit\\videopicture\\";
//            String ascaiiPictureSavePath = "E:\\Work\\VideoEdit\\text\\";
//            String dstVideofile = "E:\\Work\\VideoEdit\\charvideo\\tmp.mp4";

            File source = new File(videofile);
            Encoder encoder = new Encoder();
            MultimediaInfo m = encoder.getInfo(source);
            long ls = m.getDuration();
            System.out.println("此视频时长为:" + ls / 60000 + "分" + (ls) / 1000 + "秒！");
            System.out.println("此视频时长为:" + ls);
            //视频帧宽高
            System.out.println("此视频高度为:" + m.getVideo().getSize().getHeight());
            System.out.println("此视频宽度为:" + m.getVideo().getSize().getWidth());
            System.out.println("此视频格式为:" + m.getFormat());
            System.out.println("此视频播放速度:" + m.getVideo().getFrameRate());

            System.out.println("debug1");
            VideoEdeitUtils.grabberVideoFramer(videofile, pictureSavePath);

            System.out.println("debug2");
            convertToAsciiJpg(pictureSavePath, ascaiiPictureSavePath, m.getVideo().getSize().getWidth(), m.getVideo().getSize().getHeight());

            System.out.println("debug3");
            GetVideoFromPicture.jpgToMp4(tmpVideofile, ascaiiPictureSavePath, 29.03, m.getVideo().getSize().getWidth(), m.getVideo().getSize().getHeight(), m.getDuration());

            System.out.println("debug4");
            String audioPath = VideoEdeitUtils.videoToAudio(videofile);
            System.out.println("---audioPath:" + audioPath);
            String videoOutput = new File(videofile).getPath().replaceAll(".mp4", "_ASCII.mp4");
            System.out.println("----videoOutput:" + videoOutput);
            VideoEdeitUtils.combineVideoAndAudio(tmpVideofile, audioPath, videoOutput);

            System.out.println("debug5");
            try {
                new File(audioPath).delete();
                new File(tmpVideofile).delete();
                emptyDir(pictureSavePath);
                emptyDir(ascaiiPictureSavePath);
                System.out.println("---delete tmp file ");
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("run over!!!");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void convertToAsciiJpg(String srcPath, String dstPath, Integer imag_width, Integer imag_height) {
        JpgToAscaIIJpg jpgToAscaIIJpg = new JpgToAscaIIJpg();
        File file = new File(srcPath);
        File[] flist = file.listFiles();
        //System.out.println("---file numbers:" + flist.length);
        // 循环所有图片
        for (int i = 0; i <= flist.length - 2; i++) {
            String srcfile = srcPath + i + ".jpg";
            //System.out.println("---process " + srcfile);
            String dstfile = dstPath + i + ".jpg";
            jpgToAscaIIJpg.createAsciiPic(srcfile, dstfile, imag_width, imag_height);
        }
    }

    public static void emptyDir(String dirpath) {
        File dir = new File(dirpath);
        if (dir.listFiles().length > 0) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
        }
        dir.delete();
    }

}
