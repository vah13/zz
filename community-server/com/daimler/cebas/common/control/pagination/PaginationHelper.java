/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.pagination.IBooleanPagination
 *  com.daimler.cebas.common.control.pagination.ISimplePagination
 */
package com.daimler.cebas.common.control.pagination;

import com.daimler.cebas.common.control.pagination.IBooleanPagination;
import com.daimler.cebas.common.control.pagination.ISimplePagination;

public class PaginationHelper {
    private static final int SIZE_100 = 100;
    private static final int SIZE_1500 = 1500;
    private static final int SIZE_5000 = 5000;
    private static final int SIZE_25000 = 25000;
    private static final int SIZE_100000 = 100000;

    private PaginationHelper() {
    }

    public static int getBatchSize(long totalCount) {
        if (totalCount <= 100L) {
            return 100;
        }
        if (totalCount <= 1500L) {
            return (int)totalCount * 20 / 100;
        }
        if (totalCount <= 5000L) {
            return (int)totalCount * 10 / 100;
        }
        if (totalCount <= 25000L) {
            return (int)totalCount * 3 / 100;
        }
        if (totalCount > 100000L) return (int)totalCount / 500;
        return (int)totalCount / 200;
    }

    public static void makePagination(long totalSize, ISimplePagination consumer) {
        int page = 0;
        int batchSize = PaginationHelper.getBatchSize(totalSize);
        while ((long)(page * batchSize) < totalSize) {
            consumer.accept(page, batchSize);
            ++page;
        }
    }

    public static boolean makeBooleanPagination(long totalSize, IBooleanPagination consumer) {
        int page = 0;
        int batchSize = PaginationHelper.getBatchSize(totalSize);
        while ((long)(page * batchSize) < totalSize) {
            boolean result = consumer.accept(page, batchSize);
            if (!result) {
                return result;
            }
            ++page;
        }
        return true;
    }

    public static boolean isOnLastPage(int page, int pageSize, long totalCount) {
        return PaginationHelper.getRemainingNumberOfElements(page, pageSize, totalCount) <= pageSize;
    }

    public static int getRemainingNumberOfElements(int page, int pageSize, long totalCount) {
        return (int)totalCount - page * pageSize;
    }
}
