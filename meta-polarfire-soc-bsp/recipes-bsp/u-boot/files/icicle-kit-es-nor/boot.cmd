ubifsload ${scriptaddr} boot/fitImage
ubifsumount
ubi detach
bootm start ${scriptaddr}#conf-microchip_mpfs-icicle-kit.dtb#conf-mpfs_icicle_pmod_sf3.dtbo
bootm loados ${scriptaddr};
# Try to load a ramdisk if available inside fitImage
bootm ramdisk;
bootm prep;
fdt set /soc/ethernet@20112000 mac-address ${icicle_mac_addr0};
fdt set /soc/ethernet@20110000 mac-address ${icicle_mac_addr1};
run design_overlays;
bootm go;