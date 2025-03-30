package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.validation;

import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.zenzefi.users.control.validation.IUserValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.annotation.RequestScope;

@CEBASControl
@RequestScope
@Profile({"AFTERSALES"})
public class XentryUserValidator implements IUserValidator {
  public void validateUser(User user) {}
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\validation\XentryUserValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */