# Hollow Knight Clone

A 2D action-platformer built in Java with libGDX, inspired by Hollow
Knight. Built with a clean MVC architecture from the ground up.

## What's in it

**Architecture**: clean MVC separation throughout.
- `model/` — game state and data, no rendering logic
- `view/` — rendering only
- `controller/` — input, physics, AI, and everything that mutates state

**Core gameplay:**
- Full player physics: running, jumping (variable height + cutoff), double jump, dash (with cooldown), wall slide, pogo, Focus heal
- Nail attack with a 4-directional hitbox (left/right/up/down for pogo-ing off enemies)
- Soul system — build it up on hits, spend it on Focus/spells, capped at 99
- Mask/HP system with invincibility frames, knockback, and respawn-to-checkpoint
- Two spells: Vengeful Spirit (projectile) and Howling Wraiths (burst attack)
- 8 charms with a notch-limit system managing how many can be equipped at once

**Enemies & bosses:**
- Several enemy AI types: a generic ground walker, a dash-attacking Mosquito, a patrol/rest/charge Husk Hornhead, and a laser-based Crystal Guardian
- A full boss fight against the False Knight — a 5-move decision system, a stun phase at 50% HP, a faster/harder second phase, and a wave attack

**Systems:**
- An event bus (Observer pattern) powering an in-game achievement system
- Save/load support (JSON-based)
- A handful of cheat codes for quick testing (Ctrl+B/N/H/R/G/K)
- A camera that follows the player smoothly, with screen shake and arena bounds
- Sprite-based animation throughout — the player, enemies, and boss all animate through proper frame sequences instead of static shapes
- Real Knight sprite art, sliced from sprite sheets and rendered at the correct scale relative to the hitbox
- Level data loaded from Tiled (`.tmx`) maps, with collision, hazard, enemy spawn, and player spawn layers all read directly from the map file

**Rooms:** Forgotten Crossroads and Greenpath, fully built out with platforms, hazards, and enemy placement.

## How to run it

```
./gradlew desktop:run
```

## Tech stack

- Java
- libGDX
- Gradle
- Tiled (for map data)
- Gson (for save data)
