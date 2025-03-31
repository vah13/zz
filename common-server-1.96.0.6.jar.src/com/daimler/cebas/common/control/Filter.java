/*     */ package com.daimler.cebas.common.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.vo.DateFilter;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.control.vo.FilterObject;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
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
/*     */ public class Filter
/*     */ {
/*     */   private static final String SEPARATOR = "  ";
/*     */   private static final String CLOSE = ")";
/*     */   private static final String OPEN = "(";
/*     */   private static final String T = "T";
/*     */   private static final String IN = "in";
/*     */   private static final String MATCH = "match";
/*     */   private static final String SEPARATOR_REPLACER = ";";
/*     */   private static final String IN_SEPARATOR = ",";
/*     */   private static final String EMPTY = "";
/*     */   private static final String LTE = "lte=";
/*     */   private static final String GTE = "gte=";
/*     */   public static final String SLASH = "/";
/*     */   public static final String VALID_TO = "validTo";
/*     */   public static final String VALID_FROM = "validFrom";
/*     */   public static final String TYPE = "type";
/*     */   public static final String NULL = "null";
/*     */   public static final String PARENT = "parent";
/*     */   public static final String FILTER_VALID = "valid";
/*     */   public static final String SORT = "sort";
/* 121 */   private static final Map<CertificateType, Function<Certificate, String>> filterDescription = new EnumMap<>(CertificateType.class);
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
/*     */   static {
/* 134 */     filterDescription.put(CertificateType.ROOT_CA_CERTIFICATE, certificate -> certificate.getSubject() + "  " + certificate.getSerialNo() + "  " + certificate.getSubjectKeyIdentifier());
/*     */ 
/*     */     
/* 137 */     filterDescription.put(CertificateType.BACKEND_CA_CERTIFICATE, certificate -> certificate.getSubject() + "  " + certificate.getIssuer() + "  " + certificate.getSerialNo() + "  " + certificate.getSubjectKeyIdentifier());
/*     */ 
/*     */ 
/*     */     
/* 141 */     filterDescription.put(CertificateType.ROOT_CA_LINK_CERTIFICATE, certificate -> certificate.getSubject() + "  " + certificate.getIssuer() + "  " + certificate.getValidTo().toString());
/*     */ 
/*     */     
/* 144 */     filterDescription.put(CertificateType.BACKEND_CA_LINK_CERTIFICATE, certificate -> certificate.getSubject() + "  " + certificate.getIssuer() + "  " + certificate.getValidTo().toString());
/*     */ 
/*     */     
/* 147 */     filterDescription.put(CertificateType.ECU_CERTIFICATE, certificate -> certificate.getSubject() + "  " + certificate.getIssuer() + "  " + certificate.getSerialNo() + "  " + certificate.getUniqueECUID() + "  " + certificate.getValidTo().toString());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     filterDescription.put(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, certificate -> certificate.getSubject() + "  " + certificate.getUserRole() + "  " + certificate.getIssuer() + "  " + certificate.getSerialNo() + "  " + certificate.getTargetECU() + "  " + certificate.getTargetVIN() + "  " + certificate.getValidTo().toString());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     filterDescription.put(CertificateType.VARIANT_CODE_USER_CERTIFICATE, certificate -> certificate.getSubject() + "  " + certificate.getIssuer() + "  " + certificate.getSerialNo() + "  " + certificate.getTargetECU() + "  " + certificate.getTargetVIN() + "  " + certificate.getValidTo().toString());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     filterDescription.put(CertificateType.TIME_CERTIFICATE, certificate -> certificate.getSubject() + "  " + certificate.getUserRole() + "  " + certificate.getIssuer() + "  " + certificate.getSerialNo() + "  " + certificate.getNonce() + "  " + certificate.getTargetECU() + "  " + certificate.getTargetVIN() + "  " + certificate.getValidTo().toString());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     filterDescription.put(CertificateType.SEC_OC_IS, certificate -> certificate.getIssuer() + "  " + certificate.getBaseCertificateID() + "  " + certificate.getTargetSubjectKeyIdentifier() + "  " + certificate.getTargetECU() + "  " + certificate.getTargetVIN() + "  " + certificate.getValidTo().toString());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     filterDescription.put(CertificateType.ENHANCED_RIGHTS_CERTIFICATE, certificate -> certificate.getIssuer() + "  " + certificate.getBaseCertificateID() + "  " + certificate.getServices() + "  " + certificate.getTargetECU() + "  " + certificate.getTargetVIN() + "  " + certificate.getValidTo().toString());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     filterDescription.put(CertificateType.VIRTUAL_FOLDER, certificate -> "Virtual folder type");
/* 180 */     filterDescription.put(CertificateType.NO_TYPE, certificate -> "Invalid Certificate type");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, FilterObject> getFilters(Map<String, String> allRequestParams) {
/* 191 */     Map<String, FilterObject> mapFilters = new HashMap<>();
/* 192 */     if (allRequestParams != null) {
/* 193 */       allRequestParams.entrySet().forEach(entry -> determineFilterForEntry(mapFilters, entry));
/*     */     }
/* 195 */     return mapFilters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<? super Certificate> getFilterPredicatedBasedOnType(CertificateType type, String search) {
/* 206 */     switch (type) {
/*     */       case ROOT_CA_CERTIFICATE:
/* 208 */         return certificate -> rootFilter(search, certificate);
/*     */       case BACKEND_CA_CERTIFICATE:
/* 210 */         return certificate -> backendFilter(search, certificate);
/*     */       case ROOT_CA_LINK_CERTIFICATE:
/*     */       case BACKEND_CA_LINK_CERTIFICATE:
/* 213 */         return linkCertificateFilter(search);
/*     */       case ECU_CERTIFICATE:
/* 215 */         return certificate -> (matchValidTo(search, certificate) || ecuFilter(search, certificate));
/*     */       case SEC_OC_IS:
/* 217 */         return certificate -> (matchValidTo(search, certificate) || secocisFilter(search, certificate));
/*     */       case ENHANCED_RIGHTS_CERTIFICATE:
/* 219 */         return certificate -> (matchValidTo(search, certificate) || enhancedRightsFilter(search, certificate));
/*     */       case TIME_CERTIFICATE:
/* 221 */         return certificate -> (matchValidTo(search, certificate) || defaultFilter(search, certificate) || StringUtils.containsIgnoreCase(certificate.getNonce(), search));
/*     */     } 
/*     */     
/* 224 */     return certificate -> (matchValidTo(search, certificate) || defaultFilter(search, certificate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFilterDescription(Certificate certificate) {
/*     */     CertificateType type;
/* 236 */     if (certificate.isSecOCISCert()) {
/* 237 */       type = CertificateType.SEC_OC_IS;
/*     */     } else {
/* 239 */       type = certificate.getType();
/*     */     } 
/* 241 */     return ((Function<Certificate, String>)filterDescription.get(type)).apply(certificate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void determineFilterForEntry(Map<String, FilterObject> mapFilters, Map.Entry<String, String> entry) {
/* 251 */     dateMatch(mapFilters, entry);
/* 252 */     if (((String)entry.getValue()).contains("match")) {
/* 253 */       match(mapFilters, entry);
/* 254 */     } else if (((String)entry.getValue()).contains("in")) {
/* 255 */       in(mapFilters, entry);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void match(Map<String, FilterObject> mapFilters, Map.Entry<String, String> entry) {
/* 266 */     String value = getValue(entry);
/* 267 */     mapFilters.put(entry.getKey(), new FilterObject(true, value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void in(Map<String, FilterObject> mapFilters, Map.Entry<String, String> entry) {
/* 277 */     if (!((String)entry.getValue()).contains("(") && !((String)entry.getValue()).contains(")")) {
/* 278 */       throw new CEBASCertificateException("Cannot create filters, wrong input params");
/*     */     }
/* 280 */     String[] array = StringUtils.substringBetween(entry.getValue(), "(", ")").split(",");
/*     */     
/* 282 */     List<String> asList = (List<String>)Arrays.<String>stream(array).map(item -> item.replace(";", ",")).collect(Collectors.toList());
/* 283 */     String key = entry.getKey();
/* 284 */     mapFilters.put(key, new FilterObject(false, asList));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dateMatch(Map<String, FilterObject> mapFilters, Map.Entry<String, String> entry) {
/* 294 */     if (((String)entry.getKey()).equals("validFrom")) {
/* 295 */       String[] filter = ((String)entry.getValue()).split("/");
/* 296 */       DateFilter dateFilter = getDateFilter(filter);
/* 297 */       mapFilters.put("validFrom", new FilterObject(false, dateFilter));
/* 298 */     } else if (((String)entry.getKey()).equals("validTo")) {
/* 299 */       String[] filter = ((String)entry.getValue()).split("/");
/* 300 */       DateFilter dateFilter = getDateFilter(filter);
/* 301 */       mapFilters.put("validTo", new FilterObject(false, dateFilter));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getValue(Map.Entry<String, String> entry) {
/* 312 */     return ((String)entry.getValue()).split("/")[1].replace(";", ",");
/*     */   }
/*     */   
/*     */   private static boolean backendFilter(String search, Certificate certificate) {
/* 316 */     return (rootFilter(search, certificate) || StringUtils.containsIgnoreCase(certificate.getIssuer(), search));
/*     */   }
/*     */   
/*     */   private static Predicate<? super Certificate> linkCertificateFilter(String search) {
/* 320 */     return certificate -> (matchSubjectOrIssuer(search, certificate) || matchValidTo(search, certificate));
/*     */   }
/*     */   
/*     */   private static boolean enhancedRightsFilter(String search, Certificate certificate) {
/* 324 */     return (matchTargetVINOrECU(search, certificate) || 
/* 325 */       StringUtils.containsIgnoreCase(certificate.getBaseCertificateID(), search) || 
/* 326 */       StringUtils.containsIgnoreCase(certificate.getIssuer(), search) || 
/* 327 */       StringUtils.containsIgnoreCase(certificate.getTargetSubjectKeyIdentifier(), search));
/*     */   }
/*     */   
/*     */   private static boolean secocisFilter(String search, Certificate certificate) {
/* 331 */     return (matchTargetVINOrECU(search, certificate) || 
/* 332 */       StringUtils.containsIgnoreCase(certificate.getBaseCertificateID(), search) || 
/* 333 */       StringUtils.containsIgnoreCase(certificate.getIssuer(), search) || 
/* 334 */       StringUtils.containsIgnoreCase(certificate.getSerialNo(), search) || 
/* 335 */       StringUtils.containsIgnoreCase(certificate.getTargetSubjectKeyIdentifier(), search));
/*     */   }
/*     */   
/*     */   private static boolean ecuFilter(String search, Certificate certificate) {
/* 339 */     return (matchSubjectOrIssuer(search, certificate) || 
/* 340 */       StringUtils.containsIgnoreCase(certificate.getSerialNo(), search) || 
/* 341 */       StringUtils.containsIgnoreCase(certificate.getUniqueECUID(), search));
/*     */   }
/*     */   
/*     */   private static boolean rootFilter(String search, Certificate certificate) {
/* 345 */     return (StringUtils.containsIgnoreCase(certificate.getSubject(), search) || 
/* 346 */       StringUtils.containsIgnoreCase(certificate.getSerialNo(), search) || 
/* 347 */       StringUtils.containsIgnoreCase(certificate.getSubjectKeyIdentifier(), search));
/*     */   }
/*     */   
/*     */   private static boolean defaultFilter(String search, Certificate certificate) {
/* 351 */     return (matchTargetVINOrECU(search, certificate) || matchSubjectOrIssuer(search, certificate) || 
/* 352 */       StringUtils.containsIgnoreCase(certificate.getUserRole(), search) || 
/* 353 */       StringUtils.containsIgnoreCase(certificate.getSerialNo(), search));
/*     */   }
/*     */   
/*     */   private static boolean matchTargetVINOrECU(String search, Certificate certificate) {
/* 357 */     return (StringUtils.containsIgnoreCase(certificate.getTargetECU(), search) || 
/* 358 */       StringUtils.containsIgnoreCase(certificate.getTargetVIN(), search));
/*     */   }
/*     */   
/*     */   private static boolean matchSubjectOrIssuer(String search, Certificate certificate) {
/* 362 */     return (StringUtils.containsIgnoreCase(certificate.getSubject(), search) || 
/* 363 */       StringUtils.containsIgnoreCase(certificate.getIssuer(), search));
/*     */   }
/*     */   
/*     */   private static boolean matchValidTo(String search, Certificate certificate) {
/* 367 */     return StringUtils.containsIgnoreCase(certificate.getValidTo().toString(), search);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DateFilter getDateFilter(String... filter) {
/*     */     DateFilter dateFilter;
/* 378 */     if (filter.length == 2) {
/* 379 */       String gt = filter[0].replaceAll("gte=", "");
/* 380 */       String lte = filter[1].replaceAll("lte=", "");
/* 381 */       dateFilter = new DateFilter(getDate(gt), getDate(lte));
/*     */     } else {
/* 383 */       String criteria = filter[0];
/* 384 */       if (criteria.contains("gte=")) {
/* 385 */         String gt = filter[0].replaceAll("gte=", "");
/* 386 */         dateFilter = new DateFilter(getDate(gt), null);
/*     */       } else {
/* 388 */         String lte = filter[0].replaceAll("lte=", "");
/* 389 */         dateFilter = new DateFilter(null, getDate(lte));
/*     */       } 
/*     */     } 
/* 392 */     return dateFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Date getDate(String value) {
/*     */     try {
/* 403 */       return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).parse(value);
/* 404 */     } catch (ParseException e) {
/* 405 */       throw new CEBASCertificateException("Cannot create filters, wrong input params");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\Filter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */