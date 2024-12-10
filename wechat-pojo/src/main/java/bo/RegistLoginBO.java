package bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String mobile;
    private String smsCode;
    private String nickname;
}
