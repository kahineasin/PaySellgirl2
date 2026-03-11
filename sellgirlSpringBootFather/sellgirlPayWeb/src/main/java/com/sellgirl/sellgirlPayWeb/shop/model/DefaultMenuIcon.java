package com.sellgirl.sellgirlPayWeb.shop.model;

public class DefaultMenuIcon {
    public int _currentIcon = 0;
    public String NextIcon()
    {
         _currentIcon++;
         String[]  icons = new String[] { "office", "gongwen", "nav-info", "konwledge", "agency", "email", "system" };
        if (_currentIcon > icons.length-1) { _currentIcon=0; }
        return icons[_currentIcon];
    }
}
