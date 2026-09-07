package com.hollowknight.model.charm;

import java.util.ArrayList;
import java.util.List;


public enum Charm {
    SOUL_CATCHER       ("Soul Catcher",        "Gain more Soul per nail hit.",      1),
    DASHMASTER         ("Dashmaster",           "Reduced dash cooldown.",            1),
    UNBREAKABLE_STRENGTH("Unbreakable Strength","Nail deals more damage.",           1),
    QUICK_SLASH        ("Quick Slash",          "Faster nail attack speed.",         1),
    QUICK_FOCUS        ("Quick Focus",          "Heal faster while focusing.",       1),
    HEAVY_BLOW         ("Heavy Blow",           "Enemies knocked back further.",     1),
    SHARP_SHADOW       ("Sharp Shadow",         "Dash through enemies dealing damage.", 1);

    public final String name;
    public final String description;
    public final int    notchCost;

    Charm(String name, String desc, int cost) {
        this.name = name; this.description = desc; this.notchCost = cost;
    }
}
