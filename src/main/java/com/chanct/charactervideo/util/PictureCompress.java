package com.chanct.charactervideo.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PictureCompress {
    private final static Logger logger = LoggerFactory.getLogger(PictureCompress.class);

    /**
     * 压缩图片
     * @param file
     * @param qality 参数qality是取值0~1范围内  代表压缩的程度
     * @return
     * @throws IOException
     */
    public static File compressPictureByQality(File file,float qality) throws IOException {
        BufferedImage src = null;
        FileOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;
        logger.info("开始设定压缩图片参数");
        // 指定写图片的方式为 jpg
        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
                null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality(qality);
        imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
        ColorModel colorModel =ImageIO.read(file).getColorModel();// ColorModel.getRGBdefault();
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
                colorModel, colorModel.createCompatibleSampleModel(32, 32)));
        logger.info("结束设定压缩图片参数");
        if (!file.exists()) {
             logger.info("Not Found Img File,文件不存在");
            throw new FileNotFoundException("Not Found Img File,文件不存在");
        } else {
            logger.info("图片转换前大小"+file.length()+"字节");
            src = ImageIO.read(file);
            out = new FileOutputStream(file);
            imgWrier.reset();
            // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
            // OutputStream构造
            imgWrier.setOutput(ImageIO.createImageOutputStream(out));
            // 调用write方法，就可以向输入流写图片
            imgWrier.write(null, new IIOImage(src, null, null),
                    imgWriteParams);
            out.flush();
            out.close();
            logger.info("图片转换后大小"+file.length()+"字节");
            return file;
        }
    }
}
