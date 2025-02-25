# Clear the first 140 blocks after the script address in case of clobber
mw.l 8e000258 00000000 140
ubifsload ${scriptaddr} boot/fitImage
ubifsumount
ubi detach
bootm start ${scriptaddr}#conf-microchip_mpfs-icicle-kit.dtb#conf-mpfs_icicle_pmod_sf3.dtbo
bootm loados ${scriptaddr};
# Try to load a ramdisk if available inside fitImage
bootm ramdisk;
bootm prep;
run design_overlays;
bootm go;