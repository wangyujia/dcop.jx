package com.dcop.jx.entry;

import java.lang.*;
import java.lang.annotation.*;
import java.util.*;

import com.dcop.jx.entry.*;



@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ClassImport {
    String value();
}