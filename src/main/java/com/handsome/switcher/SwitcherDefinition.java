package com.handsome.switcher;

/**
 * 这个类是开关的定义类，就像beanDefinition那样的作用
 */
public class SwitcherDefinition {
    /**
     * 开关状态
     */
    private boolean state;

    /**
     * 过期时间
     */
    private int expiry = 0;

    /**
     * 生效时间
     */
    private int effective = 0;

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
