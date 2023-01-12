package com.clickup.api.helpers;

import java.util.ArrayList;
import java.util.Collections;

public final class Color {

    public static final String ORANGE = "#FF8600";
    public static final String RED = "#F31D2F";
    public static final String YELLOW = "#FCB410";
    public static final String PURPLE = "#7B68EE";
    public static final String BLUE = "#0918EC";
    public static final String CYAN = "#3498DB";
    public static final String GREEN = "#00D717";
    public static final String JAVA = "#1ABC9C";
    public static final String BLACK = "#181D21";
    public static final String DEFAULT = null;

    static ArrayList<String> colorsList() {

        ArrayList<String> colorsList = new ArrayList<>();
        colorsList.add(ORANGE);
        colorsList.add(RED);
        colorsList.add(YELLOW);
        colorsList.add(PURPLE);
        colorsList.add(BLUE);
        colorsList.add(CYAN);
        colorsList.add(GREEN);
        colorsList.add(JAVA);
        colorsList.add(BLACK);

        return colorsList;

    }

    public static String getRandomColor() {
        ArrayList<String> colorsList = colorsList();
        Collections.shuffle(colorsList);
        return colorsList.get(0);
    }

    public static String getRandomColorWithoutExcluded(ArrayList<String> excludedColors) {
        ArrayList<String> colorsList = colorsList();

        if (excludedColors.size() <= colorsList.size() - 1) {
            for (String excludedColor : excludedColors) {
                colorsList.remove(excludedColor);
            }
        } else {
            throw new IllegalArgumentException("Excluded colors list size can't be equals or higher then colors list");
        }
        Collections.shuffle(colorsList);
        return colorsList.get(0);
    }


}
