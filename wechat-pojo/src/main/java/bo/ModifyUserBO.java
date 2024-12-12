package bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import java.time.LocalDate;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModifyUserBO {
    private String userId;

    private String face;
    private Integer sex;
    private String nickname;
    private String wechatNum;

    private String province;
    private String city;
    private String district;
    private String chatBg;
    private String friendCircleBg;
    private String signature;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @Email
    private String email;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate startWorkDate;
}
