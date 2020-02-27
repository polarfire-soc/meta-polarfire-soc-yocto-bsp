FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_riscv64 = "file://nss-3.50-fix-riscv64.patch \
                          "
