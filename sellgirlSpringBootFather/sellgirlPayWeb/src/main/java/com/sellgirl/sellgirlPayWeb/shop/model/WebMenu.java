package com.sellgirl.sellgirlPayWeb.shop.model;

import java.util.List;

public class WebMenu {

    public int Id ;
    public String Menuname ;
    public String Url ;
    public int Parent ;
    public String Icon ;//图标--by wxj20180621
    public List<WebMenu> MenuItems ;
    public Boolean IsHidden ;
}
