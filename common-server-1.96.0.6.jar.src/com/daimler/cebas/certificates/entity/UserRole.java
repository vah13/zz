/*    */ package com.daimler.cebas.certificates.entity;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum UserRole
/*    */ {
/* 13 */   SUPPLIER("Supplier"), DEVELOPMENT_ENHANCED("Development ENHANCED"),
/* 14 */   PRODUCTION("Production"), AS_ENHANCED("After-Sales ENHANCED"),
/* 15 */   AS_STANDARD("After-Sales STANDARD"),
/* 16 */   AS_BASIC("After-Sales BASIC"),
/* 17 */   INTERNAL_DIAGNOSTIC_TEST_TOOL("Internal Diagnostic Test Tool"),
/* 18 */   EPTI_TEST_TOOL("ePTI Test Tool"),
/* 19 */   NO_ROLE("");
/*    */   private String text;
/*    */   private static final Map<String, String> map;
/*    */   
/*    */   static {
/* 24 */     map = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 81 */     UserRole[] values = values();
/* 82 */     for (UserRole userRole : values)
/* 83 */     { if (userRole != NO_ROLE)
/* 84 */         map.put(Integer.toString(userRole.ordinal() + 1), userRole
/* 85 */             .getText());  } 
/*    */   }
/*    */   UserRole(String text) { this.text = text; } public static UserRole getUserRoleFromByte(byte value) { switch (value) { case 1: return SUPPLIER;case 2: return DEVELOPMENT_ENHANCED;case 3: return PRODUCTION;case 4: return AS_ENHANCED;
/*    */       case 5: return AS_STANDARD;
/*    */       case 6: return AS_BASIC;
/*    */       case 7: return INTERNAL_DIAGNOSTIC_TEST_TOOL;
/* 91 */       case 8: return EPTI_TEST_TOOL; }  return NO_ROLE; } public static Map<String, String> getValuesMap() { return map; }
/*    */   public static UserRole getUserRoleFromText(String value) { switch (value) { case "Supplier": return SUPPLIER;case "Development ENHANCED": return DEVELOPMENT_ENHANCED;case "Production": return PRODUCTION;case "After-Sales ENHANCED": return AS_ENHANCED;case "After-Sales STANDARD": return AS_STANDARD;case "After-Sales BASIC": return AS_BASIC;
/*    */       case "Internal Diagnostic Test Tool": return INTERNAL_DIAGNOSTIC_TEST_TOOL;
/*    */       case "ePTI Test Tool": return EPTI_TEST_TOOL; }  return NO_ROLE; }
/* 95 */   public String getText() { return this.text; } public static byte getByteFromText(String text) { return Byte.decode((String)((Map.Entry)map.entrySet().stream()
/* 96 */         .filter(entry -> ((String)entry.getValue()).equals(text)).findAny()
/* 97 */         .orElseThrow(() -> new InvalidInputException("Invalid user role")))
/*    */         
/* 99 */         .getKey()).byteValue(); }
/*    */ 
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\UserRole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */