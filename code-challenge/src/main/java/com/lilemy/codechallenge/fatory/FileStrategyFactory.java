package com.lilemy.codechallenge.fatory;

import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.exception.BusinessException;
import com.lilemy.codechallenge.strategy.FileStrategy;
import com.lilemy.codechallenge.strategy.impl.CosFileStrategy;
import com.lilemy.codechallenge.strategy.impl.MinioFileStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工厂模式：
 * 根据配置文件中的参数加载对应的 Bean
 */
@Configuration
public class FileStrategyFactory {

    @Value("${storage.file.type}")
    private String fileStrategyType = "minio";

    @Bean
    public FileStrategy getFileStrategy() {
        if (StringUtils.equals(fileStrategyType, "minio")) {
            return new MinioFileStrategy();
        } else if (StringUtils.equals(fileStrategyType, "cos")) {
            return new CosFileStrategy();
        }
        throw new BusinessException(ResultCode.PARAMS_ERROR, "不可用的存储类型");
    }
}
