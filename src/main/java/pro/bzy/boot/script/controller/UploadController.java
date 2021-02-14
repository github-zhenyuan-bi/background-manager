package pro.bzy.boot.script.controller;

import java.awt.Image;
import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pro.bzy.boot.framework.config.yml.YmlBean;
import pro.bzy.boot.framework.utils.FileUtil;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.bean.R;

@Api(tags = {"上传"})
@ApiSupport(order = 100)
@Controller
@RequestMapping("upload")
public class UploadController extends MyAbstractController {

    
    @Autowired
    private YmlBean ymlBean;
    
    
    
    @ApiOperation(value="剧本封面图")
    @PostMapping("jubenCoverImg")
    public @ResponseBody R<Object> jubenCoverImg(MultipartFile img) throws Exception {
        return uploadJubenImg(img, ymlBean.getConfig().getImageServer().getJubenCoverImagePath(), true);
        //return uploadJubenImg(img, ScriptConstant.JUBEN_UPLOAD_COVER_IMG_PATH);
    }
    
    
    
    
    @ApiOperation(value="剧本人物图")
    @PostMapping("jubenCharacterImg")
    public @ResponseBody R<Object> jubenCharacterImg(MultipartFile img) throws Exception {
        return uploadJubenImg(img, ymlBean.getConfig().getImageServer().getJubenCharacterImagePath(), true);
        //return uploadJubenImg(img, ScriptConstant.JUBEN_UPLOAD_CHARACTER_IMG_PATH);
    }
    
    
    
    
    @ApiOperation(value="微信小程序轮播大图")
    @PostMapping("wxIndexSwipperImg")
    public void wxIndexSwipperImg(MultipartFile img, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        R<Object> uploadedImg = uploadJubenImg(img, ymlBean.getConfig().getImageServer().getWxMiniprogramIndexImagePath(), false);
        request.setAttribute("uploadedImg", uploadedImg.getData());
        request.getRequestDispatcher("/wx/wxMiniprogramSettingIndeximg/saveAfterUpload").forward(request, response);
        //return uploadJubenImg(img, ScriptConstant.JUBEN_UPLOAD_CHARACTER_IMG_PATH);
    }
    
    
    
    
    @ApiOperation(value="微信小程序服务模块icon")
    @PostMapping("wxMiniprogramServerIcon")
    public @ResponseBody R<Object> wxMiniprogramServerIcon(MultipartFile img) throws Exception {
        return uploadJubenImg(img, ymlBean.getConfig().getImageServer().getWxMiniprogramServerModuleIconPath(), true);
    }
    
    
    
    
    @ApiOperation(value="微信小程序合作模块icon")
    @PostMapping("wxMiniprogramCooperationIcon")
    public @ResponseBody R<Object> wxMiniprogramCooperationIcon(MultipartFile img) throws Exception {
        return uploadJubenImg(img, ymlBean.getConfig().getImageServer().getWxMiniprogramCooperationIconPath(), false);
    }
    
    
    
    
    @ApiOperation(value="通知公告图")
    @PostMapping("bulletinIconImg")
    public @ResponseBody R<Object> bulletinIconImg(MultipartFile img) throws Exception {
        return uploadJubenImg(img, ymlBean.getConfig().getImageServer().getJubenBulletinIconImagePath(), false);
    }
    
    
    
    private R<Object> uploadJubenImg(MultipartFile img, String uploadJubenImgPath, boolean thumbnail) throws Exception {
        File uploadedImg = null;
        String thumbnailImgName = null;
        String uploadJubenImgPath2 = null;
        try {
            // 1. 校验是否图片文件
            Image imgObj = FileUtil.checkImg(img);
            
            // 2. 获取剧本封面上传路径
            uploadJubenImgPath2 = ymlBean.getConfig().getImageServer().buildStoragePath(uploadJubenImgPath);
            //PathUtil.getWebappResourcePath(uploadJubenImgPath);
            
            // 3. 上传图片
            uploadedImg = FileUtil.uploadImg(img, uploadJubenImgPath2);
            String uploadedImgName = uploadedImg.getName();
            
            // 4. 添加水印
            //FileUtil.waterMarkForPic(uploadedImg);
            
            // 5. 生成原图的压缩图片
            if (thumbnail)
                thumbnailImgName = FileUtil.generateThumbnail2Directory(
                        uploadedImg, uploadJubenImgPath2, FileUtil.calculateImgScale(imgObj));
            
            // 6. 返回文件在项目中的相对地址
            Object canoPath = ymlBean.getConfig().getImageServer().buildServerPath(uploadJubenImgPath) + (thumbnail? thumbnailImgName : uploadedImgName);
            return R.ofSuccess(canoPath);
        } catch (Exception e) {
            // exp: 异常时将上传的文件删除
            if (uploadedImg != null && uploadedImg.exists()) 
                uploadedImg.delete();
            if (!StringUtils.isEmpty(thumbnailImgName) && !StringUtils.isEmpty(uploadJubenImgPath2)) {
                File thumbnailImg = new File(uploadJubenImgPath2 + thumbnailImgName);
                if (thumbnailImg.exists())
                    thumbnailImg.delete();
            }
            throw new Exception("上传图片失败，原因：" + e.getMessage());
        }
    }
}
