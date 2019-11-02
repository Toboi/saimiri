# Saimiri
Collection of utilities for writing 2D games with the [jMonkeyEngine](https://github.com/jMonkeyEngine/jmonkeyengine), mostly using the [zay-es](https://github.com/jMonkeyEngine-Contributions/zay-es) entity system.
## Gradle
```groovy
repositories {
    jcenter()
}
dependencies {
    compile 'de.toboidev:saimiri-base:0.0.1'
    compile 'de.toboidev:saimiri-game:0.0.1'
    compile 'de.toboidev:saimiri-gfx:0.0.1'
}
```
## Modules
### saimiri-base
Contains some base classes and components that are used by all modules.
### saimiri-game
Systems and components related to gameplay. At the moment it contains only a simple collision system.
### saimiri-gfx
Rendering-related systems, and a deferred lighting processor for 2D.

## Examples
### simpleDeferredLighting
Showcase for the deferred lighting.
### simpleCollision
Simple platformer level using the collision system.
### esCollision
Simple platformer level using the entity system-version of the collision system.
