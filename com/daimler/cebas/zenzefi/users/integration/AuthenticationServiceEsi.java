package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.integration;

import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationServiceEsi {
  ZenZefiUser backendLogin(HttpServletRequest paramHttpServletRequest);
  
  void revokeToken(Authentication paramAuthentication);
  
  void revokeToken(String paramString);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\integration\AuthenticationServiceEsi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */