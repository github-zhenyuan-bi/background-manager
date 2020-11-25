package pro.bzy.boot.framework.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.io.Files;

import lombok.NonNull;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import pro.bzy.boot.framework.utils.parents.MyUtil;

/**
 * 文件操作工具
 */
public class FileUtil implements MyUtil {

    /**
     * 读取文件成字符串
     * @param file 文件对象
     * @return
     * @throws IOException
     */
    public static String readString(File file) throws IOException {
        return Files.toString(file, Charset.forName(SystemConstant.CHARSET_UTF8));
    }
    
    
    
    /**
     * 上传图片
     * @param imgFile   要上传的图片文件
     * @param uploadPath    上传地址
     */
    public static File uploadImg(@NonNull MultipartFile imgFile, @NonNull String uploadPath)
            throws IllegalStateException, IOException {
        // 1. 处理上传图片的名称 防止重复 在名称尾部拼上唯一识别号
        String handledFileName = appendSuffix(imgFile);
        
        // 2. 上传目标文件 若该文件夹不存在 则创建
        File destFile = new File(uploadPath + handledFileName);
        
        // 3. 将上传文件数据输入到此目标文件中
        imgFile.transferTo(destFile);
        
        // 4. 返回目标文件
        return destFile;
    }
    
    
    
    /**
     * 验证文件是否为图片文件
     * @param imgFile 目标文件
     */
    public static Image checkImg(MultipartFile imgFile) throws IOException {
        try {
            Image img = ImageIO.read(imgFile.getInputStream());
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                throw new IOException("非图片文件");
            }
            return img;
        } catch (IOException e) {
            throw new IOException("非图片文件");
        }
    }
    
    
    
    /**
     * 在文件名称后面拼接一个指定的文本
     * @param fileName 文件名字符串
     * @param suffix 要拼接的字符串
     * @return
     */
    public static String appendSuffix(@NonNull String fileName, String suffix) {
        String nameWithoutExtension = Files.getNameWithoutExtension(fileName);
        String fileExtension = Files.getFileExtension(fileName);
        return nameWithoutExtension + suffix + StringPool.DOT +  fileExtension;
    }
    /**
     * 在文件名称后面拼接压缩文件指定的字符串
     * @param fileName 文件名字符串
     * @return
     */
    public static String appendSuffixThumbnail(String fileName) {
        return appendSuffix(fileName, SUFFIX);
    }
    /**
     * 在文件名称后面拼接压缩文件指定的字符串
     * @param fileName 文件
     * @return
     */
    public static String appendSuffix(MultipartFile file) {
        return appendSuffix(file.getOriginalFilename(), StringPool.DASH+UUID.randomUUID());
    }
    
    
    
    
    
    // 缩略图后缀
    public static final String SUFFIX = "-thumbnail";
    /**
     * 压缩文件
     * @param orgImgFile
     * @param uploadImgPath
     * @param scale
     * @return
     * @throws IOException
     */
    public static String generateThumbnail2Directory(File orgImgFile, final String uploadImgPath, Double scale) throws IOException {
        // 1. 压缩文件存放文件夹
        File tbFileDir = new File(uploadImgPath);
        if (!tbFileDir.exists())
            tbFileDir.mkdir();
        
        // 2. 按指定 比例 和指定 命名规则 生成压缩图片文件
        Thumbnails.of(orgImgFile)
                // 图片缩放率，不能和size()一起使用
                .scale(scale)
                // 缩略图保存目录,该目录需存在，否则报错
                .toFiles(tbFileDir, Rename.SUFFIX_HYPHEN_THUMBNAIL);
        
        // 3. 返回压缩后的文件名称
        return appendSuffixThumbnail(orgImgFile.getName());
    }
    /**
     * 压缩图片文件
     * @param orgImgFile
     * @param uploadImgPath
     * @return
     * @throws IOException
     */
    public static String generateThumbnail2Directory(File orgImgFile, final String uploadImgPath, Image imgObj) throws IOException {
        return generateThumbnail2Directory(orgImgFile, uploadImgPath, calculateImgScale(imgObj));
    }
    
    
    
    private static final Double   DEST_IMAGE_SIZE = 400000.0;
    /**
     * 计算压缩比例
     * @param orgImgFile
     * @return
     * @throws IOException
     */
    public static Double calculateImgScale(Image imgObj) throws IOException {
        int w = imgObj.getWidth(null);
        int h = imgObj.getHeight(null);
        double xs = w * h;
        if (xs < DEST_IMAGE_SIZE) 
            return 1.0;
        else {
            double v1 = xs / DEST_IMAGE_SIZE;
            double v2 = Math.sqrt(v1);
            return 1 / v2;
        }
    }
    
    
    
    
    
    //图片加水印
    public static File waterMarkForPic(File orgImgFile) {
        //水印内容
        String waterMarkContent = "图灵屋";
        try {
            //文件转化为图片
            Image srcImg = ImageIO.read(orgImgFile);
            //图片的宽/高
            int srcImgWidth = srcImg.getWidth(null)
                , srcImgHeight = srcImg.getHeight(null);
            
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufImg.createGraphics();
            graphics2D.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //颜色
            graphics2D.setColor(new Color(255, 255, 255)); 
            //字体
            graphics2D.setFont(new Font("Times New Roman", Font.PLAIN, 120));
            //透明
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f));
            //设置水印的坐标，这可以根据打印的内容大小和位置决定
            int x = srcImgWidth - 100;
            int y = srcImgHeight - 40;
            //画出水印
            graphics2D.drawString(waterMarkContent, x, y);  
            graphics2D.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(orgImgFile.getPath());
            ImageIO.write(bufImg, "jpg", outImgStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回加了谁演的图片路径
        return orgImgFile;
    }
    
    
    
    /**
     * 查询文件夹下的全部文件名
     * @param dictPath
     * @return
     */
    public static File[] getFilesNameUnderDict(@NonNull final String dictPath) {
        // 1. 映射路径为文件夹
        File dictFile = new File(dictPath);
        if (!dictFile.isDirectory())
            throw new IllegalArgumentException("参数给定的文件路径 非文件夹：" + dictPath);
        
        // 2. 文件夹下全部文件
        return dictFile.listFiles();
    }
    
    
    
    
    public static int removeNonUsedFiles(File[] allFiles, List<String> usedFileNames) {
        // 0. 无文件 不操作
        if (allFiles == null || allFiles.length == 0)
            return 0;
        
        // 1.1 如果不存在被使用的文件 则当前文件全删
        if (CollectionUtil.isEmpty(usedFileNames)) {
            Arrays.stream(allFiles).forEach(file -> file.deleteOnExit());
            return allFiles.length;
        }
        // 1.2 如果存在正在被使用的文件 则过滤出剩余未使用的 进行删除
        else {
            List<File> nonUsedFiles = Arrays.stream(allFiles).filter(file -> {
                return usedFileNames.stream().noneMatch(usedFileName -> {
                    return usedFileName.equals(file.getName()) || FileUtil.appendSuffixThumbnail(usedFileName).equals(file.getName());
                });
            }).collect(Collectors.toList());
            nonUsedFiles.forEach(nonUsedFile -> nonUsedFile.deleteOnExit());
            return nonUsedFiles.size();
        }
    }
}
