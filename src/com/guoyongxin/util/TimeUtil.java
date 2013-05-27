/*
 * Copyright (c) 2013 NeuLion, Inc. All Rights Reserved.
 */
package com.guoyongxin.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class TimeUtil
{
    public static String getTime(int millseconds)
    {
        SimpleDateFormat outFormat = new SimpleDateFormat("mm:ss");
        outFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outFormat.format(millseconds);
    }
}
