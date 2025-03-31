/*     */ package com.daimler.cebas.users.entity;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import javax.persistence.Column;
/*     */ import javax.persistence.Entity;
/*     */ import javax.persistence.FetchType;
/*     */ import javax.persistence.JoinColumn;
/*     */ import javax.persistence.ManyToOne;
/*     */ import javax.persistence.NamedQueries;
/*     */ import javax.persistence.NamedQuery;
/*     */ import javax.persistence.OneToOne;
/*     */ import javax.persistence.Table;
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
/*     */ @Entity
/*     */ @Table(name = "r_key_store")
/*     */ @NamedQueries({@NamedQuery(name = "findUserKeyPairByCertificate", query = "Select kp from UserKeyPair kp JOIN kp.certificate c WHERE c =:certificate"), @NamedQuery(name = "findUserKeyPairWithNullCertificate", query = "SELECT kp from UserKeyPair kp where kp.user =:user and kp.certificate IS NULL")})
/*     */ public class UserKeyPair
/*     */   extends AbstractEntity
/*     */ {
/*     */   private static final long serialVersionUID = 4568854091921404325L;
/*     */   public static final String CERTIFICATE_FIELD = "certificate";
/*     */   public static final String FIND_KEY_PAIR_BY_CERTIFICATE = "findUserKeyPairByCertificate";
/*     */   public static final String FIND_KEY_PAIR_WITH_NULL_CERTIFICATE = "findUserKeyPairWithNullCertificate";
/*     */   public static final String QUERY_BY_CERTIFICATE = "Select kp from UserKeyPair kp JOIN kp.certificate c WHERE c =:certificate";
/*     */   public static final String QUERY_BY_NULL_CERTIFICATE = "SELECT kp from UserKeyPair kp where kp.user =:user and kp.certificate IS NULL";
/*     */   public static final String R_KEY_STORE = "r_key_store";
/*     */   public static final String F_PUBLIC_KEY = "f_public_key";
/*     */   public static final String F_PRIVATE_KEY = "f_private_key";
/*     */   @Column(name = "f_public_key")
/*     */   private String publicKey;
/*     */   @Column(name = "f_private_key")
/*     */   private String privateKey;
/*     */   @JoinColumn(name = "r_certificate")
/*     */   @OneToOne(fetch = FetchType.LAZY)
/*     */   private Certificate certificate;
/*     */   @ManyToOne(fetch = FetchType.LAZY)
/*     */   @JoinColumn(name = "r_user")
/*     */   private User user;
/*     */   
/*     */   public UserKeyPair() {}
/*     */   
/*     */   public UserKeyPair(String publicKey, String privateKey, User user) {
/* 104 */     this.publicKey = publicKey;
/* 105 */     this.privateKey = privateKey;
/* 106 */     this.user = user;
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
/*     */   public UserKeyPair(String publicKey, String privateKey, User user, Certificate certificate) {
/* 118 */     this(publicKey, privateKey, user);
/* 119 */     this.certificate = certificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPublicKey() {
/* 127 */     return this.publicKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrivateKey() {
/* 135 */     return this.privateKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Certificate getCertificate() {
/* 143 */     return this.certificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCertificate(Certificate certificate) {
/* 151 */     this.certificate = certificate;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\entity\UserKeyPair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */