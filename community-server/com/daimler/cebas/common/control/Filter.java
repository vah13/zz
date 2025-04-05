/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.vo.DateFilter
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.control.Filter$1
 *  com.daimler.cebas.common.control.vo.FilterObject
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.vo.DateFilter;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.control.Filter;
import com.daimler.cebas.common.control.vo.FilterObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class Filter {
    private static final String SEPARATOR = "  ";
    private static final String CLOSE = ")";
    private static final String OPEN = "(";
    private static final String T = "T";
    private static final String IN = "in";
    private static final String MATCH = "match";
    private static final String SEPARATOR_REPLACER = ";";
    private static final String IN_SEPARATOR = ",";
    private static final String EMPTY = "";
    private static final String LTE = "lte=";
    private static final String GTE = "gte=";
    public static final String SLASH = "/";
    public static final String VALID_TO = "validTo";
    public static final String VALID_FROM = "validFrom";
    public static final String TYPE = "type";
    public static final String NULL = "null";
    public static final String PARENT = "parent";
    public static final String FILTER_VALID = "valid";
    public static final String SORT = "sort";
    private static final Map<CertificateType, Function<Certificate, String>> filterDescription = new EnumMap<CertificateType, Function<Certificate, String>>(CertificateType.class);

    public Map<String, FilterObject> getFilters(Map<String, String> allRequestParams) {
        HashMap<String, FilterObject> mapFilters = new HashMap<String, FilterObject>();
        if (allRequestParams == null) return mapFilters;
        allRequestParams.entrySet().forEach(entry -> this.determineFilterForEntry((Map<String, FilterObject>)mapFilters, (Map.Entry<String, String>)entry));
        return mapFilters;
    }

    public static Predicate<? super Certificate> getFilterPredicatedBasedOnType(CertificateType type, String search) {
        switch (1.$SwitchMap$com$daimler$cebas$certificates$entity$CertificateType[type.ordinal()]) {
            case 1: {
                return certificate -> Filter.rootFilter(search, certificate);
            }
            case 2: {
                return certificate -> Filter.backendFilter(search, certificate);
            }
            case 3: 
            case 4: {
                return Filter.linkCertificateFilter(search);
            }
            case 5: {
                return certificate -> Filter.matchValidTo(search, certificate) || Filter.ecuFilter(search, certificate);
            }
            case 6: {
                return certificate -> Filter.matchValidTo(search, certificate) || Filter.secocisFilter(search, certificate);
            }
            case 7: {
                return certificate -> Filter.matchValidTo(search, certificate) || Filter.enhancedRightsFilter(search, certificate);
            }
            case 8: {
                return certificate -> Filter.matchValidTo(search, certificate) || Filter.defaultFilter(search, certificate) || StringUtils.containsIgnoreCase(certificate.getNonce(), search);
            }
        }
        return certificate -> Filter.matchValidTo(search, certificate) || Filter.defaultFilter(search, certificate);
    }

    public static String getFilterDescription(Certificate certificate) {
        CertificateType type = certificate.isSecOCISCert() ? CertificateType.SEC_OC_IS : certificate.getType();
        return filterDescription.get(type).apply(certificate);
    }

    protected void determineFilterForEntry(Map<String, FilterObject> mapFilters, Map.Entry<String, String> entry) {
        this.dateMatch(mapFilters, entry);
        if (entry.getValue().contains(MATCH)) {
            this.match(mapFilters, entry);
        } else {
            if (!entry.getValue().contains(IN)) return;
            this.in(mapFilters, entry);
        }
    }

    protected void match(Map<String, FilterObject> mapFilters, Map.Entry<String, String> entry) {
        String value = this.getValue(entry);
        mapFilters.put(entry.getKey(), new FilterObject(true, (Object)value));
    }

    protected void in(Map<String, FilterObject> mapFilters, Map.Entry<String, String> entry) {
        if (!entry.getValue().contains(OPEN) && !entry.getValue().contains(CLOSE)) {
            throw new CEBASCertificateException("Cannot create filters, wrong input params");
        }
        String[] array = StringUtils.substringBetween(entry.getValue(), OPEN, CLOSE).split(IN_SEPARATOR);
        List asList = Arrays.stream(array).map(item -> item.replace(SEPARATOR_REPLACER, IN_SEPARATOR)).collect(Collectors.toList());
        String key = entry.getKey();
        mapFilters.put(key, new FilterObject(false, asList));
    }

    protected void dateMatch(Map<String, FilterObject> mapFilters, Map.Entry<String, String> entry) {
        if (entry.getKey().equals(VALID_FROM)) {
            String[] filter = entry.getValue().split(SLASH);
            DateFilter dateFilter = Filter.getDateFilter(filter);
            mapFilters.put(VALID_FROM, new FilterObject(false, (Object)dateFilter));
        } else {
            if (!entry.getKey().equals(VALID_TO)) return;
            String[] filter = entry.getValue().split(SLASH);
            DateFilter dateFilter = Filter.getDateFilter(filter);
            mapFilters.put(VALID_TO, new FilterObject(false, (Object)dateFilter));
        }
    }

    protected String getValue(Map.Entry<String, String> entry) {
        return entry.getValue().split(SLASH)[1].replace(SEPARATOR_REPLACER, IN_SEPARATOR);
    }

    private static boolean backendFilter(String search, Certificate certificate) {
        return Filter.rootFilter(search, certificate) || StringUtils.containsIgnoreCase(certificate.getIssuer(), search);
    }

    private static Predicate<? super Certificate> linkCertificateFilter(String search) {
        return certificate -> Filter.matchSubjectOrIssuer(search, certificate) || Filter.matchValidTo(search, certificate);
    }

    private static boolean enhancedRightsFilter(String search, Certificate certificate) {
        return Filter.matchTargetVINOrECU(search, certificate) || StringUtils.containsIgnoreCase(certificate.getBaseCertificateID(), search) || StringUtils.containsIgnoreCase(certificate.getIssuer(), search) || StringUtils.containsIgnoreCase(certificate.getTargetSubjectKeyIdentifier(), search);
    }

    private static boolean secocisFilter(String search, Certificate certificate) {
        return Filter.matchTargetVINOrECU(search, certificate) || StringUtils.containsIgnoreCase(certificate.getBaseCertificateID(), search) || StringUtils.containsIgnoreCase(certificate.getIssuer(), search) || StringUtils.containsIgnoreCase(certificate.getSerialNo(), search) || StringUtils.containsIgnoreCase(certificate.getTargetSubjectKeyIdentifier(), search);
    }

    private static boolean ecuFilter(String search, Certificate certificate) {
        return Filter.matchSubjectOrIssuer(search, certificate) || StringUtils.containsIgnoreCase(certificate.getSerialNo(), search) || StringUtils.containsIgnoreCase(certificate.getUniqueECUID(), search);
    }

    private static boolean rootFilter(String search, Certificate certificate) {
        return StringUtils.containsIgnoreCase(certificate.getSubject(), search) || StringUtils.containsIgnoreCase(certificate.getSerialNo(), search) || StringUtils.containsIgnoreCase(certificate.getSubjectKeyIdentifier(), search);
    }

    private static boolean defaultFilter(String search, Certificate certificate) {
        return Filter.matchTargetVINOrECU(search, certificate) || Filter.matchSubjectOrIssuer(search, certificate) || StringUtils.containsIgnoreCase(certificate.getUserRole(), search) || StringUtils.containsIgnoreCase(certificate.getSerialNo(), search);
    }

    private static boolean matchTargetVINOrECU(String search, Certificate certificate) {
        return StringUtils.containsIgnoreCase(certificate.getTargetECU(), search) || StringUtils.containsIgnoreCase(certificate.getTargetVIN(), search);
    }

    private static boolean matchSubjectOrIssuer(String search, Certificate certificate) {
        return StringUtils.containsIgnoreCase(certificate.getSubject(), search) || StringUtils.containsIgnoreCase(certificate.getIssuer(), search);
    }

    private static boolean matchValidTo(String search, Certificate certificate) {
        return StringUtils.containsIgnoreCase(certificate.getValidTo().toString(), search);
    }

    private static DateFilter getDateFilter(String ... filter) {
        DateFilter dateFilter;
        if (filter.length == 2) {
            String gt = filter[0].replaceAll(GTE, EMPTY);
            String lte = filter[1].replaceAll(LTE, EMPTY);
            dateFilter = new DateFilter(Filter.getDate(gt), Filter.getDate(lte));
        } else {
            String criteria = filter[0];
            if (criteria.contains(GTE)) {
                String gt = filter[0].replaceAll(GTE, EMPTY);
                dateFilter = new DateFilter(Filter.getDate(gt), null);
            } else {
                String lte = filter[0].replaceAll(LTE, EMPTY);
                dateFilter = new DateFilter(null, Filter.getDate(lte));
            }
        }
        return dateFilter;
    }

    private static Date getDate(String value) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(value);
        }
        catch (ParseException e) {
            throw new CEBASCertificateException("Cannot create filters, wrong input params");
        }
    }

    static {
        filterDescription.put(CertificateType.ROOT_CA_CERTIFICATE, certificate -> certificate.getSubject() + SEPARATOR + certificate.getSerialNo() + SEPARATOR + certificate.getSubjectKeyIdentifier());
        filterDescription.put(CertificateType.BACKEND_CA_CERTIFICATE, certificate -> certificate.getSubject() + SEPARATOR + certificate.getIssuer() + SEPARATOR + certificate.getSerialNo() + SEPARATOR + certificate.getSubjectKeyIdentifier());
        filterDescription.put(CertificateType.ROOT_CA_LINK_CERTIFICATE, certificate -> certificate.getSubject() + SEPARATOR + certificate.getIssuer() + SEPARATOR + certificate.getValidTo().toString());
        filterDescription.put(CertificateType.BACKEND_CA_LINK_CERTIFICATE, certificate -> certificate.getSubject() + SEPARATOR + certificate.getIssuer() + SEPARATOR + certificate.getValidTo().toString());
        filterDescription.put(CertificateType.ECU_CERTIFICATE, certificate -> certificate.getSubject() + SEPARATOR + certificate.getIssuer() + SEPARATOR + certificate.getSerialNo() + SEPARATOR + certificate.getUniqueECUID() + SEPARATOR + certificate.getValidTo().toString());
        filterDescription.put(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, certificate -> certificate.getSubject() + SEPARATOR + certificate.getUserRole() + SEPARATOR + certificate.getIssuer() + SEPARATOR + certificate.getSerialNo() + SEPARATOR + certificate.getTargetECU() + SEPARATOR + certificate.getTargetVIN() + SEPARATOR + certificate.getValidTo().toString());
        filterDescription.put(CertificateType.VARIANT_CODE_USER_CERTIFICATE, certificate -> certificate.getSubject() + SEPARATOR + certificate.getIssuer() + SEPARATOR + certificate.getSerialNo() + SEPARATOR + certificate.getTargetECU() + SEPARATOR + certificate.getTargetVIN() + SEPARATOR + certificate.getValidTo().toString());
        filterDescription.put(CertificateType.TIME_CERTIFICATE, certificate -> certificate.getSubject() + SEPARATOR + certificate.getUserRole() + SEPARATOR + certificate.getIssuer() + SEPARATOR + certificate.getSerialNo() + SEPARATOR + certificate.getNonce() + SEPARATOR + certificate.getTargetECU() + SEPARATOR + certificate.getTargetVIN() + SEPARATOR + certificate.getValidTo().toString());
        filterDescription.put(CertificateType.SEC_OC_IS, certificate -> certificate.getIssuer() + SEPARATOR + certificate.getBaseCertificateID() + SEPARATOR + certificate.getTargetSubjectKeyIdentifier() + SEPARATOR + certificate.getTargetECU() + SEPARATOR + certificate.getTargetVIN() + SEPARATOR + certificate.getValidTo().toString());
        filterDescription.put(CertificateType.ENHANCED_RIGHTS_CERTIFICATE, certificate -> certificate.getIssuer() + SEPARATOR + certificate.getBaseCertificateID() + SEPARATOR + certificate.getServices() + SEPARATOR + certificate.getTargetECU() + SEPARATOR + certificate.getTargetVIN() + SEPARATOR + certificate.getValidTo().toString());
        filterDescription.put(CertificateType.VIRTUAL_FOLDER, certificate -> "Virtual folder type");
        filterDescription.put(CertificateType.NO_TYPE, certificate -> "Invalid Certificate type");
    }
}
