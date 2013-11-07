package com.want2play.datastore;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class Factory {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private Factory() {}

    public static PersistenceManagerFactory getInstance() {
        return pmfInstance;
    }
}
