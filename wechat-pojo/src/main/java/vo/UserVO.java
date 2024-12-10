package vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVO {
    private String id;
    private String wechatNum;
    private String wechatNumImg;
    private String mobile;
    private String nickname;
    private String realName;
    private Integer sex;
    private String face;
    private String email;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate birthday;

    private String country;
    private String province;
    private String city;
    private String district;
    private String chatBg;
    private String friendCircleBg;
    private String signature;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
    private String userToken;  //用户会话令牌token 传递给前端 让前端保存处理
}
