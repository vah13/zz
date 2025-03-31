package com.daimler.cebas.common.control.annotations;

import com.daimler.cebas.common.control.CEBASException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Lazy
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {CEBASException.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface CEBASService {}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\annotations\CEBASService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */