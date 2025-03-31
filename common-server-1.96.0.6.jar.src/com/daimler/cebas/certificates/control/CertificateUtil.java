/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.PKIRole;
/*     */ import com.daimler.cebas.certificates.entity.UserRole;
/*     */ import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import java.sql.Timestamp;
/*     */ import java.time.LocalDateTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CertificateUtil
/*     */ {
/*     */   public static String getCommaSeparatedStringFromList(List<String> values) {
/*  43 */     if (values == null) {
/*  44 */       return "";
/*     */     }
/*  46 */     return String.join(", ", (Iterable)values);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isListEmpty(List<String> values) {
/*  57 */     if (values == null || values.isEmpty()) {
/*  58 */       return true;
/*     */     }
/*  60 */     for (String string : values) {
/*  61 */       if (!StringUtils.isEmpty(string)) {
/*  62 */         return false;
/*     */       }
/*     */     } 
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCertificateMatchingEnhancedRightsPermission(Certificate certificate, EnhancedRightsPermission service) {
/*  79 */     List<String> services = (service.getServiceIds() != null) ? (List<String>)service.getServiceIds().stream().map(s -> s.replace("0x", "")).collect(Collectors.toList()) : Collections.<String>emptyList();
/*     */     
/*  81 */     return (certificate.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && 
/*  82 */       isEqualsIgnoreCaseCommaSeparatedListAndList(certificate.getServices(), services) && 
/*  83 */       isEqualsCommaSeparatedListAndList(certificate.getTargetECU(), service.getTargetECU()) && 
/*  84 */       isEqualsCommaSeparatedListAndList(certificate.getTargetVIN(), service.getTargetVIN()));
/*     */   }
/*     */   
/*     */   public static String getSortedCommaSeparatedList(List<String> someList) {
/*  88 */     if (someList == null) {
/*  89 */       return "";
/*     */     }
/*     */ 
/*     */     
/*  93 */     List<String> listToSort = new ArrayList<>(someList);
/*  94 */     Collections.sort(listToSort);
/*  95 */     return getCommaSeparatedStringFromList(listToSort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCertificateMatchingPermission(Certificate certificate, Permission permission) {
/* 108 */     String userRoleFromPermission = getUserRoleFromPermission(permission.getUserRole());
/* 109 */     String pkiRoleText = getPKIRoleTextFromPermission(permission.getPkiRole());
/*     */     
/* 111 */     return (StringUtils.equals(certificate.getUserRole(), userRoleFromPermission) && StringUtils.equals(certificate.getPKIRole(), pkiRoleText) && 
/* 112 */       isEqualsCommaSeparatedListAndList(certificate.getTargetECU(), permission.getTargetECU()) && 
/* 113 */       isEqualsCommaSeparatedListAndList(certificate.getTargetVIN(), permission.getTargetVIN()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean compareCommaSeparatedLists(String first, String second) {
/* 124 */     List<String> firstList = asList(first, ",");
/* 125 */     Collections.sort(firstList);
/*     */     
/* 127 */     List<String> secondList = asList(second, ",");
/* 128 */     Collections.sort(secondList);
/*     */     
/* 130 */     return firstList.equals(secondList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean includesTargetECUsAndTargetVINs(Certificate certificate, List<String> targetECUs, List<String> targetVINs) {
/* 145 */     List<String> ecus = (targetECUs != null) ? targetECUs : Collections.<String>emptyList();
/* 146 */     List<String> vins = (targetVINs != null) ? targetVINs : Collections.<String>emptyList();
/*     */     
/* 148 */     List<String> certificateTargetECUs = asList(certificate.getTargetECU(), ",");
/* 149 */     List<String> certificateTargetVINs = asList(certificate.getTargetVIN(), ",");
/*     */     
/* 151 */     return (certificateTargetECUs.containsAll(ecus) && certificateTargetVINs.containsAll(vins));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPattern(List<String> input) {
/* 161 */     StringBuilder pattern = new StringBuilder();
/* 162 */     for (String value : input) {
/* 163 */       pattern.append("(?=.*").append(Pattern.quote(value)).append(".*)");
/*     */     }
/* 165 */     return StringUtils.isEmpty(pattern.toString()) ? ".*" : pattern.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getServicesFromEnhRightsPermission(List<String> services) {
/* 175 */     if (services == null) {
/* 176 */       return "";
/*     */     }
/*     */     
/* 179 */     List<String> fixedList = (List<String>)services.stream().map(service -> service.replace("0x", "")).collect(Collectors.toList());
/* 180 */     return String.join(", ", (Iterable)fixedList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPKIRoleNameFromPermission(String byteValue) {
/* 191 */     int pkiRole = Integer.decode(byteValue).intValue();
/* 192 */     return ((CertificateType)PKIRole.getRoles().get(Integer.valueOf(pkiRole))).name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPKIRoleTextFromPermission(String byteValue) {
/* 203 */     int pkiRole = Integer.decode(byteValue).intValue();
/* 204 */     return ((CertificateType)PKIRole.getRoles().get(Integer.valueOf(pkiRole))).getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getUserRoleFromPermission(String byteValue) {
/* 215 */     if (!StringUtils.isBlank(byteValue)) {
/* 216 */       UserRole userRole = UserRole.getUserRoleFromByte(Byte.decode(byteValue).byteValue());
/* 217 */       return userRole.getText();
/*     */     } 
/* 219 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCertificateValidityExceedingMinimumRenewal(Certificate certificate, String renewal) {
/* 232 */     DurationParser renewalPeriod = DurationParser.parse(renewal);
/* 233 */     LocalDateTime renewalTime = renewalPeriod.plusTo(LocalDateTime.now());
/*     */     
/* 235 */     return (certificate.getValidTo().getTime() <= Timestamp.valueOf(renewalTime).getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> asList(String str, String separator) {
/* 248 */     if (StringUtils.isEmpty(str)) {
/* 249 */       return new ArrayList<>();
/*     */     }
/* 251 */     return Arrays.asList(str.split("\\s*" + separator + "\\s*"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isEqualsCommaSeparatedListAndList(String commaSeparatedString, List<String> list) {
/* 264 */     List<String> valueString = asList(commaSeparatedString, ",");
/* 265 */     Collections.sort(valueString);
/*     */ 
/*     */     
/* 268 */     List<String> valueList = (list != null) ? new ArrayList<>(list) : new ArrayList<>();
/* 269 */     Collections.sort(valueList);
/*     */     
/* 271 */     return valueList.equals(valueString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isEqualsIgnoreCaseCommaSeparatedListAndList(String commaSeparatedString, List<String> list) {
/* 284 */     List<String> valueString = asList(commaSeparatedString, ",");
/* 285 */     Collections.sort(valueString);
/*     */ 
/*     */     
/* 288 */     List<String> valueList = (list != null) ? new ArrayList<>(list) : new ArrayList<>();
/* 289 */     Collections.sort(valueList);
/*     */     
/* 291 */     return valueString.toString().equalsIgnoreCase(valueList.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\CertificateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */