DESCRIPTION = "SiFive RISC-V Core IP Demo Benchmarks Linux image"

IMAGE_FEATURES += "\
    splash \
    ssh-server-openssh \
    tools-sdk \
    tools-debug \
    tools-profile \
    doc-pkgs \
    dev-pkgs \
    dbg-pkgs \
    nfs-client \
    nfs-server"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    kernel-modules \
    kernel-devsrc \
    kernel-dev \
    sysstat \
    dhrystone \
    whetstone \
    iperf3 \
    iperf2 \
    tinymembench \
    sysbench \
    memtester \
    lmbench \
    vim \
    nano \
    mc \
    chrony \
    curl \
    wget \
    git \
    bind-utils \
    networkmanager \
    networkmanager-nmtui \
    networkmanager-nmtui-doc \
    haveged \
    e2fsprogs-resize2fs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs \
    parted \
    gptfdisk \
    rsync \
    screen \
    tmux \
    stress-ng \
    dhcp-client \
    nbd-client \
    mpfr-dev \
    gmp-dev \
    libmpc-dev \
    zlib-dev \
    flex \
    bison \
    dejagnu \
    gettext \
    texinfo \
    procps \
    glibc-dev \
    elfutils \
    elfutils-dev \
    pciutils \
    usbutils \
    devmem2 \
    mtd-utils \
    sysfsutils \
    htop \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

IMAGE_INSTALL_append_lc-mpfs = "\
    unleashed-udev-rules \
    "
IMAGE_INSTALL_append_mpfs = "\
    unleashed-udev-rules \
    "
inherit core-image extrausers

EXTRA_USERS_PARAMS = "usermod -P microchip root;"
