# Hollow Knight Clone — AP Project (libGDX, MVC)

## Status: Core gameplay skeleton — compiles & runs, NOT feature-complete

This is a **working foundation**, not the finished assignment. Given the scope
of the spec (1255 mandatory points + 585 bonus, across 5 sections), no single
session can produce a finished, polished submission. What's here is a solid,
correctly-architected base you can build on incrementally.

## What's implemented

**Architecture**: Full MVC separation.
- `model/` — pure data + state, no libGDX rendering calls except Rectangle/Vector2 math
- `view/` — rendering only (currently placeholder ShapeRenderer rectangles, NOT sprites)
- `controller/` — all game logic / physics / AI, reads input, mutates model

**Systems working end-to-end:**
- Player physics: run, jump (variable height + cutoff), double jump, dash
  (gravity-off + cooldown), wall slide, pogo (resets dash/double-jump), Focus heal
- Nail attack with 4-directional hitbox (left/right/up/down for pogo)
- Soul system (gain on hit, spend on Focus/spells, capped at 99)
- Mask/HP system with i-frames, knockback, respawn-to-checkpoint
- Two spells: Vengeful Spirit (projectile) and Howling Wraiths (3-tick burst)
- 8 charms as an enum + CharmManager with notch-limit enforcement
- Enemy AI: generic ground walker (turn at wall/cliff), Mosquito (delayed dash),
  Husk Hornhead (patrol->rest->charge state machine), Crystal Guardian (laser->enrage->cooldown)
- False Knight boss: full 5-move decision system (distance-based + randomization +
  anti-spam), stun at 50% HP, phase 2 speed scaling, power slam wave attack
- EventBus (Observer pattern) wired to an AchievementSystem (4 achievements)
- SaveData (POJO) + SaveManager (Gson JSON read/write) — game-state save/load skeleton
- Cheat codes (Ctrl+B/N/H/R/G/K) via CheatController
- Camera: lerp follow + screen shake + arena clamp
- Two rooms built (Forgotten Crossroads, Greenpath) with platforms/hazards/enemy placement

## Sprites/Animation system (just added)

Player, all enemy types, and the boss now render via real
`Animation<TextureRegion>` frames instead of static rectangles:

- `util/PixelArtFactory.java` — generates procedural pixel-art frames
  (walk cycles, wing flaps, slash arcs, boss poses) using `Pixmap` at
  runtime. This exists because the real Hollow Knight sprite sheets from
  the assignment PDF are copyrighted and must be downloaded by you — this
  is a stand-in that actually animates so the game isn't static.
- `view/anim/PlayerAnimations.java` — one `Animation` per `PlayerState`
  (idle, run, jump, fall, dash, wall-slide, hurt, focus, cast) plus a
  4-directional nail-slash overlay.
- `view/anim/EnemyAnimations.java` — walk/idle animation per `EnemyType`,
  plus a "special" animation (charge motion lines / laser beam) for
  Husk Hornhead and Crystal Guardian, and a full move-set of boss poses
  keyed by `BossState`.
- `PlayerView` / `EnemyView` were rewritten to use `SpriteBatch` +
  these animations, tracking per-entity state-time (via `IdentityHashMap`
  for enemies) so animations restart cleanly on state changes and flip
  correctly with facing direction.

**To swap in the real assets later:** replace the bodies of
`PlayerAnimations` / `EnemyAnimations` to load real
`Texture`/`TextureAtlas` files (e.g.
`new Texture(Gdx.files.internal("sprites/knight_run.png"))` sliced via
`TextureRegion.split()`) instead of calling `PixelArtFactory`. Nothing in
`PlayerView`, `EnemyView`, or `GameScreen` needs to change — they only
consume `Animation<TextureRegion>`.

## Real Knight sprites (just added)

The player now renders with the actual Knight sprite sheets under
`core/assets/sprites/knight/` instead of procedural placeholders:

- `idle.png` (9 frames), `run.png` (13), `airborne.png` (12 — split into
  rising 0-5 / falling 6-11), `dash.png` (12), `double_jump.png` (8),
  `slash.png` (5, rightward swing), `idle_hurt.png` (12), `death.png` (18).
  All frames are 349x186, single row per sheet.
- `util/SpriteSheetLoader` slices any single-row sheet into
  `TextureRegion[]`. Reuse this for enemy/boss art later.
- `view/anim/PlayerAnimations` now takes the `AssetManager` and builds one
  `Entry` (animation + loop flag) per `PlayerState`. `AssetLoader` queues
  and loads the 8 sheets with `TextureFilter.Nearest` (crisp pixel art, no
  blur) before building the animation set.
- `PlayerView` draws at a fixed visual height (frame aspect ratio
  preserved), centered on and bottom-aligned to the physics hitbox — the
  sprite frames are much bigger than the 32x48 hitbox by design (room for
  the nail swing/dash motion), so visual size and collision size are
  intentionally decoupled, same as the real game.
- Attack animation: LEFT/RIGHT attacks play the real `slash.png` body
  animation; UP/DOWN (pogo) attacks keep the current airborne pose and
  layer a small procedural white arc on top, since this pack has no
  dedicated up/down slash art.
- Double-jump and death now have real timers (`doubleJumpTimer`,
  `deathTimer` on `PlayerModel`) so their animations actually get screen
  time instead of being overwritten the same frame — death now plays out
  fully (1.44s) before respawn+heal instead of being instant.
- `FOCUSING`, `CASTING_SPELL`, `WALL_SLIDING` have no dedicated art in this
  pack and currently reuse Idle/a held Idle frame/a held Fall frame —
  swap in real sheets for these later the same way (see
  `PlayerAnimations`' class doc).

Enemy/boss art is still procedural (`PixelArtFactory`) — same swap pattern
applies whenever you get real enemy sheets.

## TMX map support (Tiled)

Forgotten Crossroads now loads from a real `.tmx` file instead of
hardcoded platform calls:

- `core/assets/maps/forgotten_crossroads.tmx` + `tileset.tsx` + a small
  placeholder `tileset.png` (4 solid-color tiles: ground/platform/hazard/
  decor) generated programmatically since the real HK tileset from the
  assignment PDF has to be downloaded by you.
- `model/world/tiled/TiledRoomLoader` reads a `.tmx`'s object layers —
  `Collisions` (rectangles → platforms), `Hazards` (rectangles + optional
  `damage` property), `Enemies` (point objects with a `type` property
  matching an `EnemyType` name), `Spawn` (one point object named
  `PlayerSpawn`) — and builds a `Room` from them. The tile layer itself is
  visual-only, rendered separately via `OrthogonalTiledMapRenderer`.
- **Coordinate note**: Tiled objects use a top-left origin with Y
  increasing downward; the game world uses bottom-left/Y-up. The loader
  converts on the way in — nothing downstream needs to care.
- Greenpath is still hand-coded (`WorldModel.buildGreenpath()`) as a
  reference for quick prototyping without a map file. Convert it to a
  `.tmx` the same way once you're ready — see `TiledRoomLoader`'s class
  doc for the exact layer schema to replicate in Tiled.
- `GameModel` now spawns the player at the current room's actual spawn
  point (from the `Spawn` layer) instead of a hardcoded position.
- **Build config**: `build.gradle`'s desktop module now points
  `sourceSets.main.resources.srcDirs` at `../core/assets` so
  `Gdx.files.internal(...)` can find `maps/forgotten_crossroads.tmx` etc.
  at runtime. Re-sync Gradle after pulling this change. If the TMX fails
  to load for any reason, `WorldModel` falls back to a hardcoded version
  of the same room so the game still runs.

**To build the real level in Tiled:** open Tiled, create a map with
`tilewidth`/`tileheight` = 32 to match, add a tile layer for your real
tileset art, then add object layers named exactly `Collisions`, `Hazards`,
`Enemies`, `Spawn` with rectangles/points as described above. Drop the
`.tmx` (and its tileset) under `core/assets/maps/` and point
`WorldModel`'s loader call at the new filename.

## What's NOT implemented yet (you need to add these)

1. **City of Tears & Crystal Peaks rooms + their TMX maps** — only
   Forgotten Crossroads (TMX) and Greenpath (hardcoded) exist so far.
2. **Remaining enemy model classes** — Mosscreep, Tiktik, Crystal Crawler,
   Mossfly, Winged Sentry, Crystal Hunter have animations reserved in
   `EnemyAnimations` but no `EnemyModel` subclass or AI yet (follow the
   `CrawlidModel`/`MosquitoModel` pattern). `TiledRoomLoader.createEnemy()`
   needs a new `case` for each as you add them.
3. **Settings / Guide / Achievements / Pause / Inventory menus** — only a
   keyboard-driven MainMenuScreen stub exists. Build these with Scene2D.UI
   (Stage + Skin + TextButton) for clickable UI.
4. **Audio** — AssetLoader has commented-out load calls; no SFX/BGM playback
   wired into EventBus listeners yet.
5. **Zote NPC**, breakable walls/secret rooms, localization, end-screen,
   database storage — all bonus-point items, not started.
6. **Inventory/Pause/Settings UI actually toggling game state** — the flags
   exist on GameModel (paused, inInventory) and block updates correctly,
   but there's no visual menu to interact with charms yet (CharmManager is
   ready, just needs a Scene2D screen calling charms.toggle(charm)).
7. **Enemy/boss real sprite art** — still procedural (PixelArtFactory).
   Same swap pattern as the Knight: get real sheets, rewrite
   EnemyAnimations to load+slice them, EnemyView doesn't need to change.

## How to build/run

    ./gradlew desktop:run

(You'll need a gradlew wrapper — run `gradle wrapper` once you have Gradle
installed, or import as a Gradle project in IntelliJ which generates it.
After pulling the TMX changes, re-sync Gradle so the desktop module picks
up the new `sourceSets.main.resources.srcDirs` pointing at `core/assets`.)

## Suggested order to keep working

1. Get it compiling and running in IntelliJ/Gradle — confirm the Knight
   animates and Forgotten Crossroads renders from the TMX tileset.
2. Build City of Tears + Crystal Peaks as `.tmx` maps in Tiled (match the
   Collisions/Hazards/Enemies/Spawn layer schema TiledRoomLoader expects),
   plus their enemy model classes.
3. Build Settings/Pause/Inventory with Scene2D.UI — CharmManager and
   InputController (rebind) are already there for it to call into.
4. Add SFX/BGM via EventBus listeners (e.g. AudioSystem implements
   EventBus.Listener, subscribes to NAIL_SLASH, PLAYER_DAMAGED, etc.)
5. Polish: particle effects, breakable walls, Zote, end screen, localization.
