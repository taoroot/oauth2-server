package cn.flizi.cloud.common.security.core.social;

import java.security.Principal;

public class SocialUserInfo implements Principal {
    @Override
    public boolean equals(Object another) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
}
