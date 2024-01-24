setenv fdt_high 0xffffffffffffffff
setenv initrd_high 0xffffffffffffffff

load mmc 0:${distro_bootpart} ${scriptaddr} fitImage
bootm start ${scriptaddr}#conf-microchip_mpfs-video-kit.dtb#conf-mpfs_h264.dtbo;
bootm loados ${scriptaddr};
# Try to load a ramdisk if available inside fitImage
bootm ramdisk;
bootm prep;
fdt set /soc/ethernet@20112000 mac-address ${videokit_mac_addr0};
fdt set /soc/ethernet@20110000 mac-address ${videokit_mac_addr1};
run design_overlays;
bootm go;
