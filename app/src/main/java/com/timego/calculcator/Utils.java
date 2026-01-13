package com.timego.calculcator;

public  class Utils {

    public static boolean isImageUrl(String url) {
        String end = url.substring(url.lastIndexOf(".") + 1).toLowerCase();
        String[] images = new String[]{"jpeg", "png", "jpg", "bmp", "webp", "gif"};
        for (String name : images) {
            if (end.equals(name)) {
                return true;
            }
        }
        return false;
    }

}
