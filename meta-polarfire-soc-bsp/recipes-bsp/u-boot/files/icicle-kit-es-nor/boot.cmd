
ubifsload 0x80000000 boot/fitImage
cp 0x80000000 ${scriptaddr} ${filesize}
setenv base_args "earlycon=sbi uio_pdrv_genirq.of_id=generic-uio"
setenv bootargs "${base_args} ubi.mtd=1 root=ubi0:rootfs rootfstype=ubifs rootwait rw mtdparts=spi2.0:2m(payload),28m(ubi)"
bootm start ${scriptaddr}#conf-microchip_mpfs-icicle-kit.dtb#conf-mpfs_icicle_pmod_sf3.dtbo
bootm loados ${scriptaddr};
# Try to load a ramdisk if available inside fitImage
bootm ramdisk;
bootm prep;
fdt set /soc/ethernet@20112000 mac-address ${icicle_mac_addr0};
fdt set /soc/ethernet@20110000 mac-address ${icicle_mac_addr1};
run design_overlays;
bootm go;


