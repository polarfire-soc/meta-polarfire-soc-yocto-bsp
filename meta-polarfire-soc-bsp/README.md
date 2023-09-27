# meta-polarfire-soc-bsp

This layer contains PolarFire SoC's evaluation boards' metadata such as machine configuration files and core recipes (Linux, U-Boot, etc.).

## Dependencies

This layer depends on:

```text
URI: git://git.openembedded.org/openembedded-core
layers: meta
branch: kirkstone

URI: git://git.openembedded.org/meta-openembedded
layers: meta-oe, meta-python, meta-multimedia, meta-networking, meta-webserver
branch: kirkstone
```