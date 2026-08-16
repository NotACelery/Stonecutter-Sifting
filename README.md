# Stonecutter Sifting

**Stonecutter Sifting** is a NeoForge mod for Minecraft 1.21.1 that gives the Stonecutter a second purpose beyond its normal cutting recipes. Drop supported block-items directly onto a Stonecutter to sift them into useful resources. Each Stonecutter processes one input every four ticks.

It is made for OneBlock, SkyBlock, superflat, and other limited worlds, where many common blocks have little use while important resources can become locked behind scarce or RNG-dependent world access. Sifting turns those blocks into renewable, chance-based paths to the materials players need, without replacing normal progression.

## Features

- 13 sifting inputs across the Overworld, Nether, and End.
- Built-in JEI and EMI support so every table and chance is visible in-game.
- English and Spanish translations (Argentina, Chile, Mexico, and Spain).
- Optional compatibility with Eruruu Patch for its Crimson and Warped Cultures. It never requires that mod.
- A configurable exclusion tag for saplings from other dimensions.

## Sifting tables

| Input | Main rewards |
| --- | --- |
| Sand / Red Sand | Desert plants, terracotta, and gold recovery |
| Gravel | Flint and weighted mineral extras, including a 1% Diamond chance |
| Dirt / Podzol | Seeds, saplings, and early vegetation |
| Rooted Dirt / Moss | Lush Caves vegetation |
| Clay / Mud | Aquatic and mangrove vegetation |
| Soul Sand / Netherrack / Blackstone | Nether crops, resources, and a 0.01% Ancient Debris chance from Netherrack |
| End Stone | Chorus and End building resources |

All gameplay rolls and the JEI/EMI displays are sourced from `SiftingTables.java`, keeping the documented viewer data aligned with actual gameplay.

## Modpack configuration

`#stonecutter_sifting:non_overworld_saplings` is an exclusion tag. It is empty by default, so compatible modded Overworld saplings can join Dirt's surprise pool. Add saplings from other dimensions to this tag to keep them out of that pool.

## Credits

- **Celerbi** — mod development
- **Mausermeyer** — Stonecutter Sifting logo artwork

## Build from source

Requires Java 21 and NeoForge 21.1.235+.

```powershell
./gradlew clean build
```

The release JAR is generated in `build/libs`.
