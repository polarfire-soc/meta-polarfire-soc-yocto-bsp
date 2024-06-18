#
# Note: dump from https://github.com/pengutronix/meta-ptx/blob/master/classes-recipe/genimage.bbclass
#
# Class to generate disk images using the `genimage` tool.
#
# In order to build an image, your recipe must inherit the genimage class and
# have a valid genimage configuration file in SRC_URI, named `genimage.config`
# by default.
#
#   inherit genimage
#
#   SRC_URI += "file://genimage.config"
#
# You also need to depend on all recipes creating artifacts used by
# genimage to build the final (disk) image, e.g.:
#
#   do_genimage[depends] += "virtual/bootloader:do_deploy core-image-minimal:do_image_complete"
#
# The main purpose of genimage is to create an entire SD, eMMC, NAND, or UBI
# image with multiple partitions based on different images (kernel,
# bootloader, rootfs, ...)
#
# The name of the resulting image is named the same way normal images are
# named. You can customize output with the variables `GENIMAGE_IMAGE_NAME` and
# `GENIMAGE_IMAGE_SUFFIX`.
#
# Note that you should also make your genimage image recipe depend on the set
# of host tools required for building, e.g.
#
#   DEPENDS += "e2fsprogs-native genext2fs-native"
#
# You can also use genimage to split up a created rootfs into different
# partition images. Consider a yocto-created rootfs, for example.
# You can put all content of the /home directory in a 'data' partition while
# putting all content of /etc in a config partition and the rest ('/') in the
# final rootfs partition, then pack all them together to an SD image.
#
# In order to do this, you have to provide the name of the image recipe you
# intend to split up the data for:
#
#   GENIMAGE_ROOTFS_IMAGE = "my-production-image"
#
# The image recipe must build an archive, either `tar.bz2` (default) or the
# type matching the extension you set with `GENIMAGE_ROOTFS_IMAGE_FSTYPE`:
#
#   GENIMAGE_ROOTFS_IMAGE_FSTYPE = "tar.xz"
#
# Since nanbield the IMAGE_NAME also contains the IMAGE_NAME_SUFFIX, with it
# being set inside the image-artifact-names.bbclass. This is either '${IMAGE_NAME_SUFFIX}'
# (default) or the name matching the suffix you set with
# `GENIMAGE_ROOTFS_IMAGE_SUFFIX`:
#
#   GENIMAGE_ROOTFS_IMAGE_SUFFIX = ".rootfs"
#
# The split-up is controlled by your genimage config file, using the
# 'mountpoint' options:
#
#   datafs {
#     [...]
#     mountpoint = "/home"
#   }
#
#   rootfs {
#     [...]
#     mountpoint = "/"
#   }
#
# Most common variables for customization from image recipe:
#
# GENIMAGE_CONFIG       - config passed to genimage --config (default: 'genimage.config')
# GENIMAGE_IMAGE_SUFFIX - file extension suffix for created image (default: 'img')
# GENIMAGE_ROOTFS_IMAGE - input rootfs image to generate file system images from
# GENIMAGE_ROOTFS_IMAGE_FSTYPE  - input roofs FSTYPE to use (default: 'tar.bz2')
# GENIMAGE_ROOTFS_IMAGE_SUFFIX  - IMAGE_NAME_SUFFIX to use (default: '${IMAGE_NAME_SUFFIX}')
# GENIMAGE_VARIABLES[VAR]       - replace @VAR@ in config with variable flag value
# GENIMAGE_COMPRESSION - compress the generated image. Allowed values
# are 'none' for no compression (the default), 'gzip' and 'xz'.

inherit nopackages image-artifact-names deploy

LICENSE ?= "MIT"
PACKAGES = ""

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}"

B = "${WORKDIR}/genimage-${PN}"

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "genimage-native"

GENIMAGE_CONFIG ?= "genimage.config"

GENIMAGE_IMAGE_SUFFIX ?= "img"

GENIMAGE_IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${DATETIME}"
# Don't include the DATETIME variable in the sstate package signature
GENIMAGE_IMAGE_NAME[vardepsexclude] = "DATETIME"
GENIMAGE_IMAGE_LINK_NAME = "${IMAGE_BASENAME}-${MACHINE}"

GENIMAGE_IMAGE_FULLNAME ?= "${GENIMAGE_IMAGE_NAME}.${GENIMAGE_IMAGE_SUFFIX}"
GENIMAGE_IMAGE_LINK_FULLNAME ?= "${GENIMAGE_IMAGE_LINK_NAME}.${GENIMAGE_IMAGE_SUFFIX}"

def get_default_fstype(d):
    fstypes = d.getVar('IMAGE_FSTYPES' or '').split()
    for x in fstypes:
        if "tar" in x:
            return x
    return "tar.bz2"

GENIMAGE_ROOTFS_IMAGE ?= ""
GENIMAGE_ROOTFS_IMAGE_FSTYPE ?= "${@get_default_fstype(d)}"
GENIMAGE_ROOTFS_IMAGE_SUFFIX ?= "${IMAGE_NAME_SUFFIX}"

GENIMAGE_VARIABLES[IMAGE] = "${GENIMAGE_IMAGE_FULLNAME}"

do_genimage[vardeps] += "GENIMAGE_VARIABLES"
do_genimage[depends] += "${@'${GENIMAGE_ROOTFS_IMAGE}:do_image_complete' if '${GENIMAGE_ROOTFS_IMAGE}' else ''}"

GENIMAGE_CREATE_BMAP ?= "0"
do_genimage[depends] += "${@'bmaptool-native:do_populate_sysroot' if d.getVar('GENIMAGE_CREATE_BMAP') == '1' else ''}"

GENIMAGE_COMPRESSION ??= "none"

GENIMAGE_COMPRESS_DEPENDS[none] = ""
GENIMAGE_COMPRESS_DEPENDS[gzip] = "pigz-native:do_populate_sysroot"
GENIMAGE_COMPRESS_DEPENDS[xz]   = "xz-native:do_populate_sysroot"
do_genimage[depends] += "${@d.getVarFlag('GENIMAGE_COMPRESS_DEPENDS', '${GENIMAGE_COMPRESSION}')}"

GENIMAGE_COMPRESS_CMD[none] = ":"
GENIMAGE_COMPRESS_CMD[gzip] = "gzip -f -9 -n"
GENIMAGE_COMPRESS_CMD[xz]   = "xz -f"
GENIMAGE_COMPRESS_CMD = "${@d.getVarFlag('GENIMAGE_COMPRESS_CMD', '${GENIMAGE_COMPRESSION}')}"

GENIMAGE_TMPDIR  = "${WORKDIR}/genimage-tmp"
GENIMAGE_ROOTDIR  = "${WORKDIR}/root"
GENIMAGE_OPTS ??= ""

do_genimage_preprocess[cleandirs] = "${GENIMAGE_TMPDIR} ${GENIMAGE_ROOTDIR} ${B}"

do_create_config() {
    cp ${WORKDIR}/genimage.config ${S}
    echo "Copied genimage.config to ${S}"
    ls -l ${S}
}

addtask create_config after do_unpack before do_image

do_configure () {
    if ! grep -q "@IMAGE@" ${WORKDIR}/${GENIMAGE_CONFIG}; then
        bbnote "${GENIMAGE_CONFIG} does not contain @IMAGE@ marker"
    fi
}

do_genimage_preprocess[dirs] = "${B}"

python do_genimage_preprocess () {
    def expandvar(m):
        val = d.getVarFlag('GENIMAGE_VARIABLES', m.group(1), expand=True)
        if val is None:
            bb.error("cannot expand variable @%s@ from GENIMAGE_VARIABLES" % m.group(1))
        return val

    import re

    infile = d.getVar('WORKDIR') + "/" + d.getVar('GENIMAGE_CONFIG')
    outfile = d.getVar('B') + "/.config"

    with open(infile, "r+") as input:
        expansion = re.sub(r"@([^{}@\n\t :]+)@", expandvar, input.read())
    with open(outfile, "w") as output:
        output.write(expansion)
}

fakeroot do_genimage () {
    # unpack input rootfs image if given
    if [ "x${GENIMAGE_ROOTFS_IMAGE}" != "x" ]; then
        bbnote "Unpacking ${DEPLOY_DIR_IMAGE}/${GENIMAGE_ROOTFS_IMAGE}-${MACHINE}${GENIMAGE_ROOTFS_IMAGE_SUFFIX}.${GENIMAGE_ROOTFS_IMAGE_FSTYPE} to ${GENIMAGE_ROOTDIR}"
        tar -xf ${DEPLOY_DIR_IMAGE}/${GENIMAGE_ROOTFS_IMAGE}-${MACHINE}${GENIMAGE_ROOTFS_IMAGE_SUFFIX}.${GENIMAGE_ROOTFS_IMAGE_FSTYPE} -C ${GENIMAGE_ROOTDIR}
    fi

    genimage \
        --loglevel 2 \
        --config ${B}/.config \
        --tmppath ${GENIMAGE_TMPDIR} \
        --inputpath ${DEPLOY_DIR_IMAGE} \
        --includepath ${WORKDIR} \
        --outputpath ${B} \
        --rootpath ${GENIMAGE_ROOTDIR} \
        ${GENIMAGE_OPTS}

    if [ "${GENIMAGE_CREATE_BMAP}" = 1 ] ; then
        bmaptool create -o ${B}/${GENIMAGE_IMAGE_FULLNAME}.bmap ${B}/${GENIMAGE_IMAGE_FULLNAME}
    fi

    ${GENIMAGE_COMPRESS_CMD} ${B}/${GENIMAGE_IMAGE_FULLNAME}

    rm ${B}/.config
}
do_genimage[depends] += "virtual/fakeroot-native:do_populate_sysroot"
do_genimage[prefuncs] += "do_genimage_preprocess"
SSTATE_SKIP_CREATION:task-genimage = '1'

addtask genimage after do_configure

do_deploy () {
    install -m 0644 ${B}/* ${DEPLOYDIR}/

    for img in ${B}/*; do
        img=$(basename "${img}")
        case "$img" in *"${GENIMAGE_IMAGE_FULLNAME}"*)
            ln -sf ${img} \
                ${DEPLOYDIR}/$(echo "${img}" | sed "s/${GENIMAGE_IMAGE_FULLNAME}/${GENIMAGE_IMAGE_LINK_FULLNAME}/")
        esac
    done
}

addtask deploy after do_genimage before do_build

do_patch[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
deltask do_populate_sysroot
