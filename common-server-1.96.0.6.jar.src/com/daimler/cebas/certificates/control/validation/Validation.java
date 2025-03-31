package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.entity.Certificate;
import java.util.function.Predicate;

@FunctionalInterface
public interface Validation {
  Predicate<Certificate> getRule(Certificate paramCertificate);
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\Validation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */