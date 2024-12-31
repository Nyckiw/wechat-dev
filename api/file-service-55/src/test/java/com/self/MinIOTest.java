package com.self;

import com.self.config.MinIOUtils;
import io.minio.messages.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/31
 */
@SpringBootTest
public class MinIOTest {
    @Test
    public void test() throws Exception {
        List<Bucket> allBuckets = MinIOUtils.getAllBuckets();
        System.out.println(allBuckets.toString());
    }
}
