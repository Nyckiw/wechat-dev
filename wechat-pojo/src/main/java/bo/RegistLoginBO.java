package bo;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegistLoginBO {
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号长度不规范")
    private String mobile;
    @NotBlank(message = "验证码不能为空")
    private String smsCode;
    private String nickname;
}
