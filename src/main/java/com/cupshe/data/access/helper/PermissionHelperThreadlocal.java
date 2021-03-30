package com.cupshe.data.access.helper;

import com.cupshe.data.access.common.PermissionControl;

/**
 * 权限帮助类Threadlocal
 * <p>Title: PermissionHelperThreadlocal</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月29日
 */
public class PermissionHelperThreadlocal {

    protected static final ThreadLocal<PermissionControl> LOCAL_PERMISSION = new ThreadLocal<PermissionControl>();

    /**
     * 设置PermissionControl
     *
     * @param page
     */
    protected static void setLocalPermissionControl(PermissionControl permissionControl) {
        LOCAL_PERMISSION.set(permissionControl);
    }

    /**
     * 获取PermissionControl
     *
     * @return
     */
    protected static PermissionControl getLocalPermissionControl() {
        return LOCAL_PERMISSION.get();
    }

    /**
     * 移除PermissionControl
     */
    protected static void clearLocalPermissionControl() {
        LOCAL_PERMISSION.remove();
    }
	
}
