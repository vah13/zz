/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.DurationParser
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.PKIRole
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission
 *  com.daimler.cebas.certificates.integration.vo.Permission
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.control.DurationParser;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.PKIRole;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
import com.daimler.cebas.certificates.integration.vo.Permission;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class CertificateUtil {
    private CertificateUtil() {
    }

    public static String getCommaSeparatedStringFromList(List<String> values) {
        if (values != null) return String.join((CharSequence)", ", values);
        return "";
    }

    public static boolean isListEmpty(List<String> values) {
        String string;
        if (values == null) return true;
        if (values.isEmpty()) {
            return true;
        }
        Iterator<String> iterator = values.iterator();
        do {
            if (!iterator.hasNext()) return true;
        } while (StringUtils.isEmpty(string = iterator.next()));
        return false;
    }

    public static boolean isCertificateMatchingEnhancedRightsPermission(Certificate certificate, EnhancedRightsPermission service) {
        List<String> services = service.getServiceIds() != null ? service.getServiceIds().stream().map(s -> s.replace("0x", "")).collect(Collectors.toList()) : Collections.emptyList();
        return certificate.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && CertificateUtil.isEqualsIgnoreCaseCommaSeparatedListAndList(certificate.getServices(), services) && CertificateUtil.isEqualsCommaSeparatedListAndList(certificate.getTargetECU(), service.getTargetECU()) && CertificateUtil.isEqualsCommaSeparatedListAndList(certificate.getTargetVIN(), service.getTargetVIN());
    }

    public static String getSortedCommaSeparatedList(List<String> someList) {
        if (someList == null) {
            return "";
        }
        ArrayList<String> listToSort = new ArrayList<String>(someList);
        Collections.sort(listToSort);
        return CertificateUtil.getCommaSeparatedStringFromList(listToSort);
    }

    public static boolean isCertificateMatchingPermission(Certificate certificate, Permission permission) {
        String userRoleFromPermission = CertificateUtil.getUserRoleFromPermission(permission.getUserRole());
        String pkiRoleText = CertificateUtil.getPKIRoleTextFromPermission(permission.getPkiRole());
        return StringUtils.equals(certificate.getUserRole(), userRoleFromPermission) && StringUtils.equals(certificate.getPKIRole(), pkiRoleText) && CertificateUtil.isEqualsCommaSeparatedListAndList(certificate.getTargetECU(), permission.getTargetECU()) && CertificateUtil.isEqualsCommaSeparatedListAndList(certificate.getTargetVIN(), permission.getTargetVIN());
    }

    public static boolean compareCommaSeparatedLists(String first, String second) {
        List<String> firstList = CertificateUtil.asList(first, ",");
        Collections.sort(firstList);
        List<String> secondList = CertificateUtil.asList(second, ",");
        Collections.sort(secondList);
        return firstList.equals(secondList);
    }

    public static boolean includesTargetECUsAndTargetVINs(Certificate certificate, List<String> targetECUs, List<String> targetVINs) {
        List<String> ecus = targetECUs != null ? targetECUs : Collections.emptyList();
        List<String> vins = targetVINs != null ? targetVINs : Collections.emptyList();
        List<String> certificateTargetECUs = CertificateUtil.asList(certificate.getTargetECU(), ",");
        List<String> certificateTargetVINs = CertificateUtil.asList(certificate.getTargetVIN(), ",");
        return certificateTargetECUs.containsAll(ecus) && certificateTargetVINs.containsAll(vins);
    }

    public static String getPattern(List<String> input) {
        StringBuilder pattern = new StringBuilder();
        for (String value : input) {
            pattern.append("(?=.*").append(Pattern.quote(value)).append(".*)");
        }
        return StringUtils.isEmpty(pattern.toString()) ? ".*" : pattern.toString();
    }

    public static String getServicesFromEnhRightsPermission(List<String> services) {
        if (services == null) {
            return "";
        }
        List fixedList = services.stream().map(service -> service.replace("0x", "")).collect(Collectors.toList());
        return String.join((CharSequence)", ", fixedList);
    }

    public static String getPKIRoleNameFromPermission(String byteValue) {
        int pkiRole = Integer.decode(byteValue);
        return ((CertificateType)PKIRole.getRoles().get(pkiRole)).name();
    }

    public static String getPKIRoleTextFromPermission(String byteValue) {
        int pkiRole = Integer.decode(byteValue);
        return ((CertificateType)PKIRole.getRoles().get(pkiRole)).getText();
    }

    public static String getUserRoleFromPermission(String byteValue) {
        if (StringUtils.isBlank(byteValue)) return "";
        UserRole userRole = UserRole.getUserRoleFromByte((byte)Byte.decode(byteValue));
        return userRole.getText();
    }

    public static boolean isCertificateValidityExceedingMinimumRenewal(Certificate certificate, String renewal) {
        DurationParser renewalPeriod = DurationParser.parse((String)renewal);
        LocalDateTime renewalTime = renewalPeriod.plusTo(LocalDateTime.now());
        return certificate.getValidTo().getTime() <= Timestamp.valueOf(renewalTime).getTime();
    }

    public static List<String> asList(String str, String separator) {
        if (!StringUtils.isEmpty(str)) return Arrays.asList(str.split("\\s*" + separator + "\\s*"));
        return new ArrayList<String>();
    }

    private static boolean isEqualsCommaSeparatedListAndList(String commaSeparatedString, List<String> list) {
        List<String> valueString = CertificateUtil.asList(commaSeparatedString, ",");
        Collections.sort(valueString);
        ArrayList<String> valueList = list != null ? new ArrayList<String>(list) : new ArrayList();
        Collections.sort(valueList);
        return valueList.equals(valueString);
    }

    private static boolean isEqualsIgnoreCaseCommaSeparatedListAndList(String commaSeparatedString, List<String> list) {
        List<String> valueString = CertificateUtil.asList(commaSeparatedString, ",");
        Collections.sort(valueString);
        ArrayList<String> valueList = list != null ? new ArrayList<String>(list) : new ArrayList();
        Collections.sort(valueList);
        return valueString.toString().equalsIgnoreCase(((Object)valueList).toString());
    }
}
