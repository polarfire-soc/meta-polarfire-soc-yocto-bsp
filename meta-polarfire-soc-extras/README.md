# meta-polarfire-soc-extras

This layer contains recipes that extend and supplement the
meta-polarfire-soc-bsp layer. These include additional applications
and demos to demonstrate PolarFire SoC features.

## Dependencies

This layer depends on:

```text
URI: git://git.openembedded.org/openembedded-core
layers: meta
branch: scarthgap

URI: git://git.openembedded.org/meta-openembedded
layers: meta-oe, meta-python, meta-multimedia, meta-networking, meta-webserver
branch: scarthgap

URI: git://github.com/polarfire-soc/meta-polarfire-soc-yocto-bsp.git
layers: meta-polarfire-soc-bsp
branch: master or PolarFire SoC Linux release version (e.g. v2023.09)
```
