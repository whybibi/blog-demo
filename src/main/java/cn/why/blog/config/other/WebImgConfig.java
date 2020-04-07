package cn.why.blog.config.other;

import cn.why.blog.utils.PathUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置图片虚拟访问地址，直接在/image/后加上数据库中地址即可，不需要绝对地址
 */
@Configuration
public class WebImgConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:"+ PathUtils.getImgBasePath());
    }

}
