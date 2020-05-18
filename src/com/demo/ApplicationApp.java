/*
 * Decompiled with CFR 0.149.
 */
package com.demo;

import com.demo.services.OperationService;

public class ApplicationApp {
    public static void main(String[] args) {
        OperationService operationService = new OperationService();
        operationService.deleteData();
    }
}

