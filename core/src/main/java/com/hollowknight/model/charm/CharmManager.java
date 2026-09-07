package com.hollowknight.model.charm;

import com.hollowknight.util.Constants;
import java.util.EnumSet;
import java.util.Set;

public class CharmManager {

    private final Set<Charm> equipped = EnumSet.noneOf(Charm.class);
    private final Set<Charm> owned    = EnumSet.noneOf(Charm.class);
    private int usedNotches = 0;

    public CharmManager() {
        for (Charm c : Charm.values()) owned.add(c);
    }

    public boolean equip(Charm c) {
        if (!owned.contains(c) || equipped.contains(c)) return false;
        if (usedNotches + c.notchCost > Constants.MAX_NOTCHES) return false;
        equipped.add(c);
        usedNotches += c.notchCost;
        return true;
    }

    public boolean unequip(Charm c) {
        if (!equipped.contains(c)) return false;
        equipped.remove(c);
        usedNotches -= c.notchCost;
        return true;
    }

    public boolean toggle(Charm c) {
        return equipped.contains(c) ? unequip(c) : equip(c);
    }

    public boolean has(Charm c)      { return equipped.contains(c); }
    public boolean owns(Charm c)     { return owned.contains(c); }
    public Set<Charm> getEquipped()  { return equipped; }
    public Set<Charm> getOwned()     { return owned; }
    public int getUsedNotches()      { return usedNotches; }
    public int getMaxNotches()       { return Constants.MAX_NOTCHES; }
}
