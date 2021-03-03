package pro.bzy.boot.framework.utils;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.io.Files;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import pro.bzy.boot.framework.config.exceptions.MyFileUploadException;
import pro.bzy.boot.framework.config.exceptions.MyNotImageFileException;
import pro.bzy.boot.framework.utils.parents.MyUtil;


/**
 * 图片上传组件
 * @author user
 *
 */
@Slf4j
public final class ImageUploaderUtil implements MyUtil {

    
    
    /**
     * 上传文件
     * @param imgFile 图片文件
     * @param uploadPath 上传路径
     * @return
     */
    public static File upload(MultipartFile imgFile, String uploadPath, long limitSize) {
        File destFile;
        try {
            // 1. 校验
            checkType(imgFile);
            limitImageSize(imgFile, limitSize);
            
            // 2. 上传目标文件 若该文件夹不存在 则创建
            destFile = new File(uploadPath + buildUniqueFileName(imgFile));
            File pFile = destFile.getParentFile();
            if (!pFile.exists())
                pFile.mkdirs();
            
            // 3. 将上传文件数据输入到此目标文件中
            imgFile.transferTo(destFile);
        } catch (Exception e) {
            throw new MyFileUploadException("图片上传异常, 原因：" + e.getMessage());
        }
        // 4. 返回目标文件
        return destFile;
    }

    
    
    
    /**
     * 生成源文件对应的压缩文件
     * @param orgFile 源文件
     * @param scale 压缩比例
     * @return
     */
    public static File genThumbnailFile(File orgFile) {
        return genThumbnailFile(orgFile, getImgScale(orgFile));
    }
    
    
    
    
    /**
     * 生成源文件对应的压缩文件
     * @param orgFile 源文件
     * @param scale 压缩比例
     * @return
     */
    public static File genThumbnailFile(File orgFile, double scale) {
        try {
            // 1. 按指定 比例 和指定 命名规则 生成压缩图片文件
            Thumbnails.of(orgFile)
                    // 图片缩放率，不能和size()一起使用
                    .scale(scale)
                    // 缩略图保存目录,该目录需存在，否则报错
                    .toFiles(orgFile.getParentFile(), Rename.SUFFIX_HYPHEN_THUMBNAIL);
            
            // 3. 返回压缩后的文件名称
            return new File(buildUniqueFileName(orgFile.getName(), "-thumbnail"));
        } catch (Exception e) {
            throw new MyFileUploadException("图片压缩异常：" + e.getMessage());
        }
    }
    
    
    
    /**
     * 目标图片尺寸大小 40w像素值的图片大小在 50-70kb上下
     */
    private static final double DEST_IMAGE_SIZE = 400000.0;
    /**
     * 计算图片的压缩比例 <br>
     * 若像素值已经小于40w则不做处理<br>
     * 否则计算图片压缩至40w像素
     * @param orgFile
     * @return
     * @throws IOException
     */
    private static double getImgScale(File orgFile) {
        try {
            Image imgObj = ImageIO.read(orgFile);
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
        } catch (Exception e) {
            log.error("计算图片压缩比例异常, 原因：" + e.getMessage());
            return 1.0;
        }
    }
    
    
    
    /**
     * 图片文件类型校验
     * @param imgFile
     */
    public static void checkType(MultipartFile imgFile) {
        String fileName = imgFile.getName();
        try {
            Image img = ImageIO.read(imgFile.getInputStream());
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) 
                throw new MyNotImageFileException();
        } catch (MyNotImageFileException e) {
            throw new MyNotImageFileException("解析上传图片文件异常：【非图片文件】:" + fileName);
        } catch (Exception e) {
            throw new MyNotImageFileException("解析上传图片文件异常：" + e.getMessage());
        }
    }
    
    
    
    /**
     * 限制图片大小
     * @param imgFile 图片文件
     * @param size 限制尺寸
     */
    public static void limitImageSize(MultipartFile imgFile, long size) {
        if (size == -1) return;
        
        long imgSize = imgFile.getSize()/1024;
        if (imgSize > size) 
            throw new MyFileUploadException("图片大小异常，该类型图片被限制上传为：" + size + " KB, 图片实际大小为：" + imgSize + " KB");
    }
    
    
    
    
    /**
     * 构建唯一的文件名
     * @param imgFile
     * @return
     */
    public static String buildUniqueFileName(MultipartFile imgFile) {
        return buildUniqueFileName(imgFile, null);
    }
    
    
    
    /**
     * 构建唯一的文件名
     * @param imgFile
     * @return
     */
    public static String buildUniqueFileName(MultipartFile imgFile, String fileNameSuffix) {
        String fileOrgName   = imgFile.getOriginalFilename();
        return buildUniqueFileName(fileOrgName, fileNameSuffix);
    }
    
    
    
    
    /**
     * 构建唯一的文件名
     * @param imgFile
     * @return
     */
    public static String buildUniqueFileName(@NonNull String fileName, String fileNameSuffix) {
        return new StringBuilder(32)
                .append(Files.getNameWithoutExtension(fileName))
                .append(StringUtils.isEmpty(fileNameSuffix)? getFileNameSuffix() : fileNameSuffix)
                .append(StringPool.DOT)
                .append(Files.getFileExtension(fileName))
                        .toString();
    }
    
    
    
    
    /**
     * 构建文件尾部唯一识别号
     * @return
     */
    public static String getFileNameSuffix() {
        return StringPool.DASH + UUID.randomUUID();
    }
}
