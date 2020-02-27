DESCRIPTION = "Microchip MPFS Development CLI Linux image"

inherit core-image extrausers
EXTRA_USERS_PARAMS = "usermod -P microchip root;"

IMAGE_FEATURES += "\
    dev-pkgs \
    dbg-pkgs \
    doc-pkgs \
    tools-profile \
    tools-debug \
    tools-profile \
    tools-sdk \
    eclipse-debug \
    ssh-server-openssh"


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
    nvme-cli \
    python3 \
    cmake \
    python3-scons \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

