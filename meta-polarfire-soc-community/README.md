# meta-polarfire-soc-community

This layer contains Microchip's partners' evaluation kits' machine
configuration files and associated configuration fragments.

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
