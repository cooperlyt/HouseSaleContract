package com.dgsoft.cms.sale.exceptions;

import org.jboss.seam.annotations.ApplicationException;

/**
 * Created by cooper on 12/20/15.
 */
@ApplicationException()
public class OwnerServerException extends IllegalStateException{

    public OwnerServerException() {
    }

    public OwnerServerException(String s) {
        super(s);
    }

    public OwnerServerException(Throwable cause) {
        super(cause);
    }
}


