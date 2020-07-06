/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chanct.charactervideo.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 文本转图片类
 *
 * @author YY2924 2014/11/18
 * @version 1.0
 */
public class JpgToAscaIIJpg {

    /**
     * 文本文件
     */
    private File textFile;
    /**
     * 图片文件
     */
    private File imageFile;

    /**
     * 图片
     */
    private BufferedImage image;
    /**
     * 图片宽度
     */
    private static final int IMAGE_WIDTH = 368;
    /**
     * 图片高度
     */
    private static final int IMAGE_HEIGHT = 640;
    /**
     * 图片类型
     */
    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

    /**
     * 构造函数
     *
     * @param textFile 文本文件
     * @param imageFile 图片文件
     */
    public JpgToAscaIIJpg() {
    }

    /**
     * 获取到图像上下文
     *
     * @param image 图片
     * @return Graphics
     */
    private static Graphics createGraphics(BufferedImage image, Integer imag_width, Integer imag_height) {
        Graphics g = image.createGraphics();
        g.setColor(Color.WHITE); //设置背景色
        g.fillRect(0, 0, imag_width, imag_height);//绘制背景
        g.setColor(Color.BLACK); //设置前景色
        g.setFont(new Font("宋体", Font.PLAIN, 12)); //设置字体
        return g;
    }

    public static void createAsciiPic(String jpgFileName, String ascaiiJpgFile, Integer imag_width, Integer imag_height) {
        //final String base = ".!@#$%^&*,?;1234567890ab ";// 字符串由复杂到简单
        final String base = "#%?!. ";// 字符串由复杂到简单
        try {
            //System.out.println("---jpgFileName:" + jpgFileName + "");
            if (new File(jpgFileName).length() == 0) {
                return;
            }
            File jpgfile = PictureCompress.compressPictureByQality(new File(jpgFileName), (float) 0.2);
            final BufferedImage image = ImageIO.read(jpgfile);

            //获取图像上下文
            BufferedImage result = new BufferedImage(imag_width, imag_height, IMAGE_TYPE);
            Graphics gp = createGraphics(result, imag_width, imag_height);
            String line;
            //图片中文本行高
            int lineNum = 1;

            for (int y = 0; y < image.getHeight(); y += 10) {
                StringBuilder str = new StringBuilder();
                for (int x = 0; x < image.getWidth(); x += 6) {
                    final int pixel = image.getRGB(x, y);
                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                    //final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    final float gray = (r + g + b) / 3;
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    str.append(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
                    //System.out.print(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
                }
//                if (lineNum == 1) {
//                    System.out.println("---line.length:" + str.length());
//                }
                //System.out.println("-----str:" + str.toString());
                gp.drawString(str.toString(), 0, lineNum * 10);
                lineNum++;
            }
            gp.dispose();

            File outPut = new File(ascaiiJpgFile);
            if (!outPut.exists()) {
                outPut.createNewFile();
            }

            String formatName = ascaiiJpgFile.substring(ascaiiJpgFile.lastIndexOf(".") + 1);
            ImageIO.write(result, formatName, outPut);

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String srcfile = "E:\\Work\\VideoEdit\\videopicture\\0.jpg";
        String dstfile = "E:\\Work\\VideoEdit\\text\\0.jpg";
        createAsciiPic(srcfile, dstfile, 544, 960);

//        final String base = "#%?!. ";// 字符串由复杂到简单
//        try {
//            File file = PictureCompress.compressPictureByQality(new File(srcfile), (float) 0.5);
//            final BufferedImage image = ImageIO.read(file);
//
//            //获取图像上下文
//            Graphics gp = createGraphics(this.image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_TYPE));
//            String line;
//            //图片中文本行高
//            int lineNum = 1;
//
//            for (int y = 0; y < image.getHeight(); y += 8) {
//                StringBuilder str = new StringBuilder();
//                for (int x = 0; x < image.getWidth(); x += 4) {
//                    final int pixel = image.getRGB(x, y);
//                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
//                    //final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
//                    final float gray = (r + g + b) / 3;
//                    final int index = Math.round(gray * (base.length() + 1) / 255);
//                    System.out.println("---y:" + y + "  x:" + x + " pixel:" + String.valueOf(pixel) + " gray:" + gray + " index:" + index);
//                    str.append(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
//                    //System.out.print(index >= base.length() ? " " : String.valueOf(base.charAt(index)));  
//                }
//                if (lineNum == 1) {
//                    System.out.println("---line.length:" + str.length());
//                }
//                System.out.println(str.toString());
//                //System.out.println();  
//                lineNum++;
//            }
//            System.out.println("lineNum:" + lineNum);
//            gp.dispose();
//
//            File outPut = new File(dstfile);
//            if (!outPut.exists()) {
//                outPut.createNewFile();
//            }
//
//            String formatName = dstfile.substring(dstfile.lastIndexOf(".") + 1);
//            ImageIO.write(image, formatName, outPut);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }

//        JpgToAscaIIJpg convert = new JpgToAscaIIJpg(new File(textFileName), new File(imageFileName));
//        boolean success = convert.convert();
//        System.out.println("文本转图片：" + (success ? "成功" : "失败"));
    }
}
